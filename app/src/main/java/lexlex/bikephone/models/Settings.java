package lexlex.bikephone.models;

import java.io.Serializable;

public class Settings implements Serializable {
    private String mac;
    private String username;
    private int samplefreq;

    public Settings(String mac) {
        this.mac = mac;
        this.username = "username";
        this.samplefreq = 100000; //100ms
    }


    public Settings(String mac, String username, int sample_freq) {
        this.mac = mac;
        this.username = username;
        this.samplefreq = sample_freq;
    }
    public int getSamplefreq() {
        return samplefreq;
    }

    public void setSamplefreq(int samplefreq) {
        this.samplefreq = samplefreq;
    }

    public Settings(String mac, String username) {
        this.mac = mac;
        this.username = username;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
