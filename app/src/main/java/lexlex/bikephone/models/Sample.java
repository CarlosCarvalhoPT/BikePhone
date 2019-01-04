package lexlex.bikephone.models;

import java.io.Serializable;

public class Sample {
    private String corridaID;
    private String typeID;
    private long timestamp; //long time= System.currentTimeMillis();
    private float value;

    public String getCorridaID() {
        return corridaID;
    }

    public void setCorridaID(String corridaID) {
        this.corridaID = corridaID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Sample(String corridaID, String typeID, long timestamp, float value) {
        this.corridaID = corridaID;
        this.typeID = typeID;
        this.timestamp = timestamp;
        this.value = value;
    }
}
