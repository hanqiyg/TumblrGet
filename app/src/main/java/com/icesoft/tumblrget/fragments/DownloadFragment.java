package com.icesoft.tumblrget.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icesoft.tumblrget.R;
import com.icesoft.tumblrget.adapters.DownloadingAdapter;

public class DownloadFragment extends Fragment
{
    private Context mContext;
    public static final String ARGUMENTS = "DownloadFragment";

    public static final String TAG = "DownloadFragment";

    private View rootView;
    private RecyclerView recyclerView;
    private DownloadingAdapter adapter;

    private Handler refreshUIHandler = new Handler();

    private long DELAY = 2000L;

    private  Runnable runnable;

    public static DownloadFragment newInstance(){
        DownloadFragment downloadFragment = new DownloadFragment();
        return downloadFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_video_download,container,false);
            recyclerView = rootView.findViewById(R.id.recycler);
            adapter = new DownloadingAdapter(mContext);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnable = new Runnable() {
            @Override
            public void run() {
                adapter.update();
                System.out.println("notify");
                refreshUIHandler.postDelayed(this,DELAY);
            }
        };
        runnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshUIHandler.removeCallbacks(runnable);
    }
}


