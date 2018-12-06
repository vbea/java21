package com.vbea.java21;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.vbea.java21.classes.Common;
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
			_title = null;
		}
		setSupportActionBar(toolbar);
        after();
    }

    protected abstract void before();
    protected abstract void after();

	public void setToolbarTitle(String t)
	{
		_title = t;
	}

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
        ActivityManager.getInstance().removeActivity(this);
    }
	
	protected void toastShortMessage(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	protected void toastLongMessage(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
