package com.example.srct.bigbong.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srct.bigbong.BigBangApplication;
import com.example.srct.bigbong.R;
import com.example.srct.bigbong.presenter.TenCentKeyword;
import com.example.srct.bigbong.presenter.TenCentTextClassify;
import com.example.srct.bigbong.util.Constants;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srct on 2018/3/14.
 */

public class BigbangActivity extends Activity {
    private final String TAG = "BigbangActivity";
    private static final String SEGMENT_URL = "http://bigbang.sanjiaoshou.net/http";
    private WordsLayout mWordsLayout;
    private LinearLayout mBoomLayout;
    //BoomFrameView mFrameView;
    private BoomLayoutView mBoomLayoutView;
    private View mPopupView;
    private PopupWindow mPopupWindow;
    private View mParentView;
    private String mText;
    private TenCentTextClassify mTenCentTextClassify;
    private TenCentTextClassify.Callback mCallback;
    private TenCentKeyword mTenCentKeyword;
    private TenCentKeyword.Callback mKeywordCallback;
    private ImageView mPopupWindowIcon;
    private TextView mPopupWindowText;

    public static final int DOUBAN = 1;
    public static final int DIANPING = 2;
    public static final int TAOBAO = 3;
    public static final int BAIKE = 4;
    private int mKeywordType = BAIKE;
    private String mKeywordText = "";
    Handler handler = new Handler();

