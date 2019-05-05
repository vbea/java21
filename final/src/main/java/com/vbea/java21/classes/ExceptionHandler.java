package com.vbea.java21.classes;

import java.util.Hashtable;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.lang.Thread.UncaughtExceptionHandler;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.vbea.java21.Main;

public class ExceptionHandler
{
	private Context mContext;
	private static ExceptionHandler instance; //单例模式
	private ExceptionHandler(){}

	//锁定代码块
	public synchronized static ExceptionHandler getInstance()
	{
		if (instance == null)
		{
			instance = new ExceptionHandler();
		}
		return instance;
	}
	public void init(Context context)
	{
		this.mContext = context;
	}
	
	public static void log(String name, Exception e)
	{
		log(name, e.toString());
	}
	
	public static void log(String name, String msg)
	{

	}
	
	public static class KeyLog
	{
		String name;
		Hashtable<String, Object> table;
		public KeyLog(String n)
		{
			name = n;
			table = new Hashtable<String, Object>();
		}
		
		public int length()
		{
			return table.size();
		}
		
		public void add(String key, Object value)
		{
			table.put(key, value);
		}
		
		public void clear()
		{
			table.clear();
		}
		
		public void toLog()
		{
			ExceptionHandler.log(name, table.toString());
		}
	}
}
