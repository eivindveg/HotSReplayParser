package ninja.eivind.stormparser.builders;

import ninja.eivind.stormparser.models.KeyValue;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.readers.BinaryReader;

import java.io.IOException;import java.lang.String;import java.lang.System;

/**
 * @author Eivind Vegsundvåg
 */
public class PlayerBuilder {
    private BinaryReader reader;

    public PlayerBuilder(BinaryReader reader) {
        this.reader = reader;
    }

    public Player build() throws IOException {
        Player player = new Player(null);

        byte[] header = reader.readBytes(2);
        int shortNameLength = new KeyValueBuilder(reader).build().getValue();
        System.out.println("Short name length: " + shortNameLength);
        String shortName = new String(reader.readBytes(shortNameLength), "UTF-8");
        System.out.println(shortName);
        reader.readBytes(3);
        new KeyValueBuilder(reader).build();
        reader.readBytes(6);

        KeyValue subIdKeyValue = new KeyValueBuilder(reader).build();
        int subId = subIdKeyValue.getValue();

        KeyValue bNetIdKeyValue = new KeyValueBuilder(reader).build();
        String bNetId = String.valueOf(bNetIdKeyValue.getValue());

        //player.setbNetId(bNetId);

        KeyValue raceLengthKeyValue = new KeyValueBuilder(reader).build();

        byte[] raceBytes = reader.readBytes(raceLengthKeyValue.getValue());

        reader.readBytes(3);

        for (int i = 0; i < 9; i++) {
            new KeyValueBuilder(reader).build();
        }

        return player;
    }
}
