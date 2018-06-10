package com.example.srct.bigbong.presenter;

import android.util.Log;

import com.example.srct.bigbong.BigBangApplication;
import com.qcloud.Module.Wenzhi;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Utilities.Json.JSONObject;

import java.util.TreeMap;

/**
 * Created by srct on 2018/5/15.
 */

public class TenCentKeyword {
    private final String TAG = "TenCentKeyword";
    private Callback mCallback;

    public void setCallback(Callback cb) {
        mCallback = cb;
    }
    public static interface Callback {
        void onTextKeyword(String keyword);
    }
    public void startFindKeyword(final String text) {
        //final int[] num = new int[1];
        Thread HttpRequestThread = new Thread(new HttpRequestRunnable(text){
            @Override
            public void run() {
                TreeMap<String, Object> config = new TreeMap<String, Object>();
                config.put("SecretId", "AKIDPiqnaCfCC7rPlvMgE1scAXnsnZFj871j");
                config.put("SecretKey", "fBaZVaWN2UnupY6UIzZBdt486vb8TVSD");
                config.put("RequestMethod", "GET");
                config.put("DefaultRegion", "tj");


                QcloudApiModuleCenter module = new QcloudApiModuleCenter(new Wenzhi(),
                        config);

                TreeMap<String, Object> params = new TreeMap<String, Object>();
                params.put("title", text);
                params.put("content", text);

                String result = null;
                String word;
                try {
			        /* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
                    result = module.call("TextKeywords", params);
                    JSONObject json_result = new JSONObject(result);
                    com.qcloud.Utilities.Json.JSONArray jsonArray = json_result.getJSONArray("keywords");

                    if (jsonArray == null) {
                        word  = BigBangApplication.getInstance().getKeyword(text);
                    } else {
                        JSONObject job = jsonArray.getJSONObject(0);
                        word = (String)job.get("keyword");
                    }


                    mCallback.onTextKeyword(word);


                } catch (Exception e) {
                    word  = BigBangApplication.getInstance().getKeyword(text);
                    mCallback.onTextKeyword(word);
                    Log.d(TAG,"error..."+e.getMessage()+ "word="+word);
                }
            }
        });
        HttpRequestThread.start();
        // return num[0];
    }
    abstract class HttpRequestRunnable implements Runnable {
        String mText;
        HttpRequestRunnable(String text) {
            mText = text;
        }
    }
}
