package com.example.srct.bigbong.view.impl;

/**
 * Created by srct on 2018/3/15.
 */
import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.srct.bigbong.R;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class WordsLayout {
    private final String TAG = "WordsLayout";
    private final int mMaxRowNumber;
    private final int mBoomPageWidth;
    private final int mWordMinWidth;
    private final int mWordBaseWidth;
    private final TextPaint mWordPaint;
    private ArrayList<Integer>  rowCount = new ArrayList<Integer>();
    private RangeList<Word> mWords = new RangeList<Word>();
    private class RangeList<E> extends ArrayList<E> {
        public void remove(int fromIndex, int toIndex) {
            if (fromIndex < toIndex) {
                removeRange(fromIndex, toIndex);
            }
        }
    }
    private class Word {
        public final String word;
        public final int start;
        public final boolean punc;

        public Word(String w, int s, boolean isPunc) {
            word = w;
            start = s;
            punc = isPunc;
        }
    }
    public WordsLayout(Context context) {
        Resources res = context.getResources();
        final int displayWidth = res.getDisplayMetrics().widthPixels;
        mBoomPageWidth = displayWidth - res.getDimensionPixelSize(R.dimen.page_margin_left)
                - res.getDimensionPixelSize(R.dimen.page_margin_right);
        //mMaxRowNumber = displayWidth > 1080 ? 11 : 10;
        mMaxRowNumber = 1000;
        mWordMinWidth = res.getDimensionPixelSize(R.dimen.word_min_width);
        mWordBaseWidth = res.getDimensionPixelSize(R.dimen.word_base_width);
        mWordPaint = ((TextView) View.inflate(context, R.layout.word_layout, null)
                .findViewById(R.id.word)).getPaint();
    }
    public void initRowCount(){
        int count = 0;
        int remain = mBoomPageWidth;
        rowCount.clear();
        for (int i = 0; i < mWords.size(); ++i) {
            final int chipWidth = measureChip(i);
            if (remain > chipWidth) {
                remain -=chipWidth;
                count++;
            } else {
                if (count == 0) {
                    rowCount.add(1);
                } else {
                    rowCount.add(count);
                    count = 0;
                    remain = mBoomPageWidth;
                    i--;
                }

            }
        }
        if (count > 0) {
            rowCount.add(count);
        }
    }
    public int getRowCount() {
        return rowCount.size();
    }
    public int getColumnCount(int row) {
        return rowCount.get(row);
    }
    public String getWord(int index){
        for(int i=0;i<mWords.size();i++){
            Log.d(TAG,"mWords="+i+" "+mWords.get(index).word);
        }
        if(index >= mWords.size()) return " ";
        return mWords.get(index).word;
    }

    public int getNum() {
        return mWords.size();
    }

    private int measureChip(int index) {
        final Word word = mWords.get(index);
        return Math.max(mWordMinWidth, mWordBaseWidth + (int)mWordPaint.measureText(word.word));
    }
    public void initWords(List<String> list) {
        for(String str:list) {
            mWords.add(new Word(str, 0, false));
        }
    }
    public void initWords(String text, int[] result){
        int k=1;
        while (k<result.length && result[k]!=-1 ) {
            String subtext;
            int start = result[k-1];
            if (result[k-1]>=0) {
                subtext = text.substring(result[k - 1], result[k] + 1);
                Log.d(TAG,"subtext.charAt(0)"+subtext.charAt(0)+isChinesePunctuation(subtext.charAt(0)));
                if (isChinesePunctuation(subtext.charAt(0))){
                    k=k+2;
                    continue;
                }
                mWords.add(new Word(subtext, start, false));
                Log.d(TAG, "subtext=" + subtext + result[k - 1] + result[k]);
            }
            k=k+2;
        }

    }
    public void clearWords(){
        mWords.clear();
        rowCount.clear();

    }
    public boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }



}
