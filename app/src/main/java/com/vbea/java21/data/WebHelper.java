package com.vbea.java21.data;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;

/**
 * 浏览器书签和历史记录类
 * 邠心工作室
 * 2018.02.01
 */
public class WebHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "WebView.db";
	public static final int DATA_VERSION = 1;
	public static final String TABLE_BOOK = "bookmark";
	public static final String TABLE_HISTORY = "history";
	private Context context;
	public WebHelper(Context c)
	{
		super(c, DATABASE_NAME, null, DATA_VERSION);
		context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.execSQL("CREATE TABLE " + TABLE_BOOK + " (id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,url TEXT NOT NULL,createOn TEXT NOT NULL)");
			db.execSQL("CREATE TABLE " + TABLE_HISTORY + " (id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,url TEXT NOT NULL,createOn TEXT NOT NULL)");
		}
		catch (Exception e)
		{
			ExceptionHandler.log("create_table",e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		try
		{
			db.execSQL("drop table " + TABLE_BOOK);
			db.execSQL("drop table " + TABLE_HISTORY);
			onCreate(db);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("upgrade_database", e.toString());
		}
	}

	public long addHistory(String title, String url)
	{
		SQLiteDatabase db = getWritableDatabase();
		try
		{
			ContentValues map = new ContentValues();
			map.put("title", title);
			map.put("url", url);
			map.put("createOn", System.currentTimeMillis());
			return db.insert(TABLE_HISTORY, null, map);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("sql_addHistory", e.toString());
		}
		finally
		{
			db.close();
		}
		return 0;
	}
	
	public long addBookmark(String name, String url)
	{
		SQLiteDatabase db = getWritableDatabase();
		try
		{
			ContentValues map = new ContentValues();
			map.put("name", name);
			map.put("url", url);
			map.put("createOn", System.currentTimeMillis());
			return db.insert(TABLE_HISTORY, null, map);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("sql_addBookmark", e.toString());
		}
		finally
		{
			db.close();
		}
		return 0;
	}

	//获取所有的历史记录
	public Cursor listHistory() throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_HISTORY, new String[] { "title","url","createOn" }, null, null, null, null, "createOn desc");
	}
	
	//获取所有的书签
	public Cursor listBookmark() throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_BOOK, new String[] { "name","url","createOn" }, null, null, null, null, "createOn desc");
	}
	
	//删除指定的历史记录
	public int deleteHistory(String...id) throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.delete(TABLE_HISTORY, "id=?", id);
	}
	
	//删除指定的书签
	public int deleteBookmark(String...id) throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.delete(TABLE_BOOK, "id=?", id);
	}
	
	//清除历史记录
	public int clearHistory() throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.delete(TABLE_HISTORY, null, null);
	}
}
