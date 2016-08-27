package ninja.eivind.stormparser.models;

/**
 * @author Eivind Vegsundvåg
 */
public class Player {
    private String bNetId;
    private String shortName;
    private PlayerType playerType;

    public String getBNetId() {
        return bNetId;
    }

    public void setBNetId(String bNetId) {
        this.bNetId = bNetId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }
}
