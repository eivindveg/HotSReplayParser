package ninja.eivind.stormparser.models;

import ninja.eivind.stormparser.models.replaycomponents.AttributeEvent;

/**
 * @author Eivind Vegsundvåg
 */
public enum AttributeEventType {
    PLAYER_TYPE(500),
    TEAM_SIZE(2001),
    PLAYER_TEAM_1V1(2002),
    PLAYER_TEAM_2V2(2003),
    PLAYER_TEAM_3V3(2004),
    PLAYER_TEAM_4V4(2005),
    PLAYER_TEAM_FFA(2006),

    TEAM_SIZE_UNKNOWN_8(2008),
    TEAM_SIZE_UNKNOWN_12(2012),
    TEAM_SIZE_UNKNOWN_16(2016),
    TEAM_SIZE_UNKNOWN_19(2019),
    TEAM_SIZE_UNKNOWN_20(2020),
    TEAM_SIZE_UNKNOWN_24(2024),

    TEAM_COLOR_INDEX_ATTRIBUTE(3002),
    PLAYER_LOGO(3011),
    TANDEM_LEADER(3012),

    PARTICIPANT_ROLE(3007),

    SKIN_AND_SKIN_TINT(4003),
    HERO_ROLE(4007),
    READY_ORDER(4011),
    READYING_TEAM(4012),
    HERO_VISIBILITY(4014),
    READYING_COUNT(4016),

    ARTIFACT_UNKNOWN_1(4101);


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

