package lexlex.bikephone.models;

public class Settings {
    private String mac;
    private String username;
    //todo - frequencia de amostragem - colocar na base de dados tb

    public Settings(String mac) {
        this.mac = mac;
        this.username = "username";
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