    private View.OnClickListener mPopWindowOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "lzh mPopWindowOnClickListener mKeywordType="+mKeywordType);
            Intent intent = new Intent(BigbangActivity.this, KeywordSearchActivity.class);
            intent.putExtra("text", mKeywordText);
            intent.putExtra("type", mKeywordType);
            startActivity(intent);

            mPopupWindow.dismiss();

        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String text = intent.getStringExtra("CONTENT");
        mText = text;
        Log.d(TAG,"lzh onNewIntent text="+text);
        segmentOnline(text);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bigbong_main);
        mWordsLayout = new WordsLayout(getApplicationContext());
        mBoomLayout = (LinearLayout)findViewById(R.id.boomlayout);
        //mFrameView = (BoomFrameView)findViewById(R.id.boomframeview);
        String text = getIntent().getStringExtra("CONTENT");
        mText = text;
        mBoomLayoutView = findViewById(R.id.boomlayoutview);
        mPopupView = getLayoutInflater().inflate(R.layout.keyword_popup,null);
        mPopupWindow  = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,false);

        mParentView = getLayoutInflater().inflate(R.layout.bigbong_main,null);
        mPopupWindowIcon = (ImageView)mPopupView.findViewById(R.id.icon);
        mPopupWindowIcon.setOnClickListener(mPopWindowOnClickListener);
        mPopupWindowText = (TextView)mPopupView.findViewById(R.id.keywords);
        mPopupWindowText.setOnClickListener(mPopWindowOnClickListener);
        mPopupWindow.setAnimationStyle(R.style.PopupAnimation);

        mTenCentTextClassify = new TenCentTextClassify();
        mCallback = new TenCentTextClassify.Callback() {
            @Override
            public void onTextClassify(int class_num) {
                Log.d(TAG, "lzh onTextClassify class_num=" + class_num);
                if (class_num == Constants.E_PTC_CATEGORY_MUSIC || class_num == Constants.E_PTC_CATEGORY_MOVIE || class_num == Constants.E_PTC_CATEGORY_LITERATURE
                    || class_num == Constants.E_PTC_CATEGORY_MINGXING || class_num == Constants.E_PTC_CATEGORY_MOVIE_TVPLAY || class_num == Constants.E_PTC_CATEGORY_MOVIE_SHOW) {

                    mKeywordType = DOUBAN;
                    Log.d(TAG, "onTextClassify mKeywordType=" + mKeywordType);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindowIcon.setImageResource(R.drawable.douban);
                            mPopupWindow.showAtLocation(new View(BigbangActivity.this), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);

                        }
                    });
                } else if (class_num == Constants.E_PTC_CATEGORY_FOOD) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindowIcon.setImageResource(R.drawable.dianping);
                            mPopupWindow.showAtLocation(new View(BigbangActivity.this), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);

                        }
                    });

                    mKeywordType = DIANPING;
                } else if (class_num == Constants.E_PTC_CATEGORY_RETAILSHOP || class_num == Constants.E_PTC_CATEGORY_TECHNOLOGY_DIG || class_num == Constants.E_PTC_CATEGORY_TECHNOLOGY_PHONE) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindowIcon.setImageResource(R.drawable.taobao);
                            mPopupWindow.showAtLocation(new View(BigbangActivity.this), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);

                        }
                    });

                    mKeywordType = TAOBAO;
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindowIcon.setImageResource(R.drawable.baike);
                            mPopupWindow.showAtLocation(new View(BigbangActivity.this), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);

                        }
                    });

                    mKeywordType = BAIKE;
                }
            }
        };
        mTenCentTextClassify.setCallback(mCallback);

        mTenCentKeyword = new TenCentKeyword();
        mKeywordCallback = new TenCentKeyword.Callback() {
            @Override
            public void onTextKeyword(String keyword) {
                Log.d(TAG, "onTextKeyword keyword=" + keyword);
                mKeywordText = keyword;
                mPopupWindowText.setText(keyword);
            }
        };
        mTenCentKeyword.setCallback(mKeywordCallback);

        segmentOnline(text);

    }
    private void showKeywordPopup(String text) {
//        TextView textView = (TextView)mPopupView.findViewById(R.id.keywords);
//        ImageView imageView = (ImageView)mPopupView.findViewById(R.id.icon);
       // String keyword = BigBangApplication.getInstance().getKeyword(text);
       // Log.d(TAG,"lzh2 showKeywordPopup keyword="+keyword);
       // textView.setText(keyword);
//        imageView.setImageResource(R.mipmap.ic_launcher);
        mTenCentKeyword.startFindKeyword(text);
        mTenCentTextClassify.startTextClassify(text);

//        mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
//        mPopupWindow.showAtLocation(new View(BigbangActivity.this), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);


    }

    private void segmentOnline(final String text) {

        if (text != null) {

            //   boolean ocr = (getIntent().getStringExtra("boom_image") != null);
            boolean ocr = false;
            Map<String, String> params = new HashMap<>(2);
            params.put("words", text);
            params.put("filter", ocr ? "1" : "0");
            com.example.srct.bigbong.newwork.OkHttpClientManager.getInstance().post(SEGMENT_URL, params,
                    new com.example.srct.bigbong.newwork.OkHttpClientManager.ResultCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                            String msg = "请检查网络";
                            Toast.makeText(BigbangActivity.this, msg, Toast.LENGTH_SHORT).show();
                            handleSegmentResultOffline(text);
                            //finish();
                        }

                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    int code = json.getInt("code");
                                    if (code != 0) {
                                        String msg = json.getString("msg");
                                        Log.d(TAG, msg);
                                        Toast.makeText(BigbangActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        finish();
                                        return;
                                    }
                                    JSONArray array = json.getJSONArray("list");
                                    if (array != null) {
                                        int[] result = new int[array.length()];
                                        for (int i = 0; i < array.length(); i++) {
                                            result[i] = array.getInt(i);
                                        }
                                        handleSegmentResult(text, result);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    private void handleSegmentResult(String text, int[] result) {

        int k=1;
        while(k<result.length && result[k]!=-1 ) {
            String subtext;
            subtext=text.substring(result[k-1],result[k]+1);
            Log.d(TAG,"subtext="+subtext+result[k-1]+result[k]);
            k++;
        }
        mWordsLayout.initWords(text, result);
        mWordsLayout.initRowCount();
        //showBoomWords();
        showSplitWords();
        mWordsLayout.clearWords();
    }

    private void showSplitWords() {
        mBoomLayoutView.clearView();
        int wordNum = mWordsLayout.getNum();
        for(int i=0; i<wordNum; i++) {
            String text = mWordsLayout.getWord(i);
            mBoomLayoutView.addText(text);
        }
       // mBoomLayoutView.addFrameView(mFrameView);
        mBoomLayoutView.setVisibility(View.VISIBLE);

        if(mText.length() > 1) {
            showKeywordPopup(mText);
        }

    }
    private void handleSegmentResultOffline(String text) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> tokens = segmenter.process(text,JiebaSegmenter.SegMode.INDEX);
        Log.d(TAG,"jieba"+tokens.toString());
        List<String> resultList = new ArrayList<String>();
        for (SegToken token : tokens) {
            resultList.add(token.word);

        }
        mWordsLayout.initWords(resultList);
        mWordsLayout.initRowCount();
        showSplitWords();
        mWordsLayout.clearWords();
    }
    private void showBoomWords() {
        clearLayout();
        int num = -1;
        for (int i = 0; i < mWordsLayout.getRowCount(); ++i) {
            final int count = mWordsLayout.getColumnCount(i);
            LinearLayout row = new LinearLayout(BigbangActivity.this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < count; ++j) {
                num++;
                View chipView = getLayoutInflater().inflate(R.layout.word_layout, null);
                String str = mWordsLayout.getWord(num);
                final TextView word = (TextView)chipView.findViewById(R.id.word);
                word.setText(str);
                word.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"lzh onClick " +word.isSelected());
                        if (word.isSelected()) {
                            word.setSelected(false);
                        } else {
                            word.setSelected(true);
                        }

                    }
                });
                row.addView(chipView);
            }
            mBoomLayout.addView(row);
        }


    }
    private void clearLayout() {
        mBoomLayout.removeAllViews();
    }
}
