package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.meta.MetaInformation;
import ninja.eivind.stormparser.models.replaycomponents.GameMode;
import ninja.eivind.stormparser.models.replaycomponents.InitData;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.BiFunction;

/**
 * @author Eivind Vegsundvåg
 */
public class InitDataBuilder implements BiFunction<MetaInformation, ByteBuffer, InitData> {

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

    @Override
    public InitData apply(MetaInformation metaInformation, ByteBuffer buffer) {
        InitData initData = new InitData();

        try (BitReader bitReader = new BitReader(new ByteArrayInputStream(buffer.array()))) {
            int i = (int) bitReader.read(5);
            //System.out.println(i);

            String[] playerList = new String[i];
            for (int j = 0; j < i; j++) {
                String playerName = new String(bitReader.readBlobPrecededWithLength(8));

                if(!playerName.isEmpty()) {
                    playerList[j] = playerName;
                }
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

                bitReader.readInt(); // m_testType

                bitReader.read(2); // observer

                bitReader.readBlobPrecededWithLength(9); // m_hero, currently empty string
                bitReader.readBlobPrecededWithLength(9); // m_skin, currently empty string
                bitReader.readBlobPrecededWithLength(9); // m_mount, currently empty string
                if(metaInformation.getMajorVersion() >= 2) {
                    bitReader.readBlobPrecededWithLength(9);
                    bitReader.readBlobPrecededWithLength(9);
                }
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

            // m_ammId
            if (metaInformation.getBuild() >= 43905 && bitReader.readBoolean()) {
                long gameModeValue = bitReader.readInt();
                final GameMode gameMode;
                gameMode = getGameMode((int) gameModeValue);

                initData.setGameMode(gameMode);
            }

            bitReader.read(3); // game speed

            bitReader.read(3); // "game type"

            int maxPlayers = (int) bitReader.read(5);
            if(maxPlayers != 10) {
                initData.setGameMode(GameMode.TRY_ME);
            }

            bitReader.read(5); // Max observers
            bitReader.read(5); // Max players
            bitReader.read(4); // +1 = Max Teams
            bitReader.read(6); // Max colors
            bitReader.read(8); // +1 = Max Races
            bitReader.read(8); // Max controlers



            initData.setPlayerList(playerList);

            return initData;
        } catch (IOException e) {
            throw new MpqException("Failed to parse initdata", e);
        }

    }
}
