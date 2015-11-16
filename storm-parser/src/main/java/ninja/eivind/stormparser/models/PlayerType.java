package ninja.eivind.stormparser.models;

/**
 * Enumeration representing player type
 */
public enum PlayerType {
    COMPUTER("comp"),
    PLAYER("humn");

    private final String type;

    PlayerType(String type) {
        this.type = type;
    }

    /**
     * Returns the type based on String found in replays
     * @param type type String to check against
     * @return The PlayerType corresponding to the parameter
     * @throws UnsupportedOperationException if no type found
     */
    public PlayerType ofType(String type) throws UnsupportedOperationException {
        PlayerType[] values = PlayerType.values();
        for (PlayerType value : values) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        throw new UnsupportedOperationException("No PlayerType for type " + type);
    }
}
