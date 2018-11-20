package lexlex.bikephone.models;

import android.arch.lifecycle.ViewModel;

public class Ride extends ViewModel {
    private String id;
    private String date;
    private long duration;

    public Ride(String id, String date, long duration) {
        this.id = id;
        this.date = date;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
