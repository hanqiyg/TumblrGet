package com.icesoft.tumblrget.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.icesoft.tumblrget.Constant;
import com.icesoft.tumblrget.MainActivity;
import com.icesoft.tumblrget.R;


public class TestFragment extends Fragment
{
    public static final String TAG = "TestFragment";
    private static final String T = "TestFragment";
    public static final String BUNDLE_TITLE = "TestFragment.bundle.title";
    private View rootView;
    private TextView tvTitle;
    private MainActivity mActivity;
    private Button btnFail,btnSucc,btnNotFound,btnShortScreen,btnLongScreen;
    private String url;
    public static TestFragment newInstance(/*String url*/) {
        TestFragment fragment = new TestFragment();
/*        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, url);
        fragment.setArguments(bundle);   //设置参数
        System.out.println(T + "@Param[" + url + "].");*/
        return fragment;
    }
    public static TestFragment newInstance(String url) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, url);
        fragment.setArguments(bundle);   //设置参数
        System.out.println(T + "@Param[" + url + "].");
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(null == rootView)
        {
            rootView = inflater.inflate(R.layout.fragment_test_url,container,false);
     /*       tvTitle = rootView.findViewById(R.id.tvTitle);
            Bundle bundle = getArguments();
            if(bundle != null)
            {
                String title = bundle.getString(BUNDLE_TITLE);
                tvTitle.setText(title);
            }
            btnSucc = rootView.findViewById(R.id.btn);
            btnSucc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.test(Constant.IS_TUMBLR_SUCC_URL);
                }
            });
            btnFail = rootView.findViewById(R.id.btnFail);
            btnFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.test(Constant.IS_TUMBLR_FAIL_URL);
                }
            });
            btnNotFound = rootView.findViewById(R.id.btnNotFound);
            btnNotFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.test(Constant.PRIVATE_LINK_URL);
                }
            });
            btnShortScreen = rootView.findViewById(R.id.btnShortScreen);
            btnShortScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.test(Constant.SHORT_SCREEN_URL);
                }
            });
            btnLongScreen = rootView.findViewById(R.id.btnLongScreen);
            btnLongScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.test(Constant.LONG_SCREEN_URL);
                }
            });*/
        }
       return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        if(getArguments() != null) {
            url = getArguments().getString(BUNDLE_TITLE);
        }
    }
}
