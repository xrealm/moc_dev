package com.mao.dev.ui.nested;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mao.dev.R;

/**
 * Created by Mao on 2016/12/14.
 */

public class NestedWebActivity extends AppCompatActivity {

    NestedWebView mNestedWebView;
    WebView mWebView;
    String url = "http://www.baidu.com/";
//    String url = "https://github.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_webview);

        initView();
        initData();
    }

    private void initData() {
        mNestedWebView.loadUrl(url);
//        mWebView.loadUrl(url);
    }

    private void initView() {
        mNestedWebView = (NestedWebView) findViewById(R.id.nested_webview);
//        mWebView = (WebView) findViewById(R.id.webview);

        mNestedWebView.getSettings().setJavaScriptEnabled(true);
//        mNestedWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mNestedWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mNestedWebView.setWebChromeClient(new WebChromeClient() {

        });
    }
}
