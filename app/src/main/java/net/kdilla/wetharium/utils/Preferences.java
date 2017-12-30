package net.kdilla.wetharium.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.MainActivity;
import net.kdilla.wetharium.R;

import java.util.List;

/**
 * Created by avetc on 10.12.2017.
 */

public class Preferences {
    public static final String APP_PREFERENCES = "shared prefs";
    public static final String SAVED_LAST_UPD= "saved last upd";
    public static final String SOURCE = "source";
    public static final String SPLASH = "splash";
    public static final String WIDGET = "widget";
    public static final String DB_LIST = "db licst";
    public static final String SAVED_WIND = "saved_wind";
    public static final String SAVED_PRESSURE = "saved_pressure";
    public static final String SAVED_HUMIDITY = "saved_storm";
    public static final String ADD_IS_OK = "add is ok";
    public static final String SAVED_WEATHER = "saved_weather";
    public static final String ADD_CITY = "add city";
    public static final String ADD_TEMP = "add temp";
    public static final String ADD_TEMP_MAX ="add temp max";
    public static final String ADD_TEMP_MIN ="add temp min";
    public static final String ADD_WIND = "add wind";
    public static final String ADD_PRESSURE = "add pressure";
    public static final String ADD_HUMIDITY = "add humidity";
    public static final String ADD_IMAGE_ID = "add image id";
    public static final String ADD_IS_WIND = "add is wind";
    public static final String ADD_IS_PRESSURE = "add is pressure";
    public static final String ADD_IS_HUMIDITY = "add is humidity";
    public static final String ADD_DESCRIPTION = "add description";
    public static final String ADD_IS_BIND = "add is bind";
    public static final String ADD_ADDITION= "add addition";

    public final static String ADD_ICON = "add icon";
    public final static String BROADCAST_ACTION = "net.kdilla.wetharium.p0961servicebackbroadcast";
    public final static String TAG_1 = "FRAGMENT_1";

    public static final int REQUEST_CODE_WEATHER = 1;
    public static final int REQUEST_CODE_IMAGE = 2;

    public static final String SAVED_CITY = "saved_city";
    public static final String EXTRA_CITY_NOM = "cityNom";

    public static final String TEXT_FONT_MAIN = "Oswald-Light.ttf";
    public static final String APP_PREFS_NAME = MainActivity.class.getPackage().getName();
    public static final String APP_CACHE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + APP_PREFS_NAME + "/cache/";
    public static final String CELCIUM = "\u00B0";
    public static  final  String DEBUG_KEY = "DEBUGGG";
//    public static Typeface fontOswaldLight(AssetManager mgr){
//        Typeface font= Typeface.createFromAsset(mgr, TEXT_FONT_MAIN);
//        return font;
//    }

    public static String temperatureFormat(float temperature) {
        if (temperature > 0) return "+" + String.valueOf(Math.round(temperature));
        else return String.valueOf(Math.round(temperature));
    }


    public final static int VERTICAL = 1;


    public static  Drawable getWeatherIcon(int id, Context context ) {
        Drawable ico = null;
        if (id==800)   ico = context.getDrawable(R.drawable.day_synny);
        else {
            id = id / 100;

            switch (id) {
                case 2:
                    ico = context.getDrawable(R.drawable.day_thunder);
                    break;
                case 3:
                    ico = context.getDrawable(R.drawable.day_drizzle);
                    break;
                case 5:
                    ico = context.getDrawable(R.drawable.day_rainy);
                    break;
                case 6:
                    ico = context.getDrawable(R.drawable.day_snowie);
                    break;
                case 7:
                    ico = context.getDrawable(R.drawable.day_foggy);
                    break;
                case 8:
                    ico = context.getDrawable(R.drawable.day_cloudly);
                    break;

                default:
                    break;
            }
        }
        return ico;
    }

    public static String getWeatherDescription(int id, Context context){
        String[] desc = context.getResources().getStringArray(R.array.description_wether);
        return desc[id/100];
    }

    public static WeatherNote getNoteByName(String name, List<WeatherNote> elements) {
        if (elements.size() != 0) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getCity().equals(name)) {
                    return elements.get(i);
                }
            }
        }
        return null;
    }
}
