package ninja.eivind.stormparser.models;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Eivind Vegsundvåg
 */
public class TrackerEventStructure {
    private int dataType;
    private TrackerEventStructure[] array;
    private Map<Long, TrackerEventStructure> dictionary;
    private TrackerEventStructure optionalData;
    private byte[] blob;
    private long varInt;
    private long integer;

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public void setInteger(long integer) {
        this.integer = integer;
    }

    public Map<Long, TrackerEventStructure> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<Long, TrackerEventStructure> dictionary) {
        this.dictionary = dictionary;
    }

    public TrackerEventStructure getOptionalData() {
        return optionalData;
    }

    public void setOptionalData(TrackerEventStructure optionalData) {
        this.optionalData = optionalData;
    }

    public TrackerEventStructure[] getArray() {
        return array;
    }

    public void setArray(TrackerEventStructure[] array) {
        this.array = array;
    }

    public long getVarInt() {
        return varInt;
    }

    public void setVarInt(long varInt) {
        this.varInt = varInt;
    }

    public String getBlobText() throws UnsupportedEncodingException {
        return blob != null ? new String(blob, "UTF-8") : null;
    }
}
