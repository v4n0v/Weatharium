package net.kdilla.wetharium.utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by avetc on 19.12.2017.
 */

public class ImagesDeserializer implements JsonDeserializer<Image> {
    @Override
    public Image deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Image image = new Image();
        JsonArray details = jsonObject.getAsJsonArray("items");
        JsonObject detailsObject =  (JsonObject) details.get(0);
        image.setTitle(detailsObject.get("title").getAsString());
        image.setLink(detailsObject.get("link").getAsString());

        return image;
    }
}