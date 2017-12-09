package net.kdilla.wetharium.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Created by avetc on 08.12.2017.
 */

public class WeatherGsonDeserializer implements JsonDeserializer<WeatherGSon> {
    @Override
    public WeatherGSon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        WeatherGSon weather = new WeatherGSon();

        weather.setCity(jsonObject.get("name").getAsString().toUpperCase(Locale.US));

        WeatherMain main = (WeatherMain) context.deserialize(jsonObject.get("main"), WeatherMain.class);
        weather.setHumidity(main.humidity);
        weather.setTemperature(main.temp);
        weather.setPressure(main.pressure);
        WeatherWind wind = (WeatherWind) context.deserialize(jsonObject.get("wind"), WeatherWind.class);
        weather.setWind(wind.speed);

        JsonArray details = jsonObject.getAsJsonArray("weather");
        JsonObject detailsObject = (JsonObject)details.get(0);
        weather.setId(detailsObject.get("id").getAsInt());
        weather.setDescription(detailsObject.get("description").getAsString().toUpperCase());
        return weather;
    }
}
