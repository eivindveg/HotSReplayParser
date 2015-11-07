package ninja.eivind.stormparser.models;

/**
 * @author Eivind Vegsundvåg
 */
public class Player {
    private String bNetId;

    public Player(TrackerEventStructure item) {
        this.bNetId = String.valueOf(item.getDictionary().get(1L).getDictionary().get(4L).getVarInt());
    }

    public void setBNetId(String bNetId) {
        this.bNetId = bNetId;
    }

    public String getBNetId() {
        return bNetId;
    }
}
