package com.icesoft.tumblrget.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.icesoft.tumblrget.Constant;

import java.io.File;

public class Downloader
{
    public static long download(Context mContext, String url,Uri uri,boolean allowMobile,boolean notification)
    {
        DownloadManager downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if(allowMobile){
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        }else{
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }
        if(notification){
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }else{
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request.setDestinationUri(uri);
        return downloadManager.enqueue(request);
    }
    public static long downloadTumblr(Context mContext,String url,String type,String blogName,long postId,String ext)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String naming = pref.getString("naming_rule", Constant.DEFAULT_NAMING_RULE);
        boolean allowMobile = pref.getBoolean("allow_mobile",Constant.DEFAULT_ALLOW_MOBILE);
        boolean notification = pref.getBoolean("notification_visibility",Constant.DEFAULT_NOTIFICATION_VISIBILITY);
        System.out.println("notification:" + notification);
        String filename = String.format(naming,type,blogName,postId,ext);
        File file = new File(pref.getString("storage_location",null),filename);
        Uri uri = Uri.fromFile(file);
        System.out.println(uri.toString());
        return download(mContext,url,uri,allowMobile,notification);
    }
}
