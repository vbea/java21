package com.vbea.java21.bridge;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.net.Uri;
import android.os.Build;
import android.os.UserHandle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.ShellUtils;
import com.vbes.util.VbeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Vbe on 2022/12/30.
 */
public class Spam3 extends BaseActivity {
    @Override
    protected void before() {
        setContentView(R.layout.welcome);
    }

    @Override
    protected void after() {
        /*VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                VbeUtil.startActivityOptions(Spam3.this, AdultPreview.class);
            }
        }, 2000);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            start();
        }
    }

    private void test() {
        Uri data = getIntent().getData();
        String action = getIntent().getAction();
        //alipay 96
        String cmd = "am start --user 96";
        if (!VbeUtil.isNullOrEmpty(action)) {
            cmd += " -a " + action;
        }
        if (data != null) {
            cmd += " -d \"" + data.toString() + "\"";
        }
        cmd += " -n \"com.eg.android.AlipayGphone/com.alipay.mobile.quinox.SchemeLauncherActivity\"";
        Log.i("跳转分身", cmd);
        //Common.execCmd(cmd);
        final String cmdd = cmd;
        VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult result = ShellUtils.execCommand(cmdd, false);
                Log.i("执行结果", result.toString());
            }
        }, 500);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start() {
        Intent intent = new Intent();
        intent.setAction(getIntent().getAction());
        intent.setData(getIntent().getData());
        intent.setClassName("com.eg.android.AlipayGphone", "com.alipay.mobile.quinox.SchemeLauncherActivity");
        startActivityAsUser(intent, 96);
        VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                finishAndRemoveTask();
            }
        }, 1000);
    }
}
