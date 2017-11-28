package com.vbea.java21.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomTextView extends TextView
{
	private boolean isActive = true;
    /*public final static String TAG = CustomTextView.class.getSimpleName();

    private float textLength = 0f;//文本长度
    private float viewWidth = 0f;
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting = false;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text = "";//文本内容
	//private int colors = 0xff000000;*/

    public CustomTextView(Context context)
	{
    	super(context);
		init();
    }

    public CustomTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
    }

	@Override
	public boolean isFocused()
	{
		return isActive;
	}
	
	public void init()
	{
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.invalidate();
	}
	
	public void start()
	{
		isActive = true;
		init();
	}
	
	public void pause()
	{
		isActive = false;
	}
}
