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
	public static final int DATA_VERSION = 2;
	public static final String TABLE_BOOK = "bookmark";
	public static final String TABLE_HISTORY = "history";
	public static final String COL_ID = "id";
	public static final String COL_TITLE = "title";
	public static final String COL_URL = "url";
	public static final String COL_CREATEON = "createOn";
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
			ExceptionHandler.log("web:create-database", "----start----");
			db.execSQL("CREATE TABLE " + TABLE_BOOK + " (" + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT NULL," + COL_URL + " TEXT NULL,"+ COL_CREATEON + " TEXT NOT NULL)");
			db.execSQL("CREATE TABLE " + TABLE_HISTORY + " (" + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT NULL," + COL_URL + " TEXT NULL,"+ COL_CREATEON + " TEXT NOT NULL)");
			//ExceptionHandler.log("sql", "CREATE TABLE " + TABLE_HISTORY + " (" + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT NULL," + COL_URL + " TEXT NULL,"+ COL_CREATEON + " TEXT NOT NULL)");
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
			ExceptionHandler.log("web:upgrade-database", "----start----");
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
			map.put(COL_TITLE, title);
			map.put(COL_URL, url);
			map.put(COL_CREATEON, System.currentTimeMillis());
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
			map.put(COL_TITLE, name);
			map.put(COL_URL, url);
			map.put(COL_CREATEON, System.currentTimeMillis());
			return db.insert(TABLE_BOOK, null, map);
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
		return db.query(TABLE_HISTORY, new String[] { COL_ID,COL_TITLE,COL_URL,COL_CREATEON }, null, null, null, null, "createOn desc");
	}
	
	//获取所有的书签
	public Cursor listBookmark() throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_BOOK, new String[] { COL_ID,COL_TITLE,COL_URL,COL_CREATEON }, null, null, null, null, "createOn desc");
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
