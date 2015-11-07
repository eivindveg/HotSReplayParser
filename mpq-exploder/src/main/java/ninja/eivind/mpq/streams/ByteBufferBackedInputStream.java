package ninja.eivind.mpq.streams;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Eivind Vegsundvåg
 */
public class ByteBufferBackedInputStream extends InputStream {

    private final ByteBuffer buffer;

    public ByteBufferBackedInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (!buffer.hasRemaining()) {
            return -1;
        }
        return buffer.get() & 0xFF;
    }

    @Override
    public int read(@Nonnull byte[] bytes, int off, int len)
            throws IOException {
        if (!buffer.hasRemaining()) {
            return -1;
        }

        int length = Math.min(len, buffer.remaining());
        buffer.get(bytes, off, length);
        return length;
    }
}
