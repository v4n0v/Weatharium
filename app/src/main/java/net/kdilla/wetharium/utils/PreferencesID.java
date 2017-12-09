package net.kdilla.wetharium.utils;

import android.os.Environment;

import net.kdilla.wetharium.MainActivity;

public class PreferencesID {
    public static final String SAVED_WEATHER = "saved_weather";

    public static final String SAVED_WIND = "saved_wind";
    public static final String SAVED_PRESSURE = "saved_pressure";
    public static final String SAVED_HUMIDITY = "saved_storm";

    public static final String WEATHER_POS = "message tag";

    public static final String ADD_WIND = "add wind";
    public static final String ADD_PRESSURE = "add pressure";
    public static final String ADD_STORM = "add storm";
    public  final static String TAG_1 = "FRAGMENT_1";

    public static final int REQUEST_CODE_WEATHER = 1;
    public static final int REQUEST_CODE_IMAGE = 2;

    public static final String SAVED_CITY = "saved_city";
    public static final String EXTRA_CITY_NOM = "cityNom";


    public static final String APP_PREFS_NAME = MainActivity.class.getPackage().getName();
    public static final String APP_CACHE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + APP_PREFS_NAME + "/cache/";


    public final static int VERTICAL = 1;


}
