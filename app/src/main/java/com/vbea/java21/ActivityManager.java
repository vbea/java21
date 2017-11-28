package com.vbea.java21;

import java.util.Stack;
import android.app.Activity;

public class ActivityManager
{
	private static Stack<Activity> activityStack;
	private static ActivityManager instance;
	private ActivityManager(){}
	
	public static ActivityManager getInstance()
	{
		if(instance==null)
			instance=new ActivityManager();
		return instance;
	}
	
	public void finishActivity()
	{ 
		Activity activity = activityStack.lastElement(); 
		if(activity != null)
		{
			activity.finish();
			activity = null;
		}
	}
	
	public void finishActivity(Activity activity)
	{
		if(activity != null)
		{
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}
	
	public Activity currentActivity()
	{ 
		Activity activity = activityStack.lastElement();
		return activity;
	}
	
	public void startActivity(Activity activity)
	{ 
		if(activityStack == null)
			activityStack=new Stack<Activity>(); 
		activityStack.add(activity); 
	} 

	public void FinishAllActivitiesButThis(Class cls)
	{ 
		while(true)
		{ 
			Activity activity=currentActivity(); 
			if (activity==null)
				break; 
			if (activity.getClass().equals(cls))
				break;
			finishActivity(activity); 
		} 
	}
	
	public void FinishAllActivities()
	{
		for (Activity act : activityStack)
		{
			if (act != null && !act.isFinishing())
				act.finish();
		}
	}
}
