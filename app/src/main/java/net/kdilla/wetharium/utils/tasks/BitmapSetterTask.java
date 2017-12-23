package net.kdilla.wetharium.utils.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import net.kdilla.wetharium.utils.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by avetc on 22.12.2017.
 */

public class BitmapSetterTask extends AsyncTask<String, Void, Bitmap> {
    Context context;
    String cityName;
    ImageView imageView;
    public BitmapSetterTask(String cityName, Context context, ImageView imageView) {
        this.context=context;
        this.cityName = cityName;
        this.imageView=imageView;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        //  saveBitmap(bitmap);
        FileManager.saveBitmap(bitmap, context, cityName);
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


    private void saveBitmap(Bitmap bitmap) {

        try {

            File file = new File(context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), cityName.toLowerCase() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, file.getName(), file.getName()); // регистрация в фотоальбоме
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Saved in storage as " + cityName.toLowerCase() + ".jpg", Toast.LENGTH_SHORT).show();
    }
}