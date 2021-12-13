package com.example.blitzbar;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCache {

    private Context context;

    public ImageCache(Context context){
        this.context = context;
    }

    public void updateContext(Context context){
        this.context = context;
    }

    public boolean cacheProfilePic(String username, ImageView imageView){

        try {

            String imagePath = username + ".png";

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("Profile_Picture", Context.MODE_PRIVATE);

            File image = new File(directory, imagePath);

            boolean success = false;

            // Encode the file as a PNG image.
            FileOutputStream outStream;
            try {

                outStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                outStream.flush();
                outStream.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (success)
                return true;
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public Bitmap getProfilePic(String username){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("Profile_Picture", Context.MODE_PRIVATE);

        String imagePath = directory.toString() + "/" + username + ".png";

        Bitmap bmp = BitmapFactory.decodeFile(imagePath);

        return bmp;
    }
}
