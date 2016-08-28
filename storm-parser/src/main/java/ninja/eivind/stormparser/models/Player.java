package ninja.eivind.stormparser.models;

/**
 * @author Eivind Vegsundvåg
 */
public class Player {
    private String bNetId;
    private String shortName;
    private PlayerType playerType;
    private Integer battleNetRegionId;
    private int battleTag;

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

    public Integer getBattleNetRegionId() {
        return battleNetRegionId;
    }

    public void setBattleNetRegionId(Integer battleNetRegionId) {
        this.battleNetRegionId = battleNetRegionId;
    }

    public void setBattleTag(int battleTag) {
        this.battleTag = battleTag;
    }

    public int getBattleTag() {
        return battleTag;
    }
}
