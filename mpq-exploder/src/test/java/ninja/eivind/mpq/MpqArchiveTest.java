package ninja.eivind.mpq;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * @author Eivind Vegsundvåg
 */
public class MpqArchiveTest {

    private MpqArchive archive;
    private String fileName;

    @Before
    public void setUp() {
        URL resource = ClassLoader.getSystemClassLoader().getResource("test.StormReplay");
        assertNotNull("Could not load test resource", resource);
        fileName = resource.getFile();
        archive = new MpqArchive(fileName);
    }

    @Test
    public void testGetHeader() throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(fileName);
            FileChannel channel = fileInputStream.getChannel()) {
            archive.buildHeader(channel);
        }
    }

    @Test
    public void testGetFiles() throws IOException {
        Map<String, ByteBuffer> file = archive.getFiles("replay.initData");

        ByteBuffer actual = file.get("replay.initData");
        assertNotNull("We got what we were looking for", actual);
    }
}
