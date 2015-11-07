package ninja.eivind.mpq;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqHeader {

    public static final int SIGNATURE = 0x1a51504d;
    private int length;
    private long archiveLength;
    private short format;
    private short blockSize;
    private long hashTablePosition;
    private long blockTablePosition;
    private int numberOfHashEntries;
    private int numberOfBlockEntries;
    private int position;

    public void setLength(int length) {
        this.length = length;
    }

    public void setArchiveLength(long archiveLength) {
        this.archiveLength = archiveLength;
    }

    public void setFormat(short format) {
        this.format = format;
    }

    public void setBlockSize(short blockSize) {
        this.blockSize = blockSize;
    }

    public void setHashTablePosition(long hashTablePosition) {
        this.hashTablePosition = hashTablePosition;
    }

    public void setBlockTablePosition(long blockTablePosition) {
        this.blockTablePosition = blockTablePosition;
    }

    public void setNumberOfHashEntries(int numberOfHashEntries) {
        this.numberOfHashEntries = numberOfHashEntries;
    }

    public void setNumberOfBlockEntries(int numberOfBlockEntries) {
        this.numberOfBlockEntries = numberOfBlockEntries;
    }

    public short getFormat() {
        return format;
    }

    public int getLength() {
        return length;
    }

    public short getBlockSize() {
        return blockSize;
    }

    public int getNumberOfHashEntries() {
        return numberOfHashEntries;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public long getHashTablePosition() {
        return hashTablePosition;
    }

    public long getArchiveLength() {
        return archiveLength;
    }

    public int getNumberOfBlockEntries() {
        return numberOfBlockEntries;
    }

    public long getBlockTablePosition() {
        return blockTablePosition;
    }
}
