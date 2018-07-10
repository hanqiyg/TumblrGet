package com.icesoft.tumblrget.fragments.dialogs;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.icesoft.tumblrget.App;
import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.adapters.DownloadingAdapter;
import com.icesoft.tumblrget.models.VideoDetail;

import java.lang.ref.WeakReference;

public class DeleteConfirmDialogFragment extends DialogFragment
{
    private Context mContext;
    public static final String ARGUMENTS = "DeleteConfirmDialogFragment";
    private View rootView;

    private ImageView ivBlogAvatar;
    private TextView tvBlogName;

    private ImageView ivPoster;

    private Button btnPositive,btnNegative;
    private CheckBox cbDeleteFile;

    private boolean deletefile = false;
    private VideoDetail detail;

    private static WeakReference<DownloadingAdapter> adapterWeakReference;
    private static int position;

    public static DeleteConfirmDialogFragment newInstance(VideoDetail videoDetail, DownloadingAdapter downloadingAdapter, int p)
    {
        DeleteConfirmDialogFragment fragment = new DeleteConfirmDialogFragment();
        adapterWeakReference = new WeakReference<>(downloadingAdapter);
        position = p;
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        String resultJson = gson.toJson(videoDetail);
        bundle.putString(ARGUMENTS,resultJson);
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
            rootView = inflater.inflate(R.layout.fragment_dialog_delete_post,container,false);
            ivBlogAvatar =rootView.findViewById(R.id.ivBlogAvatar);
            tvBlogName =rootView.findViewById(R.id.tvBlogName);
            ivPoster =rootView.findViewById(R.id.ivPoster);
            cbDeleteFile =rootView.findViewById(R.id.cbDeleteFile);

            btnPositive = rootView.findViewById(R.id.btnPositive);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(detail,deletefile);
                    if(adapterWeakReference.get() != null){
                        adapterWeakReference.get().notifyItemRemoved(position);
                    }
                    getDialog().dismiss();
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
            Gson gson = new Gson();
            System.out.println(gson);
            detail = gson.fromJson(json,VideoDetail.class);
            tvBlogName.setText(detail.blogName);

            RequestOptions request = new RequestOptions()
                    .placeholder(Constant.LOAD_WAIT_DRAWABLE)
                    .error(Constant.LOAD_ERROR_DRAWABLE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
            if(detail.posterFilePath != null){
                Glide.with(mContext)
                        .setDefaultRequestOptions(request)
                        .load(detail.posterFilePath)
                        .into(ivPoster);
            }
            if(detail.blogAvatarFilePath != null){
                Glide.with(mContext)
                        .setDefaultRequestOptions(request)
                        .load(detail.blogAvatarFilePath)
                        .into(ivBlogAvatar);
            }
            cbDeleteFile.setChecked(deletefile);
            cbDeleteFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    deletefile = isChecked;
                }
            });

        }
        return rootView;
    }
    public void delete(VideoDetail detail,boolean deletefile)
    {
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        if(dm != null && detail != null && detail.downloadId != -1){
            App.getInstance().deleteDowonloadById(detail.downloadId);
            if(deletefile){
                dm.remove(detail.downloadId);
            }
        }
    }
}
