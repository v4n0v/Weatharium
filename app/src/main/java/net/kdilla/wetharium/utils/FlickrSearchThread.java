package net.kdilla.wetharium.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by avetc on 21.12.2017.
 */

public class FlickrSearchThread extends Thread {
    private final String KEY = "9c6b4a5f6ad93dafa5a5ca0ef3b2f864";
    private final int COUNT = 10;
    //https://api.flickr.com/services/rest/?safe_search=safe&api_key=9c6b4a5f6ad93dafa5a5ca0ef3b2f864&format=json&text="kursk+city"&method=flickr.photos.search&media=photos&extras=url_m

    ImageView toolbarImage;

    public void setCity(String city) {
        this.city = city;
    }

    String city;

    public FlickrSearchThread(ImageView toolbarImage) {
        this.toolbarImage = toolbarImage;
    }

    @Override
    public void run() {

        try {
            String strNoSpaces = city.replace(" ", "+");
            String link = "https://api.flickr.com/services/rest/?safe_search=safe&api_key=" + KEY + "&format=json&text=" + strNoSpaces + "+city&method=flickr.photos.search&media=photos&extras=url_m&per_page=" + COUNT + "&content_type=1&sort=relevance";

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
            new BitmapDownLoader().execute(imageLinks.get(id));
        } catch (Exception e) {
            System.out.println("BITMAP" + e.getMessage());
            e.printStackTrace();
        }
    }


    private ArrayList<String> getImagesArray(String json) {
        ArrayList<String> links = new ArrayList<>();
        String[] lines = json.split(",");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("\"url_m\":")) {
                String[] tempString = lines[i].split("\"url_m\":");
                String tmp = tempString[1];
                tmp = tmp.replace("\"", "");
                links.add(tmp);
            }
        }
        Log.d("BITMAP", "Array getting complete");
        return links;
    }


class BitmapDownLoader extends AsyncTask<String, Void, Bitmap> {
    int IMAGE_COUNT = 3;
//
//        @Override
//        protected ArrayList<Bitmap> doInBackground(String arrayLists) {
//            publishProgress(new Void[]{});
//            ArrayList<Bitmap> images = new ArrayList<>();
//            ArrayList<String> links = new ArrayList<>();
//            if (arrayLists.length > 0) {
//                links = arrayLists[0];
//            }
//            for (int i = 0; i < IMAGE_COUNT; i++) {
//                try {
//                    URL newUrl = new URL(links.get(i));
//                    //final Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
//                    Log.d("BITMAP", newUrl.toString());
//                    InputStream in = newUrl.openStream();
//                    final Bitmap bmp = BitmapFactory.decodeStream(in);
//                    images.add(bmp);
//                } catch (Exception e) {
//                    Log.d("BITMAP", "Wrong link");
//
//                    e.printStackTrace();
//                }
//
//            }
//            for (int i = 0; i < images.size(); i++) {
//                if (images.get(i) == null) images.remove(i);
//            }
//            return images;
//        }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
//            int max = bitmaps.size() - 1;
//            int id = (int) (Math.random() * ++max);
//            id = 0;
//            if (id > IMAGE_COUNT) id = IMAGE_COUNT;
//            Log.d("BITMAP", String.valueOf(id));
//            Bitmap img = bitmaps.get(id);
        toolbarImage.setImageBitmap(bitmap);
        Log.d("BITMAP", "Bitmap applied");
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        Bitmap images = null;
        try {
            URL newUrl = new URL(strings[0]);
            //final Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
            Log.d("BITMAP", newUrl.toString());
            InputStream in = newUrl.openStream();
            images = BitmapFactory.decodeStream(in);
            Log.d("BITMAP", "Bitmap downloaded");
        } catch (Exception e) {
            Log.d("BITMAP", "Wrong link");

            e.printStackTrace();
        }
        return images;
    }
}
}
