package net.kdilla.wetharium.utils.gson;


public class Weather {

    private int humidity;
    private String city;
    private int temperature;
    private  int wind;
    private int pressure;
    private  int tempMin;
    private  int tempMax;
    private String description;
    private  String mainInfo;
    private int id;

    public void setCity(String city) {
        this.city = city;
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



    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getCity() {
        return city;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getWind() {
        return wind;
    }

    public int getId() {
        return id;
    }


    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }


    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }



    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setId(int id) {
        this.id = id;
    }


    public Weather(){}
    public Weather(String city, int temperature, int wind,
                   int pressure, int humidity ) {
        this.city = city;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}
