package com.example.srct.bigbong.view.impl;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.srct.bigbong.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaiduSearchActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_search);
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        String url = getSearchUrl(getIntent().getStringExtra("text"));
        mWebView.loadUrl(url);
    }

    private String getSearchUrl(String text) {
        text = text.replaceAll("\n", "");
        String url = "https://m.baidu.com/s?word=";
        try {
            return url + URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return url + text;
    }
}
