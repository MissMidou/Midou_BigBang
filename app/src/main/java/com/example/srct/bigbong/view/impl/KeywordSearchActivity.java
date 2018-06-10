package com.example.srct.bigbong.view.impl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.example.srct.bigbong.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by srct on 2018/5/17.
 */

public class KeywordSearchActivity extends Activity {
    private final String TAG = "KeywordSearchActivity";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_search);
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Log.d(TAG,"lzh type="+getIntent().getIntExtra("type",4));
        String url = getSearchUrl(getIntent().getStringExtra("text"), getIntent().getIntExtra("type",4));
        mWebView.loadUrl(url);
    }

    private String getSearchUrl(String text, int type) {
        text = text.replaceAll("\n", "");
        String url;
        switch(type) {
            case BigbangActivity.DOUBAN:
                url = "https://m.douban.com/search/?query=";
                break;
            case BigbangActivity.DIANPING:
                url = "https://m.dianping.com/shoplist/2/search?from=m_search&keyword=";
                break;
            case BigbangActivity.TAOBAO:
                url = "https://s.m.taobao.com/h5?q=";
                break;
            case BigbangActivity.BAIKE:
                //url = "https://wapbaike.baidu.com/item/";
                url = "https://m.baidu.com/s?word=";
                break;
            default:
                url = "https://m.baidu.com/s?word=";
                break;

        }
        try {
            return url + URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return url + text;
    }
}
