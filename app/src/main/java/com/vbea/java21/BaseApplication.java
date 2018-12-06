package com.vbea.java21;

import android.app.Application;
import android.content.Context;
//import android.support.multidex.MultiDex;

import com.vbea.java21.classes.ExceptionHandler;

public class BaseApplication extends Application
{

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
	}
	
	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
		//MultiDex.install(base);
		ExceptionHandler.getInstance().init();
	}
}
