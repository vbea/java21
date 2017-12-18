package com.vbea.java21.data;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.vbea.java21.R;
import com.vbea.java21.classes.ExceptionHandler;

public class SQLHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "CodeModeList.db";
	public static final int DATA_VERSION = 1;
	public static final String TABLE_NAME = "tb_code";
	private Context context;
	public SQLHelper(Context c)
	{
		super(c, DATABASE_NAME, null, DATA_VERSION);
		context = c;
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
	
	public void initJsonData(Callback back)
	{
		try
		{
			JSONArray jsa = new JSONArray(context.getString(R.string.codejson));
			if (jsa != null && jsa.length() > 0)
			{
				for (int i = 0; i < jsa.length(); i++)
				{
					JSONObject code = jsa.getJSONObject(i);
					if (code != null)
					{
						String name = code.getString("name");
						String mode = code.getString("mode").toLowerCase();
						String[] paths = code.getString("path").split("\\|");
						if (paths != null && paths.length > 0)
						{
							for (String path : paths)
							{
								insert(name, mode, path);
							}
						}
					}
				}
				if (back != null)
					back.onSuccess();
			}
		}
		catch (JSONException e)
		{
			ExceptionHandler.log("json_error", e.toString());
			if (back != null)
				back.onFailure();
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
	
	public Cursor select(String where, String...wheres) throws Exception
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_NAME, new String[] {"id", "name", "mode", "path" }, where, wheres, null, null, "id");
	}
	
	public Cursor select()
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.query(true, TABLE_NAME, new String[] { "name","mode" }, null, null, "name,mode", null, "name", null);
	}
	
	public CodeMode findModeByType(String fileType)
	{
		try
		{
			Cursor cur = select("path=?", fileType);
			if (cur != null && cur.getCount() > 0)
			{
				if (cur.moveToFirst())
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
	
	public interface Callback
	{
		void onSuccess();
		void onFailure();
	}
}
