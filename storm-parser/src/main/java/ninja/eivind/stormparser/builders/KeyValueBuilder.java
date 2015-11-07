package ninja.eivind.stormparser.builders;

import ninja.eivind.stormparser.models.KeyValue;
import ninja.eivind.stormparser.readers.BinaryReader;

import java.io.IOException;

/**
 * @author Eivind Vegsundvåg
 */
public class KeyValueBuilder {
    private BinaryReader reader;

    public KeyValueBuilder(BinaryReader reader) {
        this.reader = reader;
    }

    public KeyValue build() throws IOException {
        byte[] key = reader.readBytes(2);
        int value = (int) reader.readVariableUnsignedInt();

        return new KeyValue(key, value);
    }
}
