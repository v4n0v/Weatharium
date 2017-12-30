package net.kdilla.wetharium.utils.gson;

public class WeatherMain {

    public String getHumidity() {
        return humidity;
    }

    private String humidity;

    public String getTemp() {
        return temp;
    }

    private String temp;
    private String tempMin;
    private String tempMax;
    private String pressure;

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }


}
