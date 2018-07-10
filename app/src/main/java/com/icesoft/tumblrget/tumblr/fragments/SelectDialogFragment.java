package com.icesoft.tumblrget.tumblr.fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.Utils.TumblrUtils;
import com.icesoft.tumblrget.tumblr.jsons.TumblrResponseJson;
import com.icesoft.tumblrget.tumblr.models.ErrorDetail;
import com.icesoft.tumblrget.tumblr.models.ITumblrDetail;
import com.icesoft.tumblrget.tumblr.models.VideoDetail;

import java.util.ArrayList;
import java.util.List;

public class SelectDialogFragment extends DialogFragment
{
    private Context mContext;
    public static final String ARGUMENTS = "VideoDownloadFragment";
    private View rootView;
    private RecyclerView recyclerView;
    private Button btnPositive,btnNegative;
    private Adapter adapter;

    public static SelectDialogFragment newInstance(String json)
    {
        SelectDialogFragment fragment = new SelectDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS,json);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_dialog_select_video,container,false);
            recyclerView = rootView.findViewById(R.id.recycler);
            adapter = new Adapter(mContext);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            btnPositive = rootView.findViewById(R.id.btnPositive);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnNegative = rootView.findViewById(R.id.btnNegative);
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
        }
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String json = bundle.getString(ARGUMENTS);
            adapter.setJsonData(json);
        }
        return rootView;
    }
    class Adapter extends RecyclerView.Adapter{
        private Context mContext;
        private LayoutInflater mInflater;
        private Gson gson;
        private List<ITumblrDetail> details = new ArrayList<>();
        private boolean[] checked;

        public Adapter(Context mContext) {
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getItemViewType(int position) {
            ITumblrDetail detail = details.get(position);
            if(detail == null){return -1;}
            return detail.getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType)
            {
                case -1 :               return new Error(mInflater.inflate(R.layout.item_error_select,parent,false));
                case ErrorDetail.TYPE : return new Error(mInflater.inflate(R.layout.item_error_select,parent,false));
                case VideoDetail.TYPE : return new VideoViewHolder(mInflater.inflate(R.layout.item_video_select,parent,false));
                default:                return new Error(mInflater.inflate(R.layout.item_error_select,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ITumblrDetail detail = details.get(position);
            if(holder instanceof VideoViewHolder && detail instanceof VideoDetail){
                final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                VideoDetail videoDetail = (VideoDetail) detail;

                videoViewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checked[position] = isChecked;
                    }
                });
                videoViewHolder.tvBlogName.setText(videoDetail.getBlogName());
                videoViewHolder.tvNoteCount.setText(String.format("NoteCount : %d",videoDetail.getNoteCount()));
                videoViewHolder.tvSlug.setText(videoDetail.getSlug());
                StringBuffer buffer = new StringBuffer();
                for(String tag : videoDetail.getTags()){
                    buffer.append("#");
                    buffer.append(tag);
                    buffer.append(" ");
                }
                videoViewHolder.tvTag.setText(buffer.toString());

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(Constant.LOAD_WAIT_DRAWABLE);
                requestOptions.error(Constant.LOAD_ERROR_DRAWABLE);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mContext)
                        .setDefaultRequestOptions(requestOptions)
                        .load(videoDetail.getBlogAvatarUrl())
                        .into(videoViewHolder.ivBlogAvatar);
                System.out.println("from thumb : " + videoDetail.getPosterUrl());
                Glide.with(mContext)
                        .setDefaultRequestOptions(requestOptions)
                        .load(videoDetail.getPosterUrl())
                        .into(videoViewHolder.ivPoster);
            }else if(holder instanceof Error){
                Error error = (Error) holder;
                if(detail == null){
                    error.tvMessage.setText("Unknow error occur");
                }else if(detail instanceof ErrorDetail){
                    ErrorDetail errorDetail = (ErrorDetail) detail;
                    error.tvMessage.setText(errorDetail.getMessage());
                }else {
                    error.tvMessage.setText("Unknow error occur");
                }
            }
        }

        @Override
        public int getItemCount() {
            return details.size();
        }
        public void setJsonData(String json)
        {
            TumblrResponseJson response = getGson().fromJson(json, TumblrResponseJson.class);
            if(response != null &&
                    response.response != null &&
                    response.response.posts != null &&
                    response.response.posts.size() > 0 &&
                    response.response.posts.get(0) != null
                    )
            {
                if(response.response.posts.get(0).type.trim().equalsIgnoreCase("video")){
                    TumblrResponseJson.ResponseBean.BlogBean blog = response.response.blog;
                    TumblrResponseJson.ResponseBean.PostsBean post = response.response.posts.get(0);
                    String filename = TumblrUtils.getFileNameFromUrl(post.videoUrl);

 /*                       long id = Downloader.download(
                        MainActivity.this,
                                post.videoUrl,
                                "/GetTumblr/tumblr/videos" + File.separator + blog.name,
                                filename);*/
                    VideoDetail vd = new VideoDetail(blog.name,post.videoUrl,post.thumbnailUrl,post.slug,post.tags,post.noteCount,-1L);
                    System.out.println("from embed:" + TumblrUtils.getPosterFromEmbed(post.player.get(0).embedCode));
                    details.clear();
                    details.add(vd);
                    checked = new boolean[1];
                }else if(response.response.posts.get(0).type.trim().equalsIgnoreCase("photo")){
                    TumblrResponseJson.ResponseBean.BlogBean blog = response.response.blog;
                    TumblrResponseJson.ResponseBean.PostsBean post = response.response.posts.get(0);
                }else{
                    ErrorDetail detail = new ErrorDetail("Media Type not Supported.");
                    details.clear();
                    details.add(detail);
                }
            }else{
                ErrorDetail detail = new ErrorDetail("Tumblr Server not responses.");
                details.clear();
                details.add(detail);
            }
        }
        public Gson getGson(){
            if(gson == null){
                gson = new Gson();
            }
            return gson;
        }
        public ITumblrDetail[] getChecked()
        {
            List<ITumblrDetail> tds = new ArrayList<>();
            for(int i = 0; i < checked.length; i++){
                if(checked[i]){
                    tds.add(details.get(i));
                }
            }
            return tds.toArray(new ITumblrDetail[tds.size()]);
        }
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivBlogAvatar;
        public TextView tvBlogName;
        public CheckBox cbSelect;

        public ImageView ivPoster;

        public TextView tvSlug;
        public TextView tvTag;
        public TextView tvSource;
        public TextView tvNoteCount;

        public VideoViewHolder(View v) {
            super(v);
            ivBlogAvatar    = v.findViewById(R.id.ivBlogAvatar);
            tvBlogName      = v.findViewById(R.id.tvBlogName);
            cbSelect        = v.findViewById(R.id.cbSelect);

            ivPoster        = v.findViewById(R.id.ivPoster);

            tvSlug          = v.findViewById(R.id.tvSlug);
            tvTag           = v.findViewById(R.id.tvTag);
            tvSource        = v.findViewById(R.id.tvSource);
            tvNoteCount     = v.findViewById(R.id.tvNoteCount);
        }
    }
    public static class Error extends RecyclerView.ViewHolder {
        public TextView tvMessage;

        public Error(View v) {
            super(v);
            tvMessage         = v.findViewById(R.id.tvMessage);
        }
    }
}
