package ninja.eivind.stormparser.readers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Eivind Vegsundvåg
 */
public class BitReaderTest {

    private BitReader reader;

    @Before
    public void setUp() {
        reader = new BitReader(new ByteArrayInputStream(new byte[]{
                (byte) 0b1011_1111, (byte) 0b1111_1111
        }));
    }

    @Test
    public void testReadBit() throws IOException {
        for (int i = 0; i < 6; i++) {
            assertTrue("Bit is true at index " + i, reader.readBoolean());
        }
        assertFalse("Bit is false at index 6", reader.readBoolean());
        assertTrue("Bit is true at index 7", reader.readBoolean());
    }

    @Test
    public void testReadBitIsAccurateOverMultipleBytes() throws IOException {
        for (int i = 0; i < 6; i++) {
            assertTrue("Bit is true at index " + i, reader.readBoolean());
        }
        assertFalse("Bit is false at index 6", reader.readBoolean());
        assertTrue("Bit is true at index 7", reader.readBoolean());

        for (int i = 8; i < 16; i++) {
            assertTrue("Bit is true at index " + i, reader.readBoolean());
        }
    }

    @Test
    public void testReadByte() throws Exception {
        byte expected = (byte) 0b1011_1111;
        byte actual = reader.readByte();

        assertEquals("First byte maps to -65", expected, actual);
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }


}
