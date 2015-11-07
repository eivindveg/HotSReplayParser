package ninja.eivind.mpq;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqFileMapper {
    private MpqFile mpqFile;

    public MpqFileMapper(MpqFile mpqFile) {
        this.mpqFile = mpqFile;
    }

    public ByteBuffer map(InputStream inputStream) throws IOException {
        int read;
        List<Byte> bytes = new ArrayList<>();
        while ((read = inputStream.read()) != -1) {
            bytes.add((byte) (read & 0xFF));
        }
        byte[] bytes1 = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bytes1[i] = bytes.get(i);
        }
        return ByteBuffer.wrap(bytes1);
    }
}
