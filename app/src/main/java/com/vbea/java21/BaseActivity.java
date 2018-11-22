package com.vbea.java21;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.MyActivityManager;

public abstract class BaseActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public boolean hasFix = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyThemes.getTheme());
        super.onCreate(savedInstanceState);
        before();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        after();
    }

    public abstract void before();
    public abstract void after();

    public <T extends View> T bind(int id) {
        return (T)findViewById(id);
    }

    public void enableBackButton() {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityManager.getInstance().finishActivity(this);
    }
}
