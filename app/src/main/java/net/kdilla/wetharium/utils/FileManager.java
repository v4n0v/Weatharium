package net.kdilla.wetharium.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import net.kdilla.wetharium.MainActivity;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by avetc on 22.12.2017.
 */

public class FileManager {



    public static Bitmap loadBitmap(Context context, String name) {
        File file = new File(context.getApplicationContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES), name.toLowerCase() + ".jpg");
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static void saveBitmap(Bitmap bitmap,Context context, String name) {
        try {

            File file = new File(context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), name.toLowerCase() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.flush();
            out.close();
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, file.getName(), file.getName()); // регистрация в фотоальбоме
        } catch (Exception e) {
            e.printStackTrace();
        }
    //    Toast.makeText(context, "Saved in storage as " + name.toLowerCase() + ".jpg", Toast.LENGTH_SHORT).show();
    }

    public static void deleteBitmap(Context context, String name) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name.toLowerCase() + ".jpg");
        if (file.exists()) {
            file.delete();
      //      Toast.makeText(context, "Image deleted from storage", Toast.LENGTH_SHORT).show();
        }

    }
}
