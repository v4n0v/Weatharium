package net.kdilla.wetharium.utils;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kdilla.wetharium.utils.gson.Image;
import net.kdilla.wetharium.utils.gson.ImagesDeserializer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avetc on 19.12.2017.
 */

public class GoogleSearchThread {

    static final Handler handler = new Handler();

    public static String getJson() {
        return json;
    }

    static  String json;

    public static synchronized void getJson(final String str) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // looking for
                    String strNoSpaces = str.replace(" ", "+");

                    // Your API key
                    String key = "AIzaSyC_Hwm1br7QVYIQM1XykIIPeEnpT3CWQ08";

                    // Your Search Engine ID
                    String cx = "000850301007932783959:fyxoabpdp2e";

                    String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&searchType=image&alt=json";
                    Log.d("search_result", "Url = " + url2);
                   // json = httpGet(url2);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            saveJson(json);
//                        }
//                    });

                    URL url = new URL(url2);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() != 200) {
                        throw new IOException(conn.getResponseMessage());
                    }

                    Log.d("search_result", "Connection status = " + conn.getResponseMessage());

                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = rd.readLine()) != null) {

                        Log.d("search_result", "Line =" + rd.readLine());
                        sb.append(line + "\n");

                    }
                    rd.close();
                    conn.disconnect();

                    JSONObject jsonObject = new JSONObject(sb.toString());
                    Log.d("search", json);
                } catch (Exception e) {
                    System.out.println("Error1 " + e.getMessage());
                    Log.e("ERORO", e.getMessage());
                }
            }
        });

        thread.start();
    }

    private static void saveJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Image.class, new ImagesDeserializer())
                .create();
        Image image = gson.fromJson(json, Image.class);
        Log.d("IMAGE", image.getLink());
    }

}
