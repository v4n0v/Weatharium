package net.kdilla.wetharium.utils.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;

/**
 * Created by avetc on 22.12.2017.
 */

public class WeatherCoordDeserializer implements JsonDeserializer<WeatherCoord> {
    @Override
    public WeatherCoord deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WeatherCoord coord = new WeatherCoord();
        JsonObject jsonObject = json.getAsJsonObject();
        coord.setLat(jsonObject.get("lat").getAsFloat());
        coord.setLon(jsonObject.get("lon").getAsFloat());


        return coord;
    }
}
