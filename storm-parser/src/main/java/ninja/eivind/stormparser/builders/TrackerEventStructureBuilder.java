package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.TrackerEventStructure;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eivind Vegsundvåg
 */
public class TrackerEventStructureBuilder implements Builder<TrackerEventStructure> {
    private BitReader reader;

    public TrackerEventStructureBuilder(BitReader reader) {
        this.reader = reader;
    }

    @Override
    public TrackerEventStructure build() throws IOException {
        final TrackerEventStructure structure = new TrackerEventStructure();

        final int dataType = reader.readByte();
        structure.setDataType(dataType);
        switch (dataType) {
            case 0x00:
                final long size = reader.readVariableUnsignedInt();
                //System.out.println("Reading array of size " + size);
                final TrackerEventStructure[] array = new TrackerEventStructure[(int) size];
                for (int i = 0; i < array.length; i++) {
                    array[i] = new TrackerEventStructureBuilder(reader).build();
                }
                structure.setArray(array);
                break;
            case 0x01:
                throw new UnsupportedOperationException();
            case 0x02:
                long blobSize = reader.readVariableUnsignedInt();
                byte[] blob = reader.readBytes((int) blobSize);
                structure.setBlob(blob);
                break;
            case 0x04:
                if (reader.readByte() != 0) {
                    TrackerEventStructure optionalData = new TrackerEventStructureBuilder(reader).build();
                    structure.setOptionalData(optionalData);
                }
                break;
            case 0x05:
                Map<Long, TrackerEventStructure> dictionary = new HashMap<>();
                long dictionarySize = reader.readVariableUnsignedInt();
                //System.out.println("Reading dictionary of size " + dictionarySize);
                for (int i = 0; i < dictionarySize; i++) {
                    long key = reader.readVariableUnsignedInt();
                    dictionary.put(key, new TrackerEventStructureBuilder(reader).build());
                }
                structure.setDictionary(dictionary);
                break;
            case 0x06:
                long unsignedByte = reader.readByte();
                structure.setInteger(unsignedByte);
                break;
            case 0x07:
                long unsignedInt = reader.readInt();
                structure.setInteger(unsignedInt);
                break;
            case 0x09:
                long vInt = reader.readVariableUnsignedInt();
                structure.setVarInt(vInt);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported dataType: " + dataType);
        }
        return structure;
    }
}
