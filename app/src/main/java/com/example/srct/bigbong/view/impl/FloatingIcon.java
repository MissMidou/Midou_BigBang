package com.example.srct.bigbong.view.impl;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.srct.bigbong.R;

/**
 * Created by srct on 2018/3/14.
 */

public class FloatingIcon {

    private LinearLayout mFloatLayout;
    private Button mFloatView;
    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private Context mContext;
    private int btnPosX = 0;
    private int btnPosY = 0;
    private Intent mBigbongIntent;
    private int mScreenWidth;

    private final String TAG = "FloatingIcon";
    public  FloatingIcon(Context context) {
        mContext = context;
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        mScreenWidth = dm.widthPixels;

    }

    public void showFloatingIcon(Intent bigbangIntent) {
        mBigbongIntent = bigbangIntent;
        if(mFloatLayout == null) {
            initFloatView();
        } else {
            return;
        }
        Log.d(TAG,"lzh showFloatingIcon bigbangIntent="+bigbangIntent.getStringExtra("CONTENT"));
        setFloatButtonWindowParams(btnPosX,btnPosY);
        mWindowManager.addView(mFloatLayout, wmParams);
        Animation alphaAni = AnimationUtils.loadAnimation(mContext, R.anim.button_fadein);
        mFloatView.startAnimation(alphaAni);


    }

    public void initFloatView() {

        mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSPARENT);
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                btnPosX = wmParams.x = mScreenWidth - (int) event.getRawX() -  mFloatView.getMeasuredWidth();
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                //减25为状态栏的高度
                btnPosY = wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() - 25;
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                //刷新
                if (mFloatLayout != null)
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });
        mFloatView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mContext.startActivity(mBigbongIntent);
                removeView();
            }
        });
    }

    public void setFloatButtonWindowParams(int x, int y) {
        wmParams.x = x;
        wmParams.y = y;

    }
    public void removeView() {
        if(mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout =null;
        }
    }
}
