package lexlex.bikephone.models;

import android.arch.lifecycle.ViewModel;

import java.io.Serializable;

public class Ride extends ViewModel implements Serializable {
    private String id;
    private String mac;
    private String date;
    private int duration;
    private int distance;
    private String position;
    private int sample_freq;

    public Ride(String id, String mac, String date, int duration, int distance,String position, int sample_freq) {
        this.id = id;
        this.mac = mac;
        this.date = date;
        this.duration = duration;
        this.distance = distance;
        this.position = position;
        this.sample_freq = sample_freq;
    }

    public String getMac() {
        return mac;
    }

    //TODO - Adicionar posição aos campos mostrados ao utilizador: no xml e na activitie.
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
