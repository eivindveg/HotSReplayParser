package ninja.eivind.stormparser.meta;

import ninja.eivind.mpq.models.MpqException;
import ninja.eivind.mpq.streams.ByteBufferBackedInputStream;
import ninja.eivind.stormparser.builders.TrackerEventStructureBuilder;
import ninja.eivind.stormparser.models.TrackerEventStructure;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Function;

public class MetaInformationBuilder implements Function<ByteBuffer, MetaInformation> {

    @Override
    public MetaInformation apply(ByteBuffer byteBuffer) {
        try (BitReader reader = new BitReader(new ByteBufferBackedInputStream(byteBuffer))) {
            reader.readBytes(3); // 'Magic'
            reader.readByte(); // Format
            reader.readBytes(4); // Data max size
            reader.readBytes(4); // Header offset
            reader.readBytes(4); // User data header size

            TrackerEventStructure headerStructure = new TrackerEventStructureBuilder(reader)
                    .build();

            final String replayVersion = String.format(
                    "%s.%s.%s.%s",
                    headerStructure.getDictionary().get(1L).getDictionary().get(0L).getVarInt(),
                    headerStructure.getDictionary().get(1L).getDictionary().get(1L).getVarInt(),
                    headerStructure.getDictionary().get(1L).getDictionary().get(2L).getVarInt(),
                    headerStructure.getDictionary().get(1L).getDictionary().get(3L).getVarInt()

            );

            int replayBuild = (int) headerStructure.getDictionary().get(1L).getDictionary().get(4L).getVarInt();
            if(replayBuild >= 39951) {
                replayBuild = (int) headerStructure.getDictionary().get(6L).getVarInt();
            }

            final int replayVersionMajor;
            if(replayBuild >= 51978) {
                replayVersionMajor = (int)headerStructure.getDictionary().get(1L).getDictionary().get(1L).getVarInt();
            } else {
                replayVersionMajor = 1;
            }

            final int replayFrames = (int)headerStructure.getDictionary().get(3L).getVarInt();


            return new MetaInformation(replayVersion, replayVersionMajor, replayBuild, replayFrames);
        } catch (IOException e) {
            throw new MpqException("Failed to read metainformation", e);
        }
    }
}
