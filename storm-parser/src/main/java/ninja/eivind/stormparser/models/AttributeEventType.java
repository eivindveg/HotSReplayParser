package ninja.eivind.stormparser.models;

import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

/**
 * @author Eivind Vegsundvåg
 */
public enum AttributeEventType {
    PLAYER_TYPE(500),
    TEAM_SIZE(2001),
    PLAYER_TEAM_1V1(2002),
    PLAYER_TEAM_2V2(2002),
    PLAYER_TEAM_3V3(2002),
    PLAYER_TEAM_4V4(2002),
    PLAYER_TEAM_FFA(2002),

    UNKNOWN(3007);


    private int type;

    AttributeEventType(int type) {
        this.type = type;
    }

    public static AttributeEventType ofType(int type) {
        AttributeEventType[] values = AttributeEventType.values();
        for (AttributeEventType value : values) {
            if (value.type == type) {
                return value;
            }
        }
        throw new UnsupportedOperationException("No PlayerType for type " + type);

    }
}

