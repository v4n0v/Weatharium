package net.kdilla.wetharium.utils;

import android.os.Environment;

import net.kdilla.wetharium.MainActivity;

/**
 * Created by avetc on 10.12.2017.
 */

public class Preferences {
    public static final String SAVED_WEATHER = "saved_weather";

    public static final String SAVED_WIND = "saved_wind";
    public static final String SAVED_PRESSURE = "saved_pressure";
    public static final String SAVED_HUMIDITY = "saved_storm";

    public static final String WEATHER_POS = "message tag";

    public static final String ADD_CITY = "add city";
    public static final String ADD_TEMP = "add temp";
    public static final String ADD_WIND = "add wind";
    public static final String ADD_PRESSURE = "add pressure";
    public static final String ADD_HUMIDITY = "add humidity";
    public static final String ADD_IMAGE_ID = "add image id";
    public static final String ADD_IS_WIND = "add is wind";
    public static final String ADD_IS_PRESSURE = "add is pressure";
    public static final String ADD_IS_HUMIDITY = "add is humidity";
    public static final String ADD_DESCRIPTION = "add description";
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


//    public static Typeface fontOswaldLight(AssetManager mgr){
//        Typeface font= Typeface.createFromAsset(mgr, TEXT_FONT_MAIN);
//        return font;
//    }


    public final static int VERTICAL = 1;




}
