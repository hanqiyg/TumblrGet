package com.icesoft.tumblrget.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DirectoryUtils {

    private static final String LOG_TAG = "DirectoryUtils";

    public static void getEnvironmenrectories() {
        Log.e(LOG_TAG, "getRootDirectory(): "
                + Environment.getRootDirectory().toString());
        Log.e(LOG_TAG, "getDataDirectory(): "
                + Environment.getDataDirectory().toString());
        Log.e(LOG_TAG, "getDownloadCacheDirectory(): "
                + Environment.getDownloadCacheDirectory().toString());
        Log.e(LOG_TAG, "getExternalStorageDirectory(): "
                + Environment.getExternalStorageDirectory().toString());

        Log.e(
                LOG_TAG,
                "getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES): "
                        + Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString());

//        LogUtils.i(
//                LOG_TAG,
//                "isExternalStorageEmulated(): "
//                        + Environment.isExternalStorageEmulated());
//
//        LogUtils.i(
//                LOG_TAG,
//                "isExternalStorageRemovable(): "
//                        + Environment.isExternalStorageRemovable());

    }

    public static void getApplicationDirectories(Context context) {

        Log.e(LOG_TAG, "context.getFilesDir(): "
                + context.getFilesDir().toString());
        Log.e(LOG_TAG, "context.getCacheDir(): "
                + context.getCacheDir().toString());

        // methods below will return null if the permissions denied
        Log.e(
                LOG_TAG,
                "context.getExternalFilesDir(Environment.DIRECTORY_MOVIES): "
                        + context
                        .getExternalFilesDir(Environment.DIRECTORY_MOVIES));

        Log.e(
                LOG_TAG,
                "context.getExternalCacheDir(): "
                        + context.getExternalCacheDir());
    }
    public static File[] getExtraFilesDirs(Context context)
    {
        File[] appsDir;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            appsDir = context.getExternalFilesDirs(null);
            for (File file : appsDir) {
                Log.e("Tag", file.getAbsolutePath());
            }
        }else{
            File file = context.getExternalFilesDir(null);
            Log.e("Tag", file.getAbsolutePath());
            appsDir = new File[]{file};
        }
        return appsDir;
    }
    public static File getDefaultExtraFileDir(Context context){
        return context.getExternalFilesDir(null);
    }
}
