package com.vbea.java21.widget;

import android.content.Context;
import android.webkit.WebView;
import android.util.AttributeSet;
import android.view.MotionEvent;

//import com.tencent.smtt.sdk.WebView;

public class TouchWebView extends WebView
{
	//private long lastTime = 0l;
	public TouchWebView(Context context)
	{
        super(context);
    }
 
    public TouchWebView(Context context, AttributeSet attrs)
	{
        super(context, attrs);
    }
 
    public TouchWebView(Context context, AttributeSet attrs, int defStyleAttr)
	{
        super(context, attrs, defStyleAttr);
    }
	
	/*public boolean onTouchEvent(MotionEvent event)
	{
        if (event.getAction() == MotionEvent.ACTION_UP)
		{
			long currentTime = System.currentTimeMillis();
			long time = currentTime - lastTime;
			if (time < 300)
			{
				lastTime = currentTime;
				return true;
			}
			else
				lastTime = currentTime;
        }
        return super.onTouchEvent(event);
    }*/
}
