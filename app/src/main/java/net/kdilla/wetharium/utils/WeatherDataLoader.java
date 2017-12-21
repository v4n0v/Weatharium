package net.kdilla.wetharium.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avetc on 10.12.2017.
 */

public class WeatherDataLoader {
    private static final String OPEN_API_MAP = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String KEY = "x-api-KEY";
    private static final String OPEN_API_KEY = "1aa546d01134ed09d869b84c7e83e34f";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;



    public static JSONObject getJSONData(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_API_MAP, city));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.d("BITMAP", "Start weather download");

            connection.addRequestProperty(KEY, OPEN_API_KEY);

            BufferedReader reader = new BufferedReader((new InputStreamReader(connection.getInputStream())));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVar;

            while ((tempVar = reader.readLine()) != null) {
                rawData.append(tempVar).append(NEW_LINE);
            }
            JSONObject jsonObject = new JSONObject(rawData.toString());
            // API openweathermap

            if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            }
            Log.d("BITMAP", "Finish weather download");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
