package com.vbea.java21.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vbea.java21.R;
import com.vbea.java21.classes.Common;

/**
 * Created by Vbe on 2019/2/21.
 */
public class DownloadBridge extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        if (Common.isRun()) {
            intent.setClass(this, DownloadFile.class);
        } else {
            intent.setAction(Intent.ACTION_PICK_ACTIVITY);
            intent.setClass(this, HtmlViewer.class);
        }
        startActivity(intent);
        finish();
    }
}
