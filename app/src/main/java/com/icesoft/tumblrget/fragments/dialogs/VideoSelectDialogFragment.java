package com.icesoft.tumblrget.fragments.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icesoft.tumblrget.App;
import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.Utils.Downloader;
import com.icesoft.tumblrget.Utils.TumblrUtils;
import com.icesoft.tumblrget.adapters.VideoSelectAdapter;
import com.icesoft.tumblrget.fragments.DownloadFragment;
import com.icesoft.tumblrget.models.VideoDetail;

public class VideoSelectDialogFragment extends DialogFragment
{
    private Context mContext;
    public static final String ARGUMENTS = "VideoDownloadFragment";
    private View rootView;
    private RecyclerView recyclerView;
    private Button btnPositive,btnNegative;
    private VideoSelectAdapter adapter;

    public static VideoSelectDialogFragment newInstance(VideoDetail[] details)
    {
        VideoSelectDialogFragment fragment = new VideoSelectDialogFragment();
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        String resultJson = gson.toJson(details);
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
/*      全屏dialog
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);*/

        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
            adapter = new VideoSelectAdapter(mContext, new VideoSelectAdapter.OnClickListener() {
                @Override
                public void onDownloadClick(final VideoDetail detail) {
                    download(detail);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            btnPositive = rootView.findViewById(R.id.btnPositive);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoDetail[] ds = adapter.getChecked();
                    if(ds != null && ds.length > 0){
                        for(VideoDetail d : ds)
                        {
                            download(d);
                        }
                        App.getInstance().getFragmentByName(((AppCompatActivity)mContext).getSupportFragmentManager(), DownloadFragment.TAG);
                        getDialog().dismiss();
                    }else{
                        Toast.makeText(mContext,R.string.not_selected,Toast.LENGTH_LONG).show();
                    }
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
            VideoDetail[] details = gson.fromJson(json,VideoDetail[].class);
            adapter.setDetails(details);
        }
        return rootView;
    }

    public void download(VideoDetail detail)
    {
        if(detail != null)
        {
            String souceUrl = TumblrUtils.getUrlFromEmbed(detail.embed_code);
            String ext          = TumblrUtils.getVideoExtFilename(detail.embed_code);
            long id = Downloader.downloadTumblr(mContext,souceUrl, Constant.TUMBLR_TYPE_VIDEO,detail.blogName,detail.postId,ext);
            detail.downloadId = id;
            App.getInstance().addDownloadId(detail);
        }
    }
}
