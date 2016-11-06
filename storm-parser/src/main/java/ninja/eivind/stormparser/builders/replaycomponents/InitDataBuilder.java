package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.replaycomponents.GameMode;
import ninja.eivind.stormparser.models.replaycomponents.InitData;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Eivind Vegsundvåg
 */
public class InitDataBuilder implements Builder<InitData> {

    private final ByteBuffer buffer;

    public InitDataBuilder(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public InitData build() throws IOException {
        InitData initData = new InitData();

        try (BitReader bitReader = new BitReader(new ByteArrayInputStream(buffer.array()))) {
            int i = bitReader.readByte();
            //System.out.println(i);

            String[] playerList = new String[i];
            for (int j = 0; j < i; j++) {
                playerList[j] = new String(bitReader.readBlobPrecededWithLength(8));
                //System.out.println(playerList[j]);

                if (bitReader.readBoolean()) {
                    // clan tag
                    byte[] bytes = bitReader.readBlobPrecededWithLength(8);
                    String s = new String(bytes, "UTF-8");
                    //System.out.println("Clan tag: " + s);
                }

                if (bitReader.readBoolean()) {
                    // clan logo
                    //System.out.println("Reading clan logo... This isn't good.");
                    bitReader.readBlobPrecededWithLength(40);
                }

                if (bitReader.readBoolean()) {
                    // Highest league ranking
                    long l = bitReader.read(8);
                    //System.out.println("Highest league ranking: " + l);
                }


                if (bitReader.readBoolean()) {
                    // combined race levels
                    long i1 = bitReader.read(32);
                    //System.out.println("Combined race levels: " + i1);
                }

                long randomValue = bitReader.readInt();


                if (bitReader.readBoolean()) {
                    // Race preferences
                    long l = bitReader.read(8);
                    //System.out.println("Race preferences: " + l);
                }

                if (bitReader.readBoolean()) {
                    // Team preference
                    long l = bitReader.read(8);
                    //System.out.println("Team preference: " + l);
                }

                bitReader.readBoolean(); // test map
                bitReader.readBoolean(); // test auto
                bitReader.readBoolean(); // examine
                bitReader.readBoolean(); // custom interface

                bitReader.readInt(); // unknown getValue

                bitReader.read(2); // observer

                bitReader.readBlobPrecededWithLength(9); // m_hero, currently empty string
                bitReader.readBlobPrecededWithLength(9); // m_skin, currently empty string
                bitReader.readBlobPrecededWithLength(9); // m_mount, currently empty string
                bitReader.readBlobPrecededWithLength(7); // m_toonHandle, currently empty string

            }
            // random value, using as seed
            initData.setRandomValue(bitReader.readInt());

            bitReader.readBlobPrecededWithLength(10); // m_gameCacheName - "Dflt"

            bitReader.readBoolean(); // Lock Teams
            bitReader.readBoolean(); // Teams Together
            bitReader.readBoolean(); // Advanced Shared Control
            bitReader.readBoolean(); // Random Races
            bitReader.readBoolean(); // BattleNet
            bitReader.readBoolean(); // AMM
            bitReader.readBoolean(); // Competitive
            bitReader.readBoolean(); // m_practice
            bitReader.readBoolean(); // m_cooperative
            bitReader.readBoolean(); // m_noVictoryOrDefeat
            bitReader.readBoolean(); // m_heroDuplicatesAllowed
            bitReader.read(2); // Fog
            bitReader.read(2); // Observers
            bitReader.read(2); // User Difficulty
            bitReader.readInt(); // 64 bit int part 1: Client Debug Flags
            bitReader.readInt(); // 64 bit int part 2: Client Debug Flags

            //if (gameBuild >= 43905 && bitReader.readBoolean()) {
            if(bitReader.readBoolean()) {
                long gameModeValue = bitReader.readInt();
                final GameMode gameMode;
                gameMode = getGameMode((int) gameModeValue);

                initData.setGameMode(gameMode);
            }

            initData.setPlayerList(playerList);

        }

        return initData;
    }

    private GameMode getGameMode(int gameModeValue) {

        switch (gameModeValue) {
            case 50021: // Versus AI
            case 50041: // Practice
                return GameMode.VERSUS_AI;
            case 50001:
                return GameMode.QUICK_MATCH;
            case 50031:
                return GameMode.BRAWL;
            case 50051:
                return GameMode.UNRACKED_DRAFT;
            case 50061:
                return GameMode.HERO_LEAGUE;
            case 50071:
                return GameMode.TEAM_LEAGUE;
            default:
                return GameMode.UNKNOWN;
        }
    }
}
