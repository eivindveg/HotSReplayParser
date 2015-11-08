package ninja.eivind.stormparser.models.replaycomponents;

/**
 * @author Eivind Vegsundvåg
 */
public class InitData {

    private String[] playerList;
    private long randomValue;

    public void setPlayerList(String[] playerList) {
        this.playerList = playerList;
    }

    public void setRandomValue(long randomValue) {
        this.randomValue = randomValue;
    }
}
