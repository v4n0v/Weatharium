package net.kdilla.wetharium.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
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
    ImageView imageView;
    public GoogleSearchThread(ImageView imageView) {
        this.imageView=imageView;
        //getAndSetImage(city);
    }
    // static String json;

    public void getAndSetImage(final String str) {

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
    //  https://www.googleapis.com/customsearch/v1?q="city"+attractions+pictures"+ "&key="AIzaSyC_Hwm1br7QVYIQM1XykIIPeEnpT3CWQ08"&cx="000850301007932783959:fyxoabpdp2e"&searchType=image&alt=json
                    URL url = new URL(url2);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() != 200) {
                        Log.e("search_result",
                                "Exception: "+conn.getResponseMessage()+", code "+conn.getResponseCode());
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
                    new BitmapDownLoader().execute(imageLinks);
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


    class BitmapDownLoader extends AsyncTask<ArrayList<String>, Void, ArrayList<Bitmap>> {
        int IMAGE_COUNT = 3;

        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>[] arrayLists) {
            publishProgress(new Void[]{});
            ArrayList<Bitmap> images = new ArrayList<>();
            ArrayList<String> links = new ArrayList<>();
            if (arrayLists.length > 0) {
                links = arrayLists[0];
            }
            for (int i = 0; i < IMAGE_COUNT; i++) {
                try {
                    URL newUrl = new URL(links.get(i));
                    //final Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
                    Log.d("BITMAP", newUrl.toString());
                    InputStream in = newUrl.openStream();
                    final Bitmap bmp = BitmapFactory.decodeStream(in);
                    images.add(bmp);
                } catch (Exception e) {
                    Log.d("BITMAP", "Wrong link");

                    e.printStackTrace();
                }

            }
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i)==null) images.remove(i);
            }
            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            int max=bitmaps.size()-1;
            int id = (int)(Math.random()*++max);
            id = 0;
            if (id>IMAGE_COUNT)id=IMAGE_COUNT;
            Log.d("BITMAP", String.valueOf(id));
            Bitmap img = bitmaps.get(id) ;
            imageView.setImageBitmap(img);
        }
    }
}

