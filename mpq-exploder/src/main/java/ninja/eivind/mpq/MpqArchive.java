package ninja.eivind.mpq;

import ninja.eivind.mpq.builders.MpqHeaderBuilder;
import ninja.eivind.mpq.streams.ByteBufferBackedInputStream;
import ninja.eivind.mpq.streams.MpqFileInputStream;
import ninja.eivind.mpq.utils.Decryption;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqArchive {

    public static final int BLOCK_TABLE_SIZE = 16;
    private static final String HASH_TABLE_KEY = "(hash table)";
    private static final String BLOCK_TABLE_KEY = "(block table)";
    private static final String LIST_FILE_NAME = "(listfile)";
    private File file;
    private MpqHeader header;

    public MpqArchive(File file) {
        if (!file.exists()) {
            throw new UnsupportedOperationException("Could not locate file " + file);
        }
        this.file = file;
    }

    public MpqArchive(String fileName) {
        this(new File(fileName));
    }

    public Map<String, ByteBuffer> getFiles(String... filesToExtract) throws IOException {
        Map<String, ByteBuffer> map = new HashMap<>();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            FileChannel fileChannel = inputStream.getChannel();

            header = getHeader(fileChannel);
            int hashTableSize = header.getNumberOfHashEntries() * 16;
            long hashTablePosition = header.getHashTablePosition() + header.getPosition();
            final ByteBuffer encryptedHashTable = wrap(fileChannel.map(FileChannel.MapMode.READ_ONLY, hashTablePosition, hashTableSize)).order(LITTLE_ENDIAN);
            final ByteBuffer hashTable = Decryption.decryptTable(encryptedHashTable, HASH_TABLE_KEY).order(LITTLE_ENDIAN);

            int blocks = header.getNumberOfBlockEntries();
            int blockTableSize = blocks * BLOCK_TABLE_SIZE;
            long blockTablePosition = header.getBlockTablePosition() + header.getPosition();

            ByteBuffer encryptedBlockTable = wrap(fileChannel.map(FileChannel.MapMode.READ_ONLY, blockTablePosition, blockTableSize)).order(LITTLE_ENDIAN);
            ByteBuffer blockTable = Decryption.decryptTable(encryptedBlockTable, BLOCK_TABLE_KEY).order(LITTLE_ENDIAN);

            List<MpqFile> mpqFiles = getMpqFiles(hashTable, blockTableSize, blockTable);
            Optional<MpqFile> listFileOptional = getMpqFileByName(mpqFiles, LIST_FILE_NAME);
            if (listFileOptional.isPresent()) {
                MpqFile listFile = listFileOptional.get();

                try (InputStream listFileInputStream = getInputStream(listFile, fileChannel);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(listFileInputStream))) {
                    String fileName;
                    while((fileName = reader.readLine()) != null) {
                        if (Arrays.asList(filesToExtract).contains(fileName)) {
                            Optional<MpqFile> mpqFileOptional = getMpqFileByName(mpqFiles, fileName);
                            if (!mpqFileOptional.isPresent()) {
                                throw new FileNotFoundException("Could not locate file in archive");
                            }
                            MpqFile mpqFile = mpqFileOptional.get();
                            mpqFile.setName(fileName);
                            map.put(fileName, new MpqFileMapper(mpqFile).map(getInputStream(mpqFile, fileChannel)));
                        }
                    }
                }
            }

        }
        return map;
    }

    private ByteBuffer wrap(MappedByteBuffer map) {
        byte[] encryptedHashTableBytes = new byte[map.limit()];
        map.get(encryptedHashTableBytes);
        return ByteBuffer.wrap(encryptedHashTableBytes);
    }

    private InputStream getInputStream(MpqFile entry, FileChannel fileChannel) throws IOException {
        long positon = header.getPosition() + entry.getPosition();
        long size = entry.getCompressedSize();
        ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, positon, size).order(
                LITTLE_ENDIAN);

        InputStream inputStream = new ByteBufferBackedInputStream(buffer);
        if (entry.isSingleUnit()) {
            return new MpqFileInputStream(inputStream, entry);
        }
        return inputStream;
    }

    private Optional<MpqFile> getMpqFileByName(List<MpqFile> mpqFiles, String listFileName) {
        return mpqFiles.stream().filter(file -> file.getMpqHash().equals(new MpqHash(listFileName))).findFirst();
    }

    private List<MpqFile> getMpqFiles(ByteBuffer hashTable, int blockTableSize, ByteBuffer blockTable) {
        List<MpqFile> files = new ArrayList<>();

        for (int i = 0; i < header.getNumberOfHashEntries(); i++) {
            int hash1 = hashTable.getInt();
            int hash2 = hashTable.getInt();

            hashTable.position(hashTable.position() + 4);
            int blockIndex = hashTable.getInt();

            if (blockIndex < 0) {
                continue;
            }
            int pos = blockIndex * BLOCK_TABLE_SIZE;
            if (pos > blockTableSize - BLOCK_TABLE_SIZE) {
                continue;
            }
            blockTable.position(pos);
            long position = Integer.toUnsignedLong(blockTable.getInt());
            long compressedSize = Integer.toUnsignedLong(blockTable.getInt());
            long fileSize = blockTable.getInt();
            int flags = blockTable.getInt();
            if (compressedSize > 0) {
                MpqHash mpqHash = new MpqHash(hash1, hash2);
                MpqFile entry = new MpqFile(mpqHash, position, compressedSize,
                        fileSize, flags);
                files.add(entry);
            }
        }

        return files;
    }

    MpqHeader getHeader(FileChannel fileChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);

        int position = 0;
        while (true) {
            fileChannel.read(buffer, position);
            buffer.flip();
            if (buffer.getInt() == MpqHeader.SIGNATURE) {
                int length = buffer.getInt();
                ByteBuffer headerBuffer = ByteBuffer.allocate(length - 8).order(ByteOrder.LITTLE_ENDIAN);
                fileChannel.read(headerBuffer, position + 8);
                headerBuffer.flip();
                return new MpqHeaderBuilder(headerBuffer, length, position).build();
            }
            buffer.clear();
            position += buffer.limit();
        }
    }
}
