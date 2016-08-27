package ninja.eivind.stormparser.models.replaycomponents;

import ninja.eivind.stormparser.models.AttributeEventType;

public class AttributeEvent {
    private int header;
    private AttributeEventType type;
    private int playerId;
    private byte[] value;

    public void setHeader(int header) {
        this.header = header;
    }

    public void setType(AttributeEventType type) {
        this.type = type;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public AttributeEventType getType() {
        return type;
    }

    public byte[] getValue() {
        return value;
    }

    public int getPlayerId() {
        return playerId;
    }
}
