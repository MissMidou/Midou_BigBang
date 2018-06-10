package com.example.srct.bigbong.view.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srct.bigbong.R;
import com.example.srct.bigbong.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srct on 2018/3/16.
 */

public class BoomLayoutView extends LinearLayout{

    private ArrayList<Integer> rows  = new ArrayList<Integer>();
    private ArrayList<WordInfo> mWordList  = new ArrayList<WordInfo>();
    private final String TAG = "BoomLayoutView";
    class WordInfo{
        int width;
        int height;
        TextView mTextView;
        int row;
        public WordInfo(TextView text) {
            mTextView = text;
            width = height = 0;
            row = 0;
        }

        public void setWidth(int w) {
            width = w;
        }

        public void setHeight(int h) {
            height = h;
        }

        int getWidth() {
            return width;
        }

        int getHeight() {
            return height;
        }

        TextView getTextView() {
            return mTextView;
        }

        public void setRow(int r) {
            row = r;
        }

        public int getRow() {
            return row;
        }
    }

    Context mContext;
    int mTextBackground;
    int mTextPadding;
    int mTextSize;
    int mTextColor;
    int mTextSpace;
    int mBoomButtonHeight;
    int mBoomButtonBottomHeight;

    BoomFrameView mFrameView;
    boolean isShowButton = false;


    Handler ButtonHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch(msg.what) {
                case BoomFrameView.SEARCH:
                    handleSearch();
                    break;
                case BoomFrameView.TRANSLATE:
                    handleTrans();
                    break;
                case BoomFrameView.COPY:
                    handleCopy();
                    break;
                case BoomFrameView.SHARE:
                    handleShare();
                    break;
            }
        }
    };

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public BoomLayoutView(Context context) {
        super(context);
    }

    public BoomLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoomLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

