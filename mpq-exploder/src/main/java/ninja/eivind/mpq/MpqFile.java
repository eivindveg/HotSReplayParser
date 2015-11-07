package ninja.eivind.mpq;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqFile {
    /**
     * Flags
     */
    public static final int IMPLODED = 0x100;
    public static final int COMPRESSED = 0x00000200;
    public static final int ENCRYPTED = 0x00010000;
    public static final int FIXSEED = 0x00020000;
    public static final int SINGLE_UNIT = 0x01000000;
    public static final int DUMMY_FILE = 0x02000000;
    public static final int HAS_EXTRA = 0x04000000;
    public static final int EXISTS = 0x80000000;
    private final MpqHash mpqHash;
    private final long position;
    private final long compressedSize;
    private final long fileSize;
    private final int flags;
    private String name;

    public MpqFile(MpqHash mpqHash, long position, long compressedSize, long fileSize, int flags) {
        this.mpqHash = mpqHash;

        this.position = position;
        this.compressedSize = compressedSize;
        this.fileSize = fileSize;
        this.flags = flags;
    }

    public MpqHash getMpqHash() {
        return mpqHash;
    }

    public boolean isEncrypted() {
        return (ENCRYPTED & flags) == ENCRYPTED;
    }

    public long getPosition() {
        return position;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public boolean isSingleUnit() {
        return (SINGLE_UNIT & flags) == SINGLE_UNIT;
    }

    public boolean isCompressed() {
        return (COMPRESSED & flags) == COMPRESSED;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
