package com.vbea.java21.bridge;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.ui.AdultPreview;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2022/12/30.
 */
public class Spam2 extends BaseActivity {
    @Override
    protected void before() {
        setContentView(R.layout.welcome);
    }

    @Override
    protected void after() {
        /*VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                VbeUtil.startActivityOptions(Spam2.this, AdultPreview.class);
            }
        }, 2000);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {
        Intent intent = new Intent();
        intent.setAction(getIntent().getAction());
        intent.setData(getIntent().getData());
        intent.setClassName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
        startActivityAsUser(intent, 96);
        VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                finishAndRemoveTask();
            }
        }, 1000);
    }
}
