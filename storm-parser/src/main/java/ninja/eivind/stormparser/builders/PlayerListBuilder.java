package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.TrackerEventStructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eivind Vegsundvåg
 */
public class PlayerListBuilder implements Builder<List<Player>> {
    private final TrackerEventStructure structure;

    public PlayerListBuilder(TrackerEventStructure structure) {
        this.structure = structure;
    }

    @Override
    public List<Player> build() throws IOException {
        TrackerEventStructure[] playerArray = structure.getDictionary().get(0).getOptionalData().getArray();

        return Arrays.stream(playerArray)
                .map(PlayerBuilder::new)
                .map(PlayerBuilder::build)
                .collect(Collectors.toList());
    }
}
