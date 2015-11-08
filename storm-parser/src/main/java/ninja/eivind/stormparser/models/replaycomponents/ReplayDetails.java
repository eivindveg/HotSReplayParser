package ninja.eivind.stormparser.models.replaycomponents;

import ninja.eivind.stormparser.models.Player;

import java.util.List;

/**
 * @author Eivind Vegsundvåg
 */
public class ReplayDetails {
    private List<Player> players;

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
