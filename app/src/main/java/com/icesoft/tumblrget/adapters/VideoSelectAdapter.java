package com.icesoft.tumblrget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.Utils.MTimeUtils;
import com.icesoft.tumblrget.models.VideoDetail;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VideoSelectAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private OnClickListener mListener;
    private LayoutInflater mLayoutInflater;
    private VideoDetail[] details;
    public  boolean[] checked;

    public VideoSelectAdapter(Context context, OnClickListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_video_select, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder && details!= null && details.length > position)
        {
            final VideoDetail detail = details[position];
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.cbSelect.setVisibility(View.VISIBLE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(Constant.LOAD_WAIT_DRAWABLE);
            requestOptions.error(Constant.LOAD_ERROR_DRAWABLE);
/*            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptions)
                    .load(detail.posterFilePath)
                    .into(viewHolder.ivPoster);*/
            new MTimeUtils(new MTimeUtils.Timeable() {
                @Override
                public void beTimed() {
                    RequestOptions options = new RequestOptions();
                    options.diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(mContext)
                            .load(detail.posterFilePath)
                            .apply(options)
                            .into(viewHolder.ivPoster);;
                }
            });
            RequestOptions requestOptionsSmall = new RequestOptions();
            requestOptionsSmall.placeholder(Constant.LOAD_WAIT_DRAWABLE);
            requestOptionsSmall.error(Constant.LOAD_ERROR_DRAWABLE);
            Glide.with(mContext)
                    .setDefaultRequestOptions(requestOptionsSmall)
                    .load(detail.blogAvatarFilePath)
                    .into(viewHolder.ivBlogAvatar);
            viewHolder.tvBlogName.setText(detail.blogName);
            viewHolder.cbSelect.setChecked(checked[position]);
            viewHolder.ivPoster.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   boolean isCheck = viewHolder.cbSelect.isChecked();
                   viewHolder.cbSelect.setChecked(!isCheck);
                   checked[position] = ! isCheck;
                   System.out.println(checked);
               }
           });
            viewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    System.out.println(b);
                    checked[position] = b;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return details==null?0:details.length;
    }

    public VideoDetail[] getChecked() {
        List<VideoDetail> ds = new ArrayList<>();
        for (int i=0;i<checked.length;i++){
            if(checked[i]){
                ds.add(details[i]);
            }
        }
        return ds.toArray(new VideoDetail[ds.size()]);
    }

    public void setDetails(VideoDetail[] details) {
        checked = new boolean[details.length];
        this.details = details;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivBlogAvatar;
        public TextView tvBlogName;
        public ImageView ivPoster;
        public CheckBox cbSelect;

        public ViewHolder(View v) {
            super(v);
            ivBlogAvatar = v.findViewById(R.id.ivBlogAvatar);
            tvBlogName = v.findViewById(R.id.tvBlogName);
            cbSelect = v.findViewById(R.id.cbSelect);
            ivPoster = v.findViewById(R.id.ivPoster);
        }
    }
    public interface OnClickListener{
        void onDownloadClick(VideoDetail detail);
    }
}
