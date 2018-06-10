package com.example.srct.bigbong.model.impl;

import android.accessibilityservice.AccessibilityService;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by srct on 2018/3/6.
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int type = accessibilityEvent.getEventType();
        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        Log.d(TAG,"lzh type="+type+",isDebuggable="+isDebuggable);
        switch (type) {
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                getText(accessibilityEvent);
                break;
            default:
                getText(accessibilityEvent);
                break;
        }
    }

    private void getText(AccessibilityEvent event) {
        AccessibilityNodeInfo info = event.getSource();

        List<CharSequence> textlist =  event.getText();
        if (textlist != null) {
            StringBuilder sb = new StringBuilder();
            for(CharSequence cs : textlist) {
                sb.append(cs);
            }
            String text = sb.toString();
            Log.d(TAG,"lzh textlist="+textlist+", text="+text);
        }



    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
