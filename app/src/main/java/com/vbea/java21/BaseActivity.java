package com.vbea.java21;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vbea.java21.classes.Common;

public abstract class BaseActivity extends AppCompatActivity
{
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        Common.start(getApplicationContext());
        setTheme(MyThemes.getTheme());
        super.onCreate(savedInstanceState);
        before();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        after();
    }

    protected abstract void before();
    protected abstract void after();

    public <T extends View> T bind(int id)
	{
        return (T)findViewById(id);
    }

    protected void enableBackButton()
	{
        //toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View view)
			{
				onFinish();
                supportFinishAfterTransition();
            }
        });
    }
	
	protected void onFinish()
	{
		
	}

    @Override
    protected void onDestroy()
	{
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
