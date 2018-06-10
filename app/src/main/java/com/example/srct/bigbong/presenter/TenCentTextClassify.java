package com.example.srct.bigbong.presenter;

import android.util.Log;

import com.qcloud.Module.Wenzhi;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Utilities.Json.JSONObject;

import org.json.JSONArray;

import java.util.TreeMap;

/**
 * Created by srct on 2018/5/15.
 */

public class TenCentTextClassify {
    private final String TAG = "TenCentTextClassify";
    private Callback mCallback;

    public void setCallback(Callback cb) {
        mCallback = cb;
    }
    public void startTextClassify(final String text) {
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
                params.put("content", text);

                String result = null;
                try {
			        /* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
                    result = module.call("TextClassify", params);
                    JSONObject json_result = new JSONObject(result);
                    com.qcloud.Utilities.Json.JSONArray jsonArray = json_result.getJSONArray("classes");
                    for(int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject job = jsonArray.getJSONObject(i);
                        int num = (Integer)job.get("class_num");
                        if(num != 0 ) {
                           // num[0] = (Integer) job.get("class_num");
                            mCallback.onTextClassify(num);
                            break;
                        }
                    }

                    Log.d(TAG,"text ="+text+"json_result="+json_result);
                } catch (Exception e) {
                    Log.d(TAG,"error..."+e.getMessage());
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

    public static interface Callback {
        void onTextClassify(int class_num);
    }


}
