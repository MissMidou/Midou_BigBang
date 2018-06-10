package com.example.srct.bigbong.view.impl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.srct.bigbong.R;
import com.example.srct.bigbong.model.impl.ClipboardListenerService;
import com.example.srct.bigbong.view.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 1;
//    private RecyclerView mRecyclerView= null;
    private TextView mGuideText = null;
    private TextView mAboutText = null;
    private  final int GUIDE_DIALOG = 1;
    private  final int ABOUT_DIALOG = 2;
    //List<CardView> mCardViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, ClipboardListenerService.class));
            } else {
                requestAlertWindowPermission();
            }
        } else {
            startService(new Intent(this, ClipboardListenerService.class));
        }

//        mRecyclerView = (RecyclerView)findViewById(R.id.list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(new HomeAdapter(getApplicationContext(), mCardViews));

        mGuideText = (TextView)findViewById(R.id.use);
        mGuideText.setOnClickListener(this);
        mAboutText = (TextView)findViewById(R.id.about);
        mAboutText.setOnClickListener(this);

    }

    private  void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, ClipboardListenerService.class));
            }
        }
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch(id) {
//            case GUIDE_DIALOG:
//                return creatGuideDialog(GUIDE_DIALOG);
//            case ABOUT_DIALOG:
//                return creatAboutDialog(ABOUT_DIALOG);
//            default:
//                return null;
//        }
//    }

     private Dialog creatGuideDialog() {
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setTitle(R.string.guide_alert_dialog_title);
         builder.setMessage(R.string.guide_alert_dialog_msg);
         builder.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
         AlertDialog dialog = builder.create();

         return dialog;
    }

    private Dialog creatAboutDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_alert_dialog_title);
        builder.setMessage(R.string.about_alert_dialog_msg);
        builder.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        return dialog;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.use:
                creatGuideDialog().show();
                break;
            case R.id.about:
                creatAboutDialog().show();
                break;
        }
    }
}
