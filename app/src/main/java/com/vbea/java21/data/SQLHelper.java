package com.vbea.java21.data;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.vbea.java21.classes.ExceptionHandler;

public class SQLHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "CodeModeList.db";
	public static final int DATA_VERSION = 1;
	public static final String TABLE_NAME = "tb_code";
	public SQLHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,mode TEXT NOT NULL,path TEXT NOT NULL)");
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
			db.execSQL("drop table " + TABLE_NAME);
			onCreate(db);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("upgrade_database", e.toString());
		}
	}
	
	public long insert(String name, String mode, String path)
	{
		SQLiteDatabase db = getWritableDatabase();
		try
		{
			ContentValues map = new ContentValues();
			map.put("name", name);
			map.put("mode", mode);
			map.put("path", path);
			return db.insert(TABLE_NAME, null, map);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("sql_inset", e.toString());
		}
		finally
		{
			db.close();
		}
		return 0;
	}
	
	public Cursor select(String where, String...wheres)
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_NAME, new String[] {"id", "name", "mode", "path" }, where, wheres, null, null, "id");
	}
	
	public CodeMode findModeByPath(String fileType)
	{
		try
		{
			Cursor cur = select("path=?", fileType);
			if (cur != null && cur.getCount() > 0)
			{
				if (cur.moveToNext())
					return getData(cur);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("fideModeByPath", e.toString());
		}
		return null;
	}
	
	private CodeMode getData(Cursor cur) throws Exception
	{
		CodeMode mode = new CodeMode();
		mode.setId(cur.getInt(cur.getColumnIndex("id")));
		mode.setName(cur.getString(cur.getColumnIndex("name")));
		mode.setMode(cur.getString(cur.getColumnIndex("mode")));
		mode.setPath(cur.getString(cur.getColumnIndex("path")));
		return mode;
	}
}
