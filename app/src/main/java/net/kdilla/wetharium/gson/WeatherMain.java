package net.kdilla.wetharium.gson;

public class WeatherMain {



    public void setTemp(
            String temp) {
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

    String humidity;
    String temp;

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

    String tempMin;
    String tempMax;
    String pressure;
}