//    public void addFrameView(BoomFrameView frameView) {
//        Log.d(TAG, "lzh addFrameView");
//        mFrameView = frameView;
//        mFrameView.setVisibility(View.VISIBLE);
//        requestLayout();
//        invalidate();
//        addView(mFrameView);
//    }

    void initView(Context context) {
        mContext = context;
       // mTextBackground = mContext.getResources().getDrawable(R.drawable.boom_chips_bg);
        mTextPadding = (int)mContext.getResources().getDimension(R.dimen.boom_chip_text_pending);
        mTextSize = (int)px2dp(mContext.getResources().getDimension(R.dimen.boom_chip_text_size));
        mTextColor = mContext.getResources().getColor(R.color.boom_chip_text_color);
        mTextSpace = (int)px2dp(mContext.getResources().getDimension(R.dimen.boom_chip_text_space));
        mBoomButtonHeight = getResources().getDimensionPixelSize(R.dimen.boom_button_height);
        mBoomButtonBottomHeight = getResources().getDimensionPixelSize(R.dimen.boom_button_bottom_height);

        mFrameView = new BoomFrameView(context);
        mFrameView.setVisibility(View.GONE);
        mFrameView.setHandler(ButtonHandler);
        addView(mFrameView);
    }

    public void addText(String text) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setBackgroundResource(R.drawable.boom_chips_bg);
        textView.setTextColor(mTextColor);
        textView.setTextSize(mTextSize);
        textView.setPadding(mTextPadding,mTextPadding,mTextPadding,mTextPadding);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine(true);
        textView.setPadding(mTextPadding, mTextPadding,mTextPadding, mTextPadding);
        WordInfo wordInfo = new WordInfo(textView);
        mWordList.add(wordInfo);
        Log.d(TAG, "addText text="+text+" mWordList "+mWordList.size());
        addView(textView);

    }


    public void clearView(){
        int childCount = getChildCount();
        List<TextView> textList = new ArrayList<TextView>();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child instanceof TextView) {
                textList.add((TextView)child);
               // removeView(child);

            }

        }
        for(int i = 0; i < textList.size(); i++) {
            removeView(textList.get(i));
        }

        mWordList.clear();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        Log.d(TAG,"onMeasure getPaddingLeft()="+getPaddingLeft()+" getPaddingRight()="+getPaddingRight()+"MeasureSpec.getSize(widthMeasureSpec)"+MeasureSpec.getSize(widthMeasureSpec));
        int heightSize = 0;
        int remainWidth = widthSize+ mTextSpace;
        rows.clear();
        int columnNum = 0;
        int childHeight = 0;
        int wordNum = 0;
        for (int i = 1; i < childCount; i++) {
            View child = getChildAt(i);
            wordNum = i-1;
            if(child == mFrameView) {
                continue;
            }
            child.setVisibility(VISIBLE);
            child.measure(measureSpec, measureSpec);
            childHeight = child.getMeasuredHeight();
            mWordList.get(wordNum).setHeight(child.getMeasuredHeight());
            mWordList.get(wordNum).setWidth(child.getMeasuredWidth());
            //Log.d(TAG,"lzh onMeasure i="+i+" w ="+child.getMeasuredWidth()+" h="+child.getMeasuredHeight()+mWordList.get(i).getTextView().getText());
            if(remainWidth >= (child.getMeasuredWidth() + mTextSpace)) {
                remainWidth -= (child.getMeasuredWidth() + mTextSpace);
                columnNum++;
            } else {
                //if(columnNum == 0) {
                //    rows.add(1);
               // } else {
                    rows.add(columnNum);
                    columnNum = 0;
                    remainWidth = widthSize + mTextSpace;
                    i--;
                //}
                heightSize += child.getMeasuredHeight();
            }
            child.setBackgroundResource(R.drawable.boom_chips_bg);
            child.setPadding(mTextPadding, mTextPadding,mTextPadding, mTextPadding);
            mWordList.get(wordNum).setRow(rows.size());

        }
        if(columnNum > 0) {
            rows.add(columnNum);
            for(int n = wordNum; n > (wordNum - columnNum);n--) {
                mWordList.get(n).setRow(rows.size()-1);
            }
        }

        int offest = 0;
        if(mFrameView != null) {
            int firstRow = getfirstRow();
            int lastRow = getLastRow();

            if (lastRow != -1 && firstRow != -1) {
                int h = (lastRow - firstRow + 1) * (childHeight) + mBoomButtonHeight + mBoomButtonBottomHeight;
                mFrameView.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
                mFrameView.setVisibility(View.VISIBLE);
                isShowButton = true;
                offest = mBoomButtonHeight + mBoomButtonBottomHeight;
            } else {
                mFrameView.setVisibility(View.GONE);
                isShowButton = false;
            }
        }
        int height = heightSize + getPaddingTop() + getPaddingBottom() + childHeight + offest;
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private int getfirstRow() {
        for(int i=0; i < mWordList.size(); i++) {
            if(mWordList.get(i).getTextView().isSelected()) {
                return mWordList.get(i).getRow();
            }
        }
        return -1;
    }

    private int getLastRow() {
        int last = -1;
        for(int i=0; i < mWordList.size(); i++) {
            if(mWordList.get(i).getTextView().isSelected()) {
                last = mWordList.get(i).getRow();
            }
        }
        return last;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);

        int top = getPaddingTop();
        int left = getPaddingLeft();
        int num = -1;
        int firstRow = getfirstRow();
        int lastRow = getLastRow();
        int offest = 0;
        for(int i = 0; i < rows.size(); i++) {
            left = getPaddingLeft();
            if (i == firstRow) {
                offest = mBoomButtonHeight;
            }
            if (lastRow != -1 && (i - 1) == lastRow) {
                offest += mBoomButtonBottomHeight;
            }
            top = i * mWordList.get(0).height + offest;
            for(int c = 0; c < rows.get(i); c++) {
                num++;
                TextView child = mWordList.get(num).getTextView();
                child.layout(left, top, left + mWordList.get(num).getWidth(), top + mWordList.get(num).getHeight());
                child.setBackgroundResource(R.drawable.boom_chips_bg);
                left += mWordList.get(num).getWidth() + mTextSpace;

            }
        }
        if( mFrameView!= null) {
            int wordHeight = 0;
            if(mWordList.size() > 0) {
                wordHeight = mWordList.get(0).height;
            }
            if (lastRow != -1 && firstRow != -1) {
                Log.d(TAG,"mFrameView onLayout l="+getPaddingLeft()+" t="+firstRow * wordHeight+"r="+getPaddingLeft() + mFrameView.getMeasuredWidth()+" b="+(lastRow + 1) * wordHeight);
                mFrameView.layout(getPaddingLeft(), firstRow * wordHeight, getPaddingLeft() + mFrameView.getMeasuredWidth(), (lastRow + 1)  * wordHeight + mBoomButtonHeight + mBoomButtonBottomHeight);
            }
        }
    }

    private int dp2px(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    private float px2dp(float px){
        return px / mContext.getResources().getDisplayMetrics().density;
    }

    private WordInfo curWord = null;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // return super.onTouchEvent(event);
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                for(WordInfo word : mWordList) {
                    Rect rect = new Rect();
                    word.getTextView().getHitRect(rect);
                    if(rect.contains(x,y) && word != curWord) {
                        curWord = word;
                        boolean isSelected = word.getTextView().isSelected();
                        word.getTextView().setSelected(!isSelected);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                curWord = null;
                requestLayout();
                invalidate();
                break;
        }
        return true;

    }

    private String getSelectedText() {
        String text = "";
        for(int i=0; i < mWordList.size(); i++) {
            if(mWordList.get(i).getTextView().isSelected()) {
                text += mWordList.get(i).getTextView().getText();
            }
        }
        return text;
    }

    private void handleSearch() {
        String text = getSelectedText();
        Intent intent = new Intent(mContext, BaiduSearchActivity.class);
        intent.putExtra("text", text);
        mContext.startActivity(intent);
    }

    private void handleShare() {
        String text = getSelectedText();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.removeExtra(android.content.Intent.EXTRA_TEXT);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(sharingIntent, "分享"));
    }

    private void handleTrans() {
        String text = getSelectedText();
        Intent intent = new Intent(mContext, TranslateActivity.class);
        intent.putExtra("TRANS", text);
        mContext.startActivity(intent);

    }

    private void handleCopy() {
        String text = getSelectedText();
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("simple text",text);
        cm.setPrimaryClip(cd);
        String msg = "已复制到剪贴板";
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

        Constants.isCopyFromBigBang = true;
    }
}
