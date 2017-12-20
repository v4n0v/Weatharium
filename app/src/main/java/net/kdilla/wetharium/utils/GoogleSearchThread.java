package net.kdilla.wetharium.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by avetc on 19.12.2017.
 */

public class GoogleSearchThread   {

    final Handler handler = new Handler();

    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    ArrayList<String> imageLinks;

    public GoogleSearchThread(String city) {
        getJson(city);
    }
    // static String json;

    private void getJson(final String str) {

        new Thread() {
            public void run() {

                try {
                    // looking for

                    String strNoSpaces = str.replace(" ", "+");

                    // Your API key
                    String key = "AIzaSyC_Hwm1br7QVYIQM1XykIIPeEnpT3CWQ08";

                    // Your Search Engine ID
                    String cx = "000850301007932783959:fyxoabpdp2e";

                    String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces+"+attractions+pictures"+ "&key=" + key + "&cx=" + cx + "&searchType=image&alt=json";
                    Log.d("URL_GET", "Url = " + url2);


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

//                    final  JSONObject jsonObject = new JSONObject(sb.toString());

                    String json=sb.toString();
                    imageLinks = getImagesArray(json);
                //    Log.d("search", jsonObject.toString());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            getImageFromJson(jsonObject);
//                        }
//                    });
                    Log.d("search_result", "Image links count: "+imageLinks.size());
                    rd.close();
                    conn.disconnect();
                } catch (Exception e) {
                    System.out.println("Error1 " + e.getMessage());
                    e.printStackTrace();
                }

            }

        }.start();
    }

    private ArrayList<String> getImagesArray(String json){
        ArrayList<String> links = new ArrayList<>();
        String[] lines = json.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("\"link\": \"")){
                String[] tempString = lines[i].split("://");
                tempString[1]= tempString[1].replace("\",", "");
                if (tempString[1].contains("?")){
                    String[] tempString2 = tempString[1].split("\\?");
                    tempString[1]=tempString2[0];
                }
                links.add("https://"+tempString[1]);
            }
        }
        return links;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
               Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }


    }
}

