package ninja.eivind.stormparser.models;

/**
 * @author Eivind Vegsundvåg
 */
public class KeyValue {
    private final byte[] key;
    private final int value;

    public KeyValue(byte[] key, int value) {

        this.key = key;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
