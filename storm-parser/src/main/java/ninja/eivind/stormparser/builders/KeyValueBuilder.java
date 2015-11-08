package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.KeyValue;
import ninja.eivind.stormparser.readers.BinaryReader;

import java.io.IOException;

/**
 * @author Eivind Vegsundvåg
 */
public class KeyValueBuilder implements Builder<KeyValue> {
    private BinaryReader reader;

    public KeyValueBuilder(BinaryReader reader) {
        this.reader = reader;
    }

    @Override
    public KeyValue build() throws IOException {
        final byte[] key = reader.readBytes(2);
        final int value = (int) reader.readVariableUnsignedInt();

        return new KeyValue(key, value);
    }
}
