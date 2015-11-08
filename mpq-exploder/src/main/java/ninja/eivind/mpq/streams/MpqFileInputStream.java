package ninja.eivind.mpq.streams;

import ninja.eivind.mpq.MpqFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqFileInputStream extends FilterInputStream {

    public MpqFileInputStream(InputStream in, MpqFile entry) throws IOException {
        this(in, entry, entry.getCompressedSize(), entry.getFileSize());
    }

    public MpqFileInputStream(InputStream in, MpqFile entry, long inSize,
                              long outSize) throws IOException {
        super(init(in, entry, inSize, outSize));
    }

    private static InputStream init(InputStream in, MpqFile entry, long inSize,
                                    long outSize) throws IOException {
        if (entry.isEncrypted() && inSize > 3) {
            return initDecryptorInputStream(in);
        }
        if (entry.isCompressed() && inSize != outSize) {
            return initInflaterInputStream(in);
        }

        return in;
    }

    private static InputStream initDecryptorInputStream(InputStream in) {
        // TODO
        return in;
    }

    private static InputStream initInflaterInputStream(InputStream in)
            throws IOException {
        InputStream value = in;
        int formats = value.read();

        if (formats == -1) {
            throw new EOFException();
        }

        if ((0x10 & formats) != 0) {
            value = new BZip2CompressorInputStream(value, true);
            formats &= 0xEF;
        }

        if ((0x08 & formats) != 0) {
            value = new InflaterInputStream(value);
            formats &= 0xF7;
        }

        if ((0x02 & formats) != 0) {
            value = new InflaterInputStream(value);
            formats &= 0xFD;
        }

        if ((0x01 & formats) == 0x01) {
            value = new InflaterInputStream(value);
            formats &= 0xFE;
        }

        if (formats != 0) {
            throw new UnsupportedOperationException("Unhandled Compression formats: 0x" + Integer.toHexString(formats));
        }

        return value;
    }

}
