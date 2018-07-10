package com.icesoft.tumblrget.Utils;

import android.util.Log;

public class LogUtils
{
    public static void log(String tag,String message){
        Log.e(tag,message);
    }
    public static void log(String message){
        Log.e("Default",message);
    }
}
