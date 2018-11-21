package lexlex.bikephone.models;

public class Sensor {
    private String id;
    private String description;
    private String unit;


    public Sensor(String id, String description, String unit) {
        this.id = id;
        this.description = description;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
