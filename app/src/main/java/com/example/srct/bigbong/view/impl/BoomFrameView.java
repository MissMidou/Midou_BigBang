package com.example.srct.bigbong.view.impl;

import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.srct.bigbong.R;

/**
 * Created by srct on 2018/3/22.
 */

public class BoomFrameView extends ViewGroup implements View.OnClickListener{

    private String TAG = "BoomFrameView";

    ImageView mSearchIcon;
    ImageView mTransIcon;
    ImageView mShareIcon;
    ImageView mCopyIcon;
    ImageView mBackground;
    Context mContext;
    private int mBoomButtonBottomHeight;
    private Handler mHandler;
    public static final int SEARCH = 1;
    public static final int TRANSLATE = 2;
    public static final int SHARE = 3;
    public static final int COPY = 4;

    public BoomFrameView(Context context) {
        super(context);
        initView(context);
    }

    public BoomFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoomFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mSearchIcon = new ImageView(context);
        mSearchIcon.setImageResource(R.drawable.boom_chips_all_search);
        mSearchIcon.setOnClickListener(this);

        mTransIcon = new ImageView(context);
        mTransIcon.setImageResource(R.drawable.boom_chips_all_dict);
        mTransIcon.setOnClickListener(this);

        mShareIcon = new ImageView(context);
        mShareIcon.setImageResource(R.drawable.boom_chips_all_share);
        mShareIcon.setOnClickListener(this);

        mCopyIcon = new ImageView(context);
        mCopyIcon.setImageResource(R.drawable.boom_chips_all_copy);
        mCopyIcon.setOnClickListener(this);

        mBackground = new ImageView(context);
        mBackground.setImageResource(R.drawable.boom_frame_view_bg);

        mBoomButtonBottomHeight = getResources().getDimensionPixelSize(R.dimen.boom_button_bottom_height);


        //setBackgroundResource(R.drawable.boom_frame_view_bg);
       // setBackgroundColor(getResources().getColor(R.color.test));
        addView(mBackground);
        addView(mSearchIcon);
        addView(mTransIcon);
        addView(mShareIcon);
        addView(mCopyIcon);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        for(int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(measureSpec, measureSpec);
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int h = height - mSearchIcon.getMeasuredHeight()/2 - mBoomButtonBottomHeight/2;
        Log.d(TAG,"lzh mBackground h="+h +"mBoomButtonBottomHeight="+mBoomButtonBottomHeight+"height="+height+"mSearchIcon.getMeasuredHeight()="+mSearchIcon.getMeasuredHeight());
        mBackground.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height /*+ mSearchIcon.getMeasuredHeight()*/);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int space = dp2px(5);
        Log.d(TAG,"onLayout "+i+" "+i1+" "+i2+" "+" "+i3);
        mSearchIcon.layout(space,  0, space + mSearchIcon.getMeasuredWidth(), mSearchIcon.getMeasuredHeight());
        mTransIcon.layout(space +  mSearchIcon.getMeasuredWidth(), 0, space + mTransIcon.getMeasuredWidth()*2, mTransIcon.getMeasuredHeight());
        mShareIcon.layout(space +  mSearchIcon.getMeasuredWidth()*2, 0, space + mShareIcon.getMeasuredWidth()*3, mShareIcon.getMeasuredHeight());
        mCopyIcon.layout(getMeasuredWidth() - space - mSearchIcon.getMeasuredWidth(), 0, getMeasuredWidth() - space, mShareIcon.getMeasuredHeight());

        mBackground.layout(0, mSearchIcon.getMeasuredHeight()/2, getMeasuredWidth(), getMeasuredHeight() - mBoomButtonBottomHeight/2);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG,"mFrameView onDraw");
    }

    @Override
    public void onClick(View view) {
        if(view == mSearchIcon) {
            mHandler.sendEmptyMessage(SEARCH);
        } else if (view == mTransIcon) {
            mHandler.sendEmptyMessage(TRANSLATE);
        } else if (view == mShareIcon) {
            mHandler.sendEmptyMessage(SHARE);
        } else if (view == mCopyIcon) {
            mHandler.sendEmptyMessage(COPY);
        }

    }

    private int dp2px(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }
}
