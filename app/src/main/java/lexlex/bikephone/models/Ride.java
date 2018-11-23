package lexlex.bikephone.models;

import android.arch.lifecycle.ViewModel;

import java.io.Serializable;

public class Ride extends ViewModel implements Serializable {
    private String id;
    private String mac;
    private String date;
    private int duration;
    private int distance;
    private int sample_freq;

/*
    public Ride(String id, String date, long duration) {
        this.id = id;
        this.date = date;
        this.duration = duration;
    }
*/
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getSample_freq() {
        return sample_freq;
    }

    public void setSample_freq(int sample_freq) {
        this.sample_freq = sample_freq;
    }

    public Ride(String id, String mac, String date, int duration, int distance, int sample_freq) {
        this.id = id;
        this.mac = mac;
        this.date = date;
        this.duration = duration;
        this.distance = distance;
        this.sample_freq = sample_freq;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
