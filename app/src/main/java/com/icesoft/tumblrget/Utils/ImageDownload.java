package com.icesoft.tumblrget.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ImageDownload
{
    public static String downloadImageWithGlide(Context context,String url,String path,String filename){
        Bitmap bitmap = null;
        FileOutputStream fos = null;
        try {
            bitmap = Glide.with(context).asBitmap().load(url).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        if(bitmap != null)
        {
            File root = new File(path);
            if(!root.exists())
            {
                root.mkdirs();
            }
            String currentFilename = filename + System.currentTimeMillis() + ".jpg";
            File current = new File(root,currentFilename);
            try {
                fos = new FileOutputStream(current);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                fos.flush();
                return current.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
