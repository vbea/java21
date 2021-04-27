package com.vbea.java21;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.ActivityManager;
import com.vbea.java21.ui.MyThemes;

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
}
