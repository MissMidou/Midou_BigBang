package com.example.srct.bigbong;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srct on 2018/5/8.
 */

public class BigBangApplication extends Application {
    private final String LOG_TAG = "BigBangApplication";
    private final String  DICT_PATH = "jieba.dict.utf8";
    private final String HMM_PATH = "hmm_model.utf8";
    private final String  USER_DICT_PATH = "user.dict.utf8";
    private final String  IDF_PATH = "idf.utf8";
    private final String  STOP_WORD_PATH = "stop_words.utf8";

    private final String TARGET_PATH = "/data/data/com.example.srct.bigbong/";
    private List<String> dicList = new ArrayList<String>();

    private static BigBangApplication instance;

    public static BigBangApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "lzh onCreate");
        instance = this;
        initDic();
//        for(String s : dicList) {
//            try {
//                Log.w(LOG_TAG, "lzh copyFile dstFile =" + TARGET_PATH + s);
//                InputStream is = getAssets().open(s);
//                copyFile(is ,TARGET_PATH + s);
//
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "lzh ", e);
//                e.printStackTrace();
//            }
//        }
        LoadDictAsyncTask mLoadDictAsyncTask = new LoadDictAsyncTask();
        mLoadDictAsyncTask.execute();
//        loadDictFromJNI(TARGET_PATH);
//        String text = "我是拖拉机学院手扶拖拉机专业的。不用多久，我就会升职加薪，当上CEO，走上人生巅峰。";
//        Log.d(LOG_TAG, "lzh getKeywordFromJNI(text)="+getKeywordFromJNI(text));


    }
    private void initDic() {
        dicList.add(DICT_PATH);
        dicList.add(HMM_PATH);
        dicList.add(USER_DICT_PATH);
        dicList.add(IDF_PATH);
        dicList.add(STOP_WORD_PATH);
    }

    void copyFile(InputStream inputStream, String dstFile) throws IOException {
        Log.w(LOG_TAG, "copyFile dstFile =" + dstFile);
        File outputFile = new File(dstFile);

        if (inputStream == null) {
            Log.w(LOG_TAG, "InputStream is null...");
            return;
        }


        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        if (!outputFile.canWrite()) {
            outputFile.setWritable(true);
        }

        FileOutputStream output = new FileOutputStream(outputFile);
        byte [] buff = new byte[1024];
        int readSize;

        try {
            while ((readSize = inputStream.read(buff)) > 0) {
                output.write(buff, 0, readSize);
            }
            output.flush();
        } finally {
            inputStream.close();
            output.close();
        }
    }
    public String getKeyword(String text) {
        return getKeywordFromJNI(text);
    }
    public native void loadDictFromJNI(String dicPath);
    public native String getKeywordFromJNI(String text);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    class LoadDictAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for(String s : dicList) {
                try {
                    Log.w(LOG_TAG, "copyFile dstFile =" + TARGET_PATH + s);
                    InputStream is = getAssets().open(s);
                    copyFile(is ,TARGET_PATH + s);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            loadDictFromJNI(TARGET_PATH);
            return null;
        }
    }


}
