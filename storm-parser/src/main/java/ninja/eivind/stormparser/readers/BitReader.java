package ninja.eivind.stormparser.readers;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Eivind Vegsundvåg
 */
public class BitReader implements AutoCloseable {

    private int cursor;
    private InputStream inputStream;
    private int currentByte;

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.cursor = 0;
    }

    public long read(int numBits) throws IOException {
        //System.out.println("Cursor before: " + cursor);
        if (numBits > 32) {
            throw new UnsupportedOperationException("Number of bits must be less than 32.");
        }

        long value = 0;
        while (numBits > 0) {
            int bytePos = this.cursor & 7;
            int bitsLeftInByte = 8 - bytePos;
            if (bytePos == 0) {
                byte[] b = new byte[1];
                int bytesRead = inputStream.read(b);
                if (bytesRead != 1) {
                    throw new UnsupportedOperationException("Unexpected number of bytes read.");
                }
                currentByte = b[0] & 0xFF;
            }

            int bitsToRead = (bitsLeftInByte > numBits) ? numBits : bitsLeftInByte;

            long shifted = value << bitsToRead;
            long currentByteShifted = Integer.toUnsignedLong(this.currentByte) >> bytePos;
            long mask = (Integer.toUnsignedLong(1) << bitsToRead) - Integer.toUnsignedLong(1);

            value = shifted | (currentByteShifted & mask);

            this.cursor += bitsToRead;
            numBits -= bitsToRead;
        }
        //System.out.println("Cursor after: " + cursor);
        return value;
    }

    public void alignToByte() {
        if ((this.cursor & 7) > 0) {
            this.cursor = (this.cursor & 0x7ffffff8) + 8;
        }
    }

    public byte readByte() throws IOException {
        return (byte) this.read(8);
    }

    public boolean readBoolean() throws IOException {
        return this.read(1) == 0x1;
    }

    public short readInt16() throws IOException {
        return (short) this.read(16);
    }

    public long readInt() throws IOException {
        return this.read(32);
    }

    public byte[] readBytes(int bytes) throws IOException {
        byte[] buffer = new byte[bytes];
        for (int i = 0; i < bytes; i++) {
            buffer[i] = this.readByte();
        }

        return buffer;
    }

    public String readString(int length) throws IOException {
        byte[] buffer = this.readBytes(length);
        return new String(buffer, "UTF-8");
    }

    public byte[] readBlobPrecededWithLength(int numBitsForLength) throws IOException {
        int stringLength = (int) read(numBitsForLength);
        alignToByte();
        return readBytes(stringLength);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public void rewind(int i) throws IOException {
        System.out.println("Rewinding");
    }


    public long readVariableUnsignedInt() throws IOException {
        long b = read(8);
        boolean negative = (b & 1) == 1;
        long result = (b >> 1) & 0x3F;
        int bits = 6;
        while((b & 0x80) != 0) {
            b = read(8);
            result |= (b & 0x7f) << bits;
            bits += 7;
        }
        if (negative) {
            return -result;
        } else {
            return result;
        }
    }
}
