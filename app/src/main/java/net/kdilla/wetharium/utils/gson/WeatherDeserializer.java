package net.kdilla.wetharium.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class  WeatherDeserializer implements JsonDeserializer<Weather> {
    @Override
    public Weather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Weather weather = new Weather();

        weather.setCity(jsonObject.get("name").getAsString());

        WeatherMain main = (WeatherMain) context.deserialize(jsonObject.get("main"), WeatherMain.class);
        weather.setHumidity(Integer.parseInt(main.getHumidity()));
        weather.setTemperature(Math.round(Float.parseFloat(main.getTemp())));
        String aaa = main.getPressure();
        int a = Math.round(Float.parseFloat(main.getPressure()));

        weather.setPressure(a);
        WeatherWind wind = (WeatherWind) context.deserialize(jsonObject.get("wind"), WeatherWind.class);
        weather.setWind(Math.round(Float.parseFloat(wind.speed)));

        weather.setTempMax(Math.round(Float.parseFloat(main.getTempMax())));
        weather.setTempMin(Math.round(Float.parseFloat(main.getTempMin())));

        JsonArray details = jsonObject.getAsJsonArray("weather");
        JsonObject detailsObject = (JsonObject)details.get(0);
        weather.setId(detailsObject.get("id").getAsInt());
        weather.setDescription(detailsObject.get("description").getAsString());
        weather.setMainInfo(detailsObject.get("main").getAsString());

        WeatherCoord weatherCoord = (WeatherCoord) context.deserialize(jsonObject.get("coord"), WeatherCoord.class);
        weather.setLat(weatherCoord.getLat());
        weather.setLon(weatherCoord.getLon());

        return weather;
    }
}
