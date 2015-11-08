package ninja.eivind.stormparser.builders;

import ninja.eivind.mpq.builders.Builder;
import ninja.eivind.stormparser.models.Player;
import ninja.eivind.stormparser.models.TrackerEventStructure;

import java.lang.String;

/**
 * @author Eivind Vegsundvåg
 */
public class PlayerBuilder implements Builder<Player> {

    private final TrackerEventStructure structure;

    public PlayerBuilder(TrackerEventStructure structure) {
        this.structure = structure;
    }

    @Override
    public Player build() {
        final Player player = new Player();
        final String bNetId = String.valueOf(structure.getDictionary().get(1L).getDictionary().get(4L).getVarInt());

        player.setBNetId(bNetId);

        return player;
    }
}
