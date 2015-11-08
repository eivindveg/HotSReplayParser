package ninja.eivind.stormparser;

import ninja.eivind.mpq.MpqArchive;
import ninja.eivind.stormparser.builders.PlayerBuilder;
import ninja.eivind.stormparser.builders.ReplayBuilder;
import ninja.eivind.stormparser.builders.TrackerEventStructureBuilder;
import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.Replay;
import ninja.eivind.stormparser.models.TrackerEventStructure;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Eivind Vegsundvåg
 */
public class StormParser {
    public static final String REPLAY_DETAILS = "replay.details";
    public static final String REPLAY_INIT_DATA = "replay.initData";
    private File file;

    public StormParser(File file) {
        this.file = file;
    }

    public Replay parseReplay() {
        MpqArchive archive = new MpqArchive(file);

        try {
            Map<String, ByteBuffer> files = archive.getFiles(REPLAY_DETAILS, REPLAY_INIT_DATA);

            return new ReplayBuilder(files).build();
        } catch (IOException e) {
            throw new MpqException("Could not parse file", e);
        }
    }

    @Deprecated
    public String getMatchId() {
        MpqArchive archive = new MpqArchive(file);
        try {
            Map<String, ByteBuffer> files = archive.getFiles(REPLAY_DETAILS, REPLAY_INIT_DATA);

            String seed = String.valueOf(getRandomSeed(files));
            List<String> battleNetIds = getBattleNetIds(files);

            String concat = concatenate(battleNetIds, seed);
            byte[] hashed = MessageDigest.getInstance("MD5").digest(concat.getBytes());
            byte[] reArranged = reArrangeForUUID(hashed);
            return getUUID(reArranged);
        } catch (IOException e) {
            throw new MpqException("Could not parse file", e);
        } catch (NoSuchAlgorithmException e) {
            throw new MpqException("Could not calculate match id from parsed replay", e);
        }
    }

    private byte[] reArrangeForUUID(byte[] hashed) {
        return new byte[]{
                hashed[3],
                hashed[2],
                hashed[1],
                hashed[0],

                hashed[5],
                hashed[4],
                hashed[7],
                hashed[6],
                hashed[8],
                hashed[9],
                hashed[10],
                hashed[11],
                hashed[12],
                hashed[13],
                hashed[14],
                hashed[15],
        };
    }

    private String getUUID(byte[] bytes) {
        long msb = 0;
        long lsb = 0;
        assert bytes.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        }

        return new UUID(msb, lsb).toString();
    }

    protected String concatenate(List<String> battleNetIds, String seed) {
        StringBuilder concatBuilder = new StringBuilder();
        battleNetIds.stream().forEach(concatBuilder::append);
        concatBuilder.append(seed);

        return concatBuilder.toString();
    }

    @Deprecated
    private List<String> getBattleNetIds(Map<String, ByteBuffer> files) throws IOException {
        ByteBuffer byteBuffer = files.get(REPLAY_DETAILS);
        try (BitReader reader = new BitReader(new ByteArrayInputStream(byteBuffer.array()))) {

            TrackerEventStructure structure = new TrackerEventStructureBuilder(reader).build();

            TrackerEventStructure[] array = structure.getDictionary().get(0L).getOptionalData().getArray();

            List<Player> players = Arrays.stream(array)
                    .map(PlayerBuilder::new)
                    .map(PlayerBuilder::build)
                    .collect(Collectors.toList());

            return players.stream()
                    .map(Player::getBNetId)
                    .map(Long::parseLong)
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
    }

    @Deprecated
    protected String getRandomSeed(Map<String, ByteBuffer> files) throws IOException {
        ByteBuffer byteBuffer = files.get(REPLAY_INIT_DATA);
        String[] playerList;
        try (BitReader bitReader = new BitReader(new ByteArrayInputStream(byteBuffer.array()))) {
            int i = bitReader.readByte();
            //System.out.println(i);

            playerList = new String[i];
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

                bitReader.readBlobPrecededWithLength(7);// More unknowns
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);

            }

            return String.valueOf(bitReader.readInt());
        } catch (IOException | UnsupportedOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
