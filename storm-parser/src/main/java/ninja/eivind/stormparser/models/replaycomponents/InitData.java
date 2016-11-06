package ninja.eivind.stormparser.models.replaycomponents;

/**
 * @author Eivind Vegsundvåg
 */
public class InitData {

    private String[] playerList;
    private long randomValue;
    private GameMode gameMode;

    public void setPlayerList(String[] playerList) {
        this.playerList = playerList;
    }

    public long getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(long randomValue) {
        this.randomValue = randomValue;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
