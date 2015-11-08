package ninja.eivind.stormparser.builders.replaycomponents;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.builders.PlayerListBuilder;
import ninja.eivind.stormparser.builders.TrackerEventStructureBuilder;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.TrackerEventStructure;
import ninja.eivind.stormparser.models.replaycomponents.ReplayDetails;
import ninja.eivind.stormparser.readers.BitReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Eivind Vegsundvåg
 */
public class ReplayDetailsBuilder implements Builder<ReplayDetails> {
    private final ByteBuffer byteBuffer;

    public ReplayDetailsBuilder(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }


    @Override
    public ReplayDetails build() throws IOException {
        ReplayDetails replayDetails = new ReplayDetails();
        try (BitReader reader = new BitReader(new ByteArrayInputStream(byteBuffer.array()))) {

            TrackerEventStructure structure = new TrackerEventStructureBuilder(reader).build();

            List<Player> players = new PlayerListBuilder(structure).build();

            replayDetails.setPlayers(players);
        }

        return replayDetails;
    }
}
