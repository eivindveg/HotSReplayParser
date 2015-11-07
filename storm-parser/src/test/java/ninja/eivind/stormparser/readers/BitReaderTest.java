package ninja.eivind.stormparser.readers;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Eivind Vegsundvåg
 */
public class BitReaderTest {

    @Test
    public void testReadBit() throws IOException {
        try(BitReader reader = new BitReader(new ByteArrayInputStream(new byte[]{
                (byte) 0b1011_1111}))) {
            for(int i = 0; i < 6; i++) {
                assertTrue("Bit is true at index " + i, reader.readBoolean());
            }
        }
    }
}
