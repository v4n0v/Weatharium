package net.kdilla.wetharium.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avetc on 19.12.2017.
 */

public class GoogleSearch {
    static String KEY = "AIzaSyC_Hwm1br7QVYIQM1XykIIPeEnpT3CWQ08";
    static String CX = "000850301007932783959:fyxoabpdp2e";



    public static String getJson(String str) {

        String strNoSpaces = str.replace(" ", "+");

        String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&KEY=" + KEY + "&CX=" + CX + "&alt=json";
        Log.d("GCsearch", "Url = " + url2);
        //    String result2 =

        try {
//            URL url = new URL(url2);
            URL url = new URL(String.format(url2));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder(1024);
            String line;

            while ((line = rd.readLine()) != null) {

                Log.d("GCsearch", "Line =" + rd.readLine());
                sb.append(line + "\n");

            }
            rd.close();

          //  JSONObject jsonObject = new JSONObject(sb.toString());

            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            System.out.println("Error1 " + e.getMessage());
            return null;

        }
    }

}
