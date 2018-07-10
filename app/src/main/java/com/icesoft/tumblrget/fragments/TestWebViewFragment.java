package com.icesoft.tumblrget.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.icesoft.tumblrget.R;

public class TestWebViewFragment extends Fragment{
    public static final String TAG = "TestWebViewFragment";
    public static final String BUNDLE_TITLE = "TestFragment.bundle.title";
    private View rootView;
    private WebView webview;
    private static final String DEFAULT_URL = "https://www.google.co.jp"; //"https://hanqiyg.github.io/GetTumblr/";
    private Bundle webViewState;

    public static TestWebViewFragment newInstance(String url) {
        TestWebViewFragment fragment = new TestWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, url);
        //设置参数
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(null == rootView)
        {
            rootView = inflater.inflate(R.layout.fragment_test_webview,container,false);
            webview = rootView.findViewById(R.id.webview);
            WebSettings webSettings = webview.getSettings();
            //设置缓存
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //设置能够解析Javascript
            webSettings.setJavaScriptEnabled(true);
            //设置适应Html5    重点是这个设置
            webSettings.setDomStorageEnabled(true);
        }
        if (webViewState != null) {
            //Fragment实例并未被销毁, 重新create view
            webview.restoreState(webViewState);
        } else if (savedInstanceState != null) {
            //Fragment实例被销毁重建
            webview.restoreState(savedInstanceState);
        } else {
            //全新Fragment
            webview.loadUrl(DEFAULT_URL);
        }
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();
        webview.onPause();

        //Fragment不被销毁(Fragment被加入back stack)的情况下, 依靠Fragment中的成员变量保存WebView状态
        webViewState = new Bundle();
        webview.saveState(webViewState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Fragment被销毁的情况, 依靠outState保存WebView状态
        if (webview != null) {
            webview.saveState(outState);
        }
    }
}
