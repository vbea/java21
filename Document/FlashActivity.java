package com.binxin.zdapp

import android.app.Activity;

public class FlashActivity extends Activity
{
	static private Camera camera = null;  
	private Parameters parameters = null; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);//布局里只有两个按钮，开启和关闭
		ctx = this;
	}
	//主开启
	public void start(View v)
	{
		handler.post(startThread);
		handler.post(closeThread);
	}
	//关闭
	public void close(View v)
	{
		handler.removeCallbacks(startThread);
		handler.removeCallbacks(closeThread);
		flashclose();
		camera.stopPreview();
		camera.release();
		camera=null;
	}
	private void flashopen()
	{
		if(camera==null)
		{
			camera = Camera.open(); 
		}
		parameters = camera.getParameters();  
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
		camera.startPreview();
	}  
	private void flashclose()
	{
		if(camera==null)
		{
			camera = Camera.open(); 
		}
		parameters = camera.getParameters();  
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(parameters);
	}
	Runnable startThread = new Runnable()
	{  
		//将要执行的操作写在线程对象的run方法当中  
		public void run()
		{  
			System.out.println("updateThread");  
			flashopen();
			try
			{
				Thread.sleep(100);
				flashclose();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			handler.post(startThread);
		}
	}; 
	Runnable closeThread = new Runnable()
	{  
		//将要执行的操作写在线程对象的run方法当中  
		public void run()
		{  
			System.out.println("updateThread");  
			flashclose();	
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			handler.post(closeThread);
		}
	};
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
		}
	};
}