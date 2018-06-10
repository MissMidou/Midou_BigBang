package com.example.srct.bigbong.view.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srct.bigbong.R;
import com.example.srct.bigbong.util.TranslateData;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srct on 2018/4/23.
 */

public class TranslateActivity extends Activity {
    private final String LOG_TAG = "TranslateActivity";
    TranslateParameters tps;
    Translator translator;
    Handler handler = new Handler();
    private List<TranslateData> list = new ArrayList<TranslateData>();

    private List<Translate> trslist = new ArrayList<Translate>();
    private Context mContext;

    private EditText mText;
    private TextView mTransText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        String text = getIntent().getStringExtra("TRANS");
        mContext = getApplicationContext();
        mText = (EditText)findViewById(R.id.text);
        mText.setText(text);

        mTransText = (TextView)findViewById(R.id.transtext);

        YouDaoApplication.init(this,"475a37d8ec9f6706");
        Language langFrom = LanguageUtils.getLangByName("中文");
        Language langTo = LanguageUtils.getLangByName("英文");
        tps = new TranslateParameters.Builder().source("ydtranslate-demo").from(langFrom).to(langTo).build();
        translator = Translator.getInstance(tps);
        startTrans(text);
    }
    private void  startTrans(String input) {
        final long start = System.currentTimeMillis();
        // result TranslateErrorCode lookup "requestId" sdk null
        translator.lookup(input, "475a37d8ec9f6706", new TranslateListener() {

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                Log.d(LOG_TAG,"midou onError ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "查询错误:" + error.name(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResult(final Translate result, final String input, String requestId) {
                Log.d(LOG_TAG,"midou onResult");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateData td = new TranslateData(
                                System.currentTimeMillis(), result);

                        long end = System.currentTimeMillis();
                        long time = end-start;
                        Log.i("1111111111111111","111111111查词时间"+time);

                        list.add(td);
                        trslist.add(result);
                        //ToastUtils.show(mContext,"result:" + td.means()+td.translates());
                       // Toast.makeText(mContext, "input:"+input+"result:" + td.means(), Toast.LENGTH_SHORT).show();
                        mTransText.setText(td.translates());
                        Log.d(LOG_TAG,"midou onResult list="+list+" trslist="+trslist);

                    }
                });

            }

            @Override
            public void onResult(List<Translate> list, List<String> list1, List<TranslateErrorCode> list2, String s) {
                Log.d(LOG_TAG,"lzh onResult list1="+list1+" list2="+list2+" s="+s);


            }


        });

    }
}
