package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
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

                bitReader.readBlobPrecededWithLength(7);// More unknowns
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);
                bitReader.readBlobPrecededWithLength(7);

            }
            initData.setPlayerList(playerList);

            initData.setRandomValue(bitReader.readInt());

        }

        return initData;
    }
}
