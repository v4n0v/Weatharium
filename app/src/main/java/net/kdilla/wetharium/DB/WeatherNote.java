package net.kdilla.wetharium.DB;

/**
 * Created by avetc on 12.12.2017.
 */

public class WeatherNote {

    private long id;
    private String city;
    private int temperature;
    private int wind;
    private int pressure;
    private int humidity;
    private String time;

    public int getWeatherID() {
        return weatherID;
    }

    private int weatherID;

    public long getDate() {
        return date;
    }

    private long date;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public String toString() {
        return city;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setWeatherID(int weatherID) {
        this.weatherID = weatherID;
    }
}
