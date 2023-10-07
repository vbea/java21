package com.vbea.java21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.ActivityManager;
import com.vbea.java21.ui.MyThemes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class BaseActivity extends com.vbes.util.ui.BaseActivity {

    @Override
    public void setTheme() {
        Common.start(getApplicationContext());
        setTheme(MyThemes.getTheme());
    }

    @Override
    public void renderView() {
        before();
        after();
    }

    protected abstract void before();

    protected abstract void after();

    public <T extends View> T bind(int id) {
        //return (T)findViewById(id);
        return getView(id);
    }

    protected void onFinish() {
        supportFinishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ActivityManager.getInstance().removeActivity(this);
        } catch (Exception | Error e) {
            ExceptionHandler.log("Base:onDestroy", e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void startActivityAsUser(Intent intent, int userId) {
        try {
            Method[] methods = this.getBaseContext().getClass().getSuperclass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("startActivityAsUser") && method.getParameterTypes().length == 2) {
                    method.setAccessible(true);
                    Log.i("开始执行", "-------------\n\n\n\n\n\n\n-------" + method.getName());
                    UserHandle userHandle = UserHandle.getUserHandleForUid(userId);
                    Field f = userHandle.getClass().getDeclaredField("mHandle");
                    f.setAccessible(true);
                    f.set(userHandle, userId);
                    Log.i("多用户", userHandle.toString());
                    method.invoke(this.getBaseContext(), intent, userHandle);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
