package com.icesoft.tumblrget;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;

import com.icesoft.tumblrget.Utils.DirectoryUtils;
import com.icesoft.tumblrget.fragments.DownloadFragment;
import com.icesoft.tumblrget.fragments.TestFragment;
import com.icesoft.tumblrget.fragments.TestWebViewFragment;
import com.icesoft.tumblrget.models.BaseDetail;
import com.icesoft.tumblrget.models.TypedJson;
import com.icesoft.tumblrget.models.VideoDetail;
import com.icesoft.tumblrget.sqlite.DownloadDao;

import java.io.File;

public class App extends Application {

    // Singleton instance
    private static App sInstance = null;
    private DownloadDao downloadDao;
    private Gson gson = new Gson();
    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        sInstance = this;
        downloadDao = new DownloadDao(this);
        DefaultSettings();
    }

    private void DefaultSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = pref.getBoolean("first_run",true);
        if(firstRun){
            String storageRoot = null;
            File file = DirectoryUtils.getDefaultExtraFileDir(this);
            if(file != null){
                storageRoot = file.getAbsolutePath();
            }
            pref.edit()
                    .putString("storage_location",storageRoot)
                    .putString("naming_rule", Constant.DEFAULT_NAMING_RULE)
                    .putBoolean("allow_mobile",Constant.DEFAULT_ALLOW_MOBILE)
                    .putBoolean("notification_visibility",Constant.DEFAULT_NOTIFICATION_VISIBILITY)
                    .putBoolean("first_run",false)
                    .apply();
        }
    }

    // Getter to access Singleton instance
    public static App getInstance() {
        return sInstance ;
    }

    public DownloadDao getDownloadDao() {
        return downloadDao;
    }

    public void addDownloadId(VideoDetail result) {
        downloadDao.addDownload(VideoDetail.TYPE,result.urlQueryString,result.downloadId,gson.toJson(result));
    }
    public BaseDetail getDownloadDetail(int type, int position)
    {
        String json = downloadDao.getDownloadByType(type,position);
        if(json == null)
        {
            return null;
        }
        switch (type)
        {
            case VideoDetail.TYPE : return gson.fromJson(json,VideoDetail.class);
            default: return null;
        }
    }
    public void deleteDownloadBySource(String url)
    {
        downloadDao.delDownloadBySourceUrl(url);
    }
    public int getDownloadCountByType(int type)
    {
        return downloadDao.getCountByType(type);
    }

    public BaseDetail getDownloadBySource(String link) {
        TypedJson typedJson = downloadDao.getDownloadBySource(link);
        if(typedJson == null)
        {
            return null;
        }
        switch (typedJson.type)
        {
            case VideoDetail.TYPE : return gson.fromJson(typedJson.json,VideoDetail.class);
            default: return null;
        }
    }
    public void deleteDowonloadById(Long id)
    {
        downloadDao.deleteDownloadById(id);
    }
    public String getTumblrPath()
    {
        File file = Environment.getExternalStoragePublicDirectory(
                "GetTumblr" + File.separator  + "Tumblr"
                    );
        return file.getAbsolutePath();
    }
    private Fragment current;
    public void getFragmentByName(FragmentManager manager, String fragmentName)
    {
        System.out.println(fragmentName + "\tcurrent:" + (current==null ? "null" : current.getTag()));
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = null;
        if(current != null)
        {
            if(current.getTag() != null && (fragmentName.equals(current.getTag()))){
                return;
            }else{
                transaction.hide(current);
            }
        }
        switch(fragmentName)
        {
            case TestWebViewFragment.TAG : {
                fragment = manager.findFragmentByTag(TestWebViewFragment.TAG);
                if(fragment == null){
                    fragment = TestWebViewFragment.newInstance("https://www.google.co.jp");
                    transaction.add(R.id.fragment,fragment,TestWebViewFragment.TAG);
                }
            }break;
            case DownloadFragment.TAG : {
                fragment = manager.findFragmentByTag(DownloadFragment.TAG);
                if(fragment == null) {
                    fragment = DownloadFragment.newInstance();
                    transaction.add(R.id.fragment,fragment,DownloadFragment.TAG);
                }
            }break;
            case TestFragment.TAG : {
                fragment = manager.findFragmentByTag(TestFragment.TAG);
                if(fragment == null) {
                    fragment = TestFragment.newInstance("https://www.google.co.jp");
                    transaction.add(R.id.fragment,fragment,TestFragment.TAG);
                }
            }break;
            default:break;
        }
        current = fragment;
        transaction.show(fragment).commit();
        System.out.println("After \t" + "current:" + (current==null ? "null" : current.getTag()));
    }

    public Gson getGson() {
        return gson;
    }
}
