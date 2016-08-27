package ninja.eivind.stormparser;

import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.Replay;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Eivind Vegsundvåg
 */
public class StandaloneBattleLobbyParserTest {
    private StandaloneBattleLobbyParser parser;
    private File file;

    @Before
    public void setUp() throws Exception {
        parser = new StandaloneBattleLobbyParser();
        file = new File(getClass().getResource("replay.server.battlelobby").toURI());
    }

    @Test
    public void testRushTeaPlayedInMatch() {
        Replay replay = parser.apply(file);

        List<Player> players = replay.getReplayDetails().getPlayers();

        players.stream()
                .filter(player -> player.getShortName().equals("RushTea"))
                .findAny()
                .orElseThrow(() -> new AssertionError("Could not find player RushTea"));
    }

    @Test
    public void testRegionIsEU() {
        Replay replay = parser.apply(file);

        List<Player> players = replay.getReplayDetails().getPlayers();
        assertFalse(players.isEmpty());

        for (Player player : players) {
            final int battleNetRegionId = player.getBattleNetRegionId();

            assertEquals(2, battleNetRegionId);
        }
    }
}
