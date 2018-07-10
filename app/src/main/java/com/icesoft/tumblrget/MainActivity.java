package com.icesoft.tumblrget;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.icesoft.tumblrget.Utils.TumblrUtils;
import com.icesoft.tumblrget.asynctasks.GetTumblrLinksAsyncTask;
import com.icesoft.tumblrget.fragments.DownloadFragment;
import com.icesoft.tumblrget.fragments.TestWebViewFragment;
import com.icesoft.tumblrget.fragments.dialogs.ProgressDialog;
import com.icesoft.tumblrget.fragments.dialogs.RedownloadDialog;
import com.icesoft.tumblrget.models.BaseDetail;
import com.icesoft.tumblrget.tumblr.TumblrRestClient;
import com.icesoft.tumblrget.tumblr.fragments.SelectVideoDialogFragment;
import com.icesoft.tumblrget.tumblr.jsons.TumblrResponseJson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.commons.lang3.StringUtils;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity.class";
    private ProgressDialog progressDialog;
    private AdView mAdView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    App.getInstance().getFragmentByName(getSupportFragmentManager(), TestWebViewFragment.TAG);
                    return true;
                case R.id.navigation_dashboard:
                    App.getInstance().getFragmentByName(getSupportFragmentManager(), DownloadFragment.TAG);
                    return true;
                case R.id.navigation_notifications:
                    //App.getInstance().getFragmentByName(getSupportFragmentManager(),TestFragment.TAG);
/*                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);*/
                    Intent intent = new Intent(MainActivity.this, MailActivity.class);
                    startActivity(intent);
                    return true;
                default: return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        MobileAds.initialize(this,Constant.ADMOB_ID);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleTextMessage(intent);
            }
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        App.getInstance().getFragmentByName(getSupportFragmentManager(),TestWebViewFragment.TAG);
    }

    private void handleTextMessage(Intent intent)
    {
        final String share = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(share != null && !share.trim().equals(""))
        {
            final BaseDetail detail = App.getInstance().getDownloadBySource(share);
            if(detail != null)
            {
                RedownloadDialog dialog = new RedownloadDialog();
                dialog.setListener(new RedownloadDialog.OnClickListener() {
                    @Override
                    public void redownload() {
                        App.getInstance().deleteDownloadBySource(share);
                        detail.delete();
                        DownloadManager dm = (DownloadManager) MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                        dm.remove(detail.getDownloadId());
                        getSourceUrl(share);
                    }
                    @Override
                    public void cancel() {

                    }
                });
                dialog.show(getSupportFragmentManager(),"redownload");
            }else{
                getSourceUrl(share);
            }
        }
    }
    private void getSourceUrl(String url)
    {
        if(TumblrUtils.isTumblrLink(url)){
            // Share: https://chicaoni.tumblr.com/post/173956820507	Title: null
            GetTumblrLinksAsyncTask getTumblrLinksAsyncTask = new GetTumblrLinksAsyncTask(this);
            getTumblrLinksAsyncTask.execute(url);
            //asyncHttpClientTumblr(url);
        }else{
            String format = getResources().getString(R.string.link_not_recognized);
            Toast.makeText(this,String.format(format,url), Toast.LENGTH_LONG).show();
        }
    }
    public void test(String url)
    {
        getSourceUrl(url);
    }

    public void asyncHttpClientTumblr(final String url)
    {
        Log.e(TAG,"URL:\t" + url);
        TumblrRestClient.get(TumblrUtils.getBlogIdentifier(url), TumblrUtils.getPostId(url), new TextHttpResponseHandler() {
            long start = -1L;
            long end = -1L;
            @Override
            public void onStart() {
                super.onStart();
                start = System.currentTimeMillis();
                progressDialog = new ProgressDialog();
                progressDialog.setListener(new ProgressDialog.OnCancelListener() {
                    @Override
                    public void cancel() {
                        TumblrRestClient.cancel(TumblrUtils.getPostId(url));
                        cancelProgressDialog();
                    }
                });
                progressDialog.show(getSupportFragmentManager(),"Progress");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e(TAG,"onFinish");
                end = System.currentTimeMillis();
                Log.e(TAG,(end - start) + "ms.");
                cancelProgressDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                cancelProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = App.getInstance().getGson();
                TumblrResponseJson json = gson.fromJson(responseString, TumblrResponseJson.class);
                if(json != null &&
                        json.response != null &&
                        json.response.posts != null &&
                        json.response.posts.size() > 0 &&
                        json.response.posts.get(0) != null
                        )
                {
                    if(json.response.posts.get(0).type.trim().equalsIgnoreCase("video")){
                        TumblrResponseJson.ResponseBean.BlogBean blog = json.response.blog;
                        TumblrResponseJson.ResponseBean.PostsBean post = json.response.posts.get(0);
                        SelectVideoDialogFragment fragment = SelectVideoDialogFragment.newInstance(responseString);
                        fragment.show(getSupportFragmentManager(),"select");
                    }else if(json.response.posts.get(0).type.trim().equalsIgnoreCase("photo")){
                        TumblrResponseJson.ResponseBean.BlogBean blog = json.response.blog;
                        TumblrResponseJson.ResponseBean.PostsBean post = json.response.posts.get(0);
                    }else{

                    }
                }
                cancelProgressDialog();
            }
        });
    }
    private void printLongString(String context)
    {
        final int length = 2048;
        String rest = null;
        for(int i=0;    i< (context.length() / length) + 1;i++){
            System.out.println(StringUtils.substring(context,(i*length),(i+1)*length));
        }
    }
    public void cancelProgressDialog()
    {
        if(progressDialog != null && progressDialog.isVisible()){
            progressDialog.dismiss();
        }
    }
}
