package sample.structs;

public class Dado {

    private String data, hora, pressMax, pressMin, freqCard;

    public Dado(String data, String hora, String pressMax, String pressMin, String freqCard) {
        this.data = data;
        this.hora = hora;
        this.pressMax = pressMax;
        this.pressMin = pressMin;
        this.freqCard = freqCard;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPressMax() {
        return pressMax;
    }

    public void setPressMax(String pressMax) {
        this.pressMax = pressMax;
    }

    public String getPressMin() {
        return pressMin;
    }

    public void setPressMin(String pressMin) {
        this.pressMin = pressMin;
    }

    public String getFreqCard() {
        return freqCard;
    }

    public void setFreqCard(String freqCard) {
        this.freqCard = freqCard;
    }
}
