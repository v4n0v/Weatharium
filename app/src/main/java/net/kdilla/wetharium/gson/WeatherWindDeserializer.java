package net.kdilla.wetharium.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by avetc on 08.12.2017.
 */

public class WeatherWindDeserializer implements JsonDeserializer<WeatherWind> {
    @Override
    public WeatherWind deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WeatherWind wind = new WeatherWind();
        JsonObject jsonObject = json.getAsJsonObject();
        wind.setSpeed(jsonObject.get("speed").getAsString());


        return wind;
    }
}
