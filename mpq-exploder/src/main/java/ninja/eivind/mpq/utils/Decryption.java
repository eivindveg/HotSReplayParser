package ninja.eivind.mpq.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Eivind Vegsundvåg
 */
public final class Decryption {
    private static final int[] buffer;

    static {
        buffer = new int[0x500];
        int seed = 0x100001;
        for (int i = 0; i < 0x100; i++) {
            int index = i;
            for (int j = 0; j < 5; j++, index += 0x100) {
                seed = (seed * 125 + 3) % 0x2aaaab;
                int temp = (seed & 0xffff) << 16;
                seed = (seed * 125 + 3) % 0x2aaaab;
                buffer[index] = temp | (seed & 0xffff);
            }
        }
    }

    private Decryption() {
    }

    public static ByteBuffer decryptTable(ByteBuffer hashTable, String hashTableKey) {
        int seed1 = hash(hashTableKey, 0x300);
        int seed2 = 0xeeeeeeee;

        byte[] decrypted = new byte[hashTable.limit()];
        for (int i = 0; i < hashTable.limit() - 3; i += 4) {
            seed2 += buffer[(0x400 + (seed1 & 0xff))];
            int result = mapToInt(hashTable, i);
            result ^= seed1 + seed2;

            seed1 = ((~seed1 << 21) + 0x11111111) | (seed1 >>> 11);
            seed2 = result + seed2 + (seed2 << 5) + 3;

            decrypted[i] = ((byte) (result & 0xff));
            decrypted[i + 1] = ((byte) ((result >> 8) & 0xff));
            decrypted[i + 2] = ((byte) ((result >> 16) & 0xff));
            decrypted[i + 3] = ((byte) ((result >> 24) & 0xff));
        }

        return ByteBuffer.wrap(decrypted).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static int mapToInt(ByteBuffer buffer, int offset) {
        int v = 0;
        for (int i = offset; i < offset + 4; i++) {
            int n = (buffer.array()[i] < 0 ? buffer.array()[i] + 256 : buffer.array()[i]) << (8 * i);
            v += n;
        }
        return v;
    }

    public static int hash(String hashTableKey, int offset) {
        int seed1 = 0x7fed7fed;
        int seed2 = 0xeeeeeeee;

        for (char c : hashTableKey.toCharArray()) {
            int val = (int) Character.toUpperCase(c);
            seed1 = buffer[offset + val] ^ (seed1 + seed2);
            seed2 = (val + seed1 + seed2 + (seed2 << 5) + 3);
        }
        return seed1;
    }
}
