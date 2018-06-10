package com.example.srct.bigbong.model.impl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.srct.bigbong.R;
import com.example.srct.bigbong.util.Constants;
import com.example.srct.bigbong.view.impl.BigbangActivity;
import com.example.srct.bigbong.view.impl.FloatingIcon;
import com.example.srct.bigbong.view.impl.MainActivity;

/**
 * Created by srct on 2018/3/13.
 */

public class ClipboardListenerService  extends Service {
    @Nullable
    ClipboardManager mClipboardManager;

    private final String TAG = "ClipboardListenerService";
    FloatingIcon mFloatingIcon = null;
    Context mContext = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mFloatingIcon = new FloatingIcon(mContext);
        mClipboardManager =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                CharSequence copyText = getClipboardContent();
                Intent intent = new Intent(mContext, BigbangActivity.class);
                intent.putExtra("CONTENT", copyText.toString());
                if(!Constants.isCopyFromBigBang) {
                    mFloatingIcon.showFloatingIcon(intent);
                } else {
                    Constants.isCopyFromBigBang = false;
                }
            }
        });

    }
    public CharSequence getClipboardContent() {
        if(mClipboardManager.getPrimaryClip().getItemCount()>0) {
            ClipData.Item item = mClipboardManager.getPrimaryClip().getItemAt(0);
            Log.d(TAG, "item.getText()="+item.getText());

            return item.getText();

        }
        return "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent contentIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0 , contentIntent, 0)).setContentTitle("Bigbang").
                setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)).
                setContentText("Bigbang 前台服务").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher_round);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(100,notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
