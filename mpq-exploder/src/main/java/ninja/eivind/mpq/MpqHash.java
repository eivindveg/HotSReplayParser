package ninja.eivind.mpq;

import ninja.eivind.mpq.utils.Decryption;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqHash {
    private final int hash1;
    private final int hash2;

    protected MpqHash(int hash1, int hash2) {
        this.hash1 = hash1;
        this.hash2 = hash2;
    }

    protected MpqHash(String str) {
        this(Decryption.hash(str, 0x100), Decryption.hash(str, 0x200));
    }

    @Override
    public int hashCode() {
        return hash2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof MpqHash) {
            MpqHash that = (MpqHash) o;
            return this.hash1 == that.hash1 && this.hash2 == that.hash2;
        }
        return false;
    }
}
