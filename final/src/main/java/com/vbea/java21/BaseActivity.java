package com.vbea.java21;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;

public abstract class BaseActivity extends AppCompatActivity
{
    protected Toolbar toolbar;
	private String _title;
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        Common.start(getApplicationContext());
        setTheme(MyThemes.getTheme());
        super.onCreate(savedInstanceState);
        before();
        toolbar = bind(R.id.toolbar);
		if (!Util.isNullOrEmpty(_title)) {
			toolbar.setTitle(_title);
			_title = "";
		}
		setSupportActionBar(toolbar);
        after();
    }

    protected abstract void before();
    protected abstract void after();

	public void setToolbarTitle(String t)
	{
		if (toolbar == null)
			_title = t;
		else
			toolbar.setTitle(t);
	}

    public <T extends View> T bind(int id)
	{
        //return (T)findViewById(id);
		return findViewById(id);
    }

    protected void enableBackButton()
	{
		super.
        //toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View view)
			{
				onFinish();
            }
        });
    }
	
	protected void onFinish()
	{
		supportFinishAfterTransition();
	}

    @Override
    protected void onDestroy()
	{
        super.onDestroy();
        try {
			ActivityManager.getInstance().removeActivity(this);
		} catch (Exception | Error e) {
			ExceptionHandler.log("Base:onDestroy", e.toString());
		}
    }
	
	protected void toastShortMessage(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	protected void toastLongMessage(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	protected void toastShortMessage(int message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	protected void toastLongMessage(int message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
