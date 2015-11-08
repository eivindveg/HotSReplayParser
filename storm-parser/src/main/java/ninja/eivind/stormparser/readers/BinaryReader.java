package ninja.eivind.stormparser.readers;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Eivind Vegsundvåg
 */
public class BinaryReader implements AutoCloseable {

    private InputStream inputStream;

    public BinaryReader(InputStream inputStream) {

        this.inputStream = inputStream;
    }

    public byte[] readBytes(int n) throws IOException {
        System.out.println("Reading " + n + " bytes");
        byte[] bytes = new byte[n];
        inputStream.read(bytes);
        return bytes;
    }

    public long readVariableUnsignedInt() throws IOException {
        long value = 0;
        for (int k = 0; ; k += 7) {
            long loopValue = readByte() & 0xFF;
            value |= (loopValue & 0x7F) << k;
            if ((loopValue & 0x80) == 0) {
                return (value & 1) > 0L ? -(value >> 1) : value >> 1;
            }
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public byte readByte() throws IOException {
        return readBytes(1)[0];
    }
}
