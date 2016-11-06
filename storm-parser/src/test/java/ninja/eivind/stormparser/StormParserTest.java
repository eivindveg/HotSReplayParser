package ninja.eivind.stormparser;

import ninja.eivind.mpq.MpqArchive;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.PlayerType;
import ninja.eivind.stormparser.models.Replay;
import ninja.eivind.stormparser.models.replaycomponents.GameMode;
import ninja.eivind.stormparser.models.replaycomponents.InitData;
import ninja.eivind.stormparser.models.replaycomponents.ReplayDetails;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Eivind Vegsundvåg
 */
public class StormParserTest {

    private StormParser parser;
    private String fileName;

    @Before
    public void setUp() {
        URL resource = ClassLoader.getSystemClassLoader().getResource("test.StormReplay");
        assertNotNull("Could not load test resource", resource);
        fileName = resource.getFile();
        parser = new StormParser();
    }

    @Test
    public void testGetRandomSeed() throws IOException {
        Replay replay = parser.apply(new File(fileName));

        InitData initData = replay.getInitData();

        final long expected = 2906602328L;
        final long actual = initData.getRandomValue();

        assertEquals("Parser returns correct random value as long", expected, actual);
    }

    @Test
    public void testRushTeaPlayedInTestReplay() throws IOException {
        Replay replay = parser.apply(new File(fileName));

        ReplayDetails replayDetails = replay.getReplayDetails();

        List<Player> players = replayDetails.getPlayers();
        Optional<Player> rushTea = players.stream().filter(player -> player.getShortName().equals("RushTea")).findFirst();

        assertTrue("RushTea took part in the test replay.", rushTea.isPresent());
    }

    @Test
    public void testBrawlsCanBeDetected() throws Exception {
        Replay replay = parser.apply(new File(getClass().getResource("lost_caverns_brawl.StormReplay").getFile()));

        InitData initData = replay.getInitData();
        GameMode gameMode = initData.getGameMode();

        assertSame(GameMode.BRAWL, gameMode);
    }

    @Test
    public void testRushTeaIsAHumanPlayer() {
        Replay replay = parser.apply(new File(fileName));

        ReplayDetails replayDetails = replay.getReplayDetails();

        List<Player> players = replayDetails.getPlayers();
        Player rushTea = players.stream()
                .filter(player -> player.getShortName().equals("RushTea"))
                .findFirst()
                .orElseThrow(() -> new MpqException("Could not find RushTea", null));

        PlayerType expected = PlayerType.PLAYER;
        PlayerType actual = rushTea.getPlayerType();

        assertSame("RushTea is a human player", expected, actual);
    }
}
