package net.kdilla.wetharium.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.kdilla.wetharium.utils.tasks.BitmapSetterTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FlickrSearch {
    private final String KEY = "9c6b4a5f6ad93dafa5a5ca0ef3b2f864";
    private final int COUNT = 50;
    private final String IMAGE_SIZE = "url_m";
    //https://api.flickr.com/services/rest/?safe_search=safe&api_key=9c6b4a5f6ad93dafa5a5ca0ef3b2f864&format=json&text="kursk+city+main"&method=flickr.photos.search&media=photos&extras=url_m
    private final String REQUEST_IMAGE_KEY = "city";
    private  ImageView toolbarImage;
    private  Context context;
    private  String city;

    public FlickrSearch(ImageView toolbarImage, Context context) {
        this.context = context;
        this.toolbarImage = toolbarImage;


    }

    private  String lat;
    private  String lon;

    public void setLatAndLon(final String lat, final String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public FlickrSearch() {

    }

    public void downloadAndSetImage(final String city) {
        this.city = city;
        new Thread() {
            @Override
            public void run() {
                try {
                    String strNoSpaces = city.replace(" ", "+");
                    String link = "https://api.flickr.com/services/rest/?safe_search=safe&api_key="
                            + KEY + "&format=json&method=flickr.photos.search&media=photos&extras="
                            + IMAGE_SIZE + "&per_page=" + COUNT
                            + "&content_type=1&sort=relevance";

                    //если есть координаты, то то ним, если нет, то по названию города
                    if (lat != null && lon != null) {
                        link += "&lat=" + lat + "&lon=" + lon;
                        Log.d("BITMAP", "lat=" + lat + ", lon=" + lon);
                    } else {
                        link += "&text=" + strNoSpaces + "+"+ REQUEST_IMAGE_KEY;
                    }
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() != 200) {
                        Log.e("BITMAP",
                                "Exception: " + conn.getResponseMessage() + ", code " + conn.getResponseCode());
                        throw new IOException(conn.getResponseMessage());
                    }


                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = rd.readLine()) != null) {

                        Log.d("BITMAP", "Line =" + rd.readLine());
                        sb.append(line + "\n");

                    }
                    String st = sb.toString();
                    st = st.replace("jsonFlickrApi(", "");
                    st = st.replace(")", "");
                    JSONObject json = new JSONObject(st);

                    ArrayList<String> imageLinks = getImagesArray(st);
                    rd.close();
                    conn.disconnect();

                    int id = (int) (Math.random() * COUNT);
                    Log.d("BITMAP", "id=" + id);
                    new BitmapSetterTask(city, context, toolbarImage).execute(imageLinks.get(id));
                  //  new BitmapDownLoader(city,context).execute(imageLinks.get(id));
                } catch (Exception e) {
                    System.out.println("BITMAP" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private ArrayList<String> getImagesArray(String json) {
        ArrayList<String> links = new ArrayList<>();
        String[] lines = json.split(",");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("\"" + IMAGE_SIZE + "\":")) {
                String[] tempString = lines[i].split("\"" + IMAGE_SIZE + "\":");
                String tmp = tempString[1];
                tmp = tmp.replace("\"", "");
                links.add(tmp);
            }
        }
        Log.d("BITMAP", "Array getting complete");
        return links;
    }


}
