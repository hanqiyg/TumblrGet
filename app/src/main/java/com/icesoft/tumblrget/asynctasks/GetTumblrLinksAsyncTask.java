package com.icesoft.tumblrget.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.TumblrApiService;
import com.icesoft.tumblrget.Utils.MTimeUtils;
import com.icesoft.tumblrget.exceptions.NotSupportedPostTypeException;
import com.icesoft.tumblrget.fragments.dialogs.ErrorOccurDialog;
import com.icesoft.tumblrget.fragments.dialogs.NotFoundDialog;
import com.icesoft.tumblrget.fragments.dialogs.ProgressDialog;
import com.icesoft.tumblrget.fragments.dialogs.VideoSelectDialogFragment;
import com.icesoft.tumblrget.models.PhotoTumblrResult;
import com.icesoft.tumblrget.models.TumblrResult;
import com.icesoft.tumblrget.models.VideoDetail;
import com.icesoft.tumblrget.models.VideoTumblrResult;
import com.icesoft.tumblrget.tumblr.TumblrUtils;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.Video;
import com.tumblr.jumblr.types.VideoPost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GetTumblrLinksAsyncTask extends AsyncTask<String,Integer,TumblrResult> {
    private static final String TAG = "GetTumblrLinksAsyncTask";
    private final WeakReference<AppCompatActivity> mainActivityWeakReference;
    private ProgressDialog progressDialog;
    private NotFoundDialog notFoundDialog;
    private long start = -1L,end = -1L;

    public GetTumblrLinksAsyncTask(AppCompatActivity mainActivity) {
        this.mainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog();
        }
        progressDialog.setListener(new ProgressDialog.OnCancelListener() {
            @Override
            public void cancel() {
                if (!GetTumblrLinksAsyncTask.this.isCancelled()) {
                    GetTumblrLinksAsyncTask.this.cancel(true);
                }
            }
        });
        if (mainActivityWeakReference.get() != null) {
            progressDialog.show(mainActivityWeakReference.get().getSupportFragmentManager(), "");
        }
    }

    @Override
    protected TumblrResult doInBackground(String... strings) {
        TumblrResult result = null;
        if (strings != null && strings.length > 0) {
            System.out.println(strings[0]);
            try {
                String link = strings[0];
                start = System.currentTimeMillis();
                Post post = TumblrApiService.getPost(link);
                end = System.currentTimeMillis();
                Log.e(TAG,(end - start) + "ms.");
                System.out.println(post.getType());
                if (post != null && post instanceof VideoPost) {
                    result = getVideoDetails(link,(VideoPost) post);
                }else if(post != null && post instanceof PhotoPost){

                }else{
                    throw new NotSupportedPostTypeException("The post type is not support by GetTumblr. For now we only support VideoPost and PhotoPost.");
                }
            } catch (Exception e) {
                result = new TumblrResult(strings[0], e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(TumblrResult result) {
        if (progressDialog != null && progressDialog.isVisible()) {
            progressDialog.dismissAllowingStateLoss();
        }
        if (mainActivityWeakReference.get() == null) {
            return;
        }
        if (result.exception != null) {
            if (result.exception instanceof JumblrException && ((JumblrException)result.exception).getResponseCode()==404) {
                if (notFoundDialog == null) {
                    notFoundDialog = new NotFoundDialog();
                }
                if (mainActivityWeakReference.get() != null) {
                    notFoundDialog.show(mainActivityWeakReference.get().getSupportFragmentManager(), "notfound");
                }
            }else{
                result.exception.printStackTrace();
                System.out.println(result.exception.getMessage());
                Toast.makeText(mainActivityWeakReference.get(), result.exception.getMessage(), Toast.LENGTH_LONG).show();
                //TODO : custom dialog fragment for exceptions.
            }
        }
        if (result instanceof VideoTumblrResult) {
            VideoTumblrResult videoTumblrResult = (VideoTumblrResult) result;
            if (videoTumblrResult.getVideoDetails().length > 0){
                DialogFragment fragment = VideoSelectDialogFragment.newInstance(videoTumblrResult.getVideoDetails());
                fragment.show(mainActivityWeakReference.get().getSupportFragmentManager(),"select");
            }else{
                ErrorOccurDialog fragment = ErrorOccurDialog.newInstance("No video source found from link [ " + videoTumblrResult.queryString + " ].");
                fragment.show(mainActivityWeakReference.get().getSupportFragmentManager(),"error");
            }
        } else if (result instanceof PhotoTumblrResult) {
                System.out.println("photo post");
        }

        //System.out.println("onPostExecute end");
    }
    private VideoTumblrResult getVideoDetails(String sourceUrl,VideoPost post) throws InterruptedException, ExecutionException, TimeoutException {
        Set<VideoDetail> videoDetailSet = new HashSet<>();
        //List<VideoDetail> videoDetailSet = new ArrayList<>();
        VideoDetail videoDetail = null;
        for (Video v : post.getVideos()) {
            videoDetail = new VideoDetail();
            if(v.getEmbedCode() != null && TumblrUtils.getUrlFromEmbed(v.getEmbedCode()) != null) {
                videoDetail.urlQueryString = sourceUrl;
                videoDetail.embed_code = v.getEmbedCode();
                videoDetail.width = v.getWidth();

                videoDetail.postId = post.getId();
                videoDetail.blogName = post.getBlogName();
                videoDetail.postNoteCount = post.getNoteCount();
                videoDetail.postSlug = post.getSlug();
                videoDetail.postSource = post.getSourceTitle();
                videoDetail.posterFilePath = TumblrUtils.getPosterFromEmbed(v.getEmbedCode());
                videoDetail.blogAvatarFilePath = String.format(Constant.AVATAR_FORMATER, post.getBlogName());
                System.out.println("Jumblr embed \t" + videoDetail.posterFilePath);

                final String posterFilePath = videoDetail.posterFilePath;
                new MTimeUtils(new MTimeUtils.Timeable() {
                    @Override
                    public void beTimed() {
                        RequestOptions options = new RequestOptions();
                        options.diskCacheStrategy(DiskCacheStrategy.ALL);
                        try {
                            Glide.with(mainActivityWeakReference.get())
                                    .load(posterFilePath)
                                    .apply(options)
                                    .submit().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                videoDetailSet.add(videoDetail);
            }
        }
        System.out.println(videoDetailSet.size());
        return new VideoTumblrResult(sourceUrl, null, videoDetailSet.toArray(new VideoDetail[videoDetailSet.size()]));
    }
}
