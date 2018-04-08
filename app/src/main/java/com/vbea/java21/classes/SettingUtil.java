package com.vbea.java21.classes;

import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class SettingUtil
{
	private HashMap<String,Object> mMap;
	public static final String SET_BACKIMG = "BackImage";
	public static final String SET_THEME = "Theme";
	public static final String SET_FONTSIZE = "FontSize";
	//public static final String
	public SettingUtil()
	{
		mMap = new HashMap<String, Object>();
	}
	
	public void synaxSetting(String json)
	{
		try
		{
			if (!Util.isNullOrEmpty(json))
			{
				Gson gson = new Gson();
				mMap = gson.fromJson(json, HashMap.class);
				/*JsonParser jsp = new JsonParser();
				JsonObject jobj = jsp.parse(json).getAsJsonObject();
				if (jobj.has(SET_THEME))
					mMap.put(SET_THEME, jobj.get(SET_THEME).getAsInt());
				if (jobj.has(SET_BACKIMG))
					mMap.put(SET_BACKIMG, jobj.get(SET_BACKIMG).getAsInt());
				if (jobj.has(SET_FONTSIZE))
					mMap.put(SET_FONTSIZE, jobj.get(SET_FONTSIZE).getAsInt());*/
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("synaxSetting", e.toString());
		}
	}
	
	public void addSettings(String key, Object value)
	{
		if (key != null && value != null)
		{
			if (mMap == null)
				mMap = new HashMap<String, Object>();
			mMap.put(key, value);
		}
	}
	
	public int getIntValue(String key) throws Exception
	{
		if (mMap.containsKey(key))
		{
			Object obj = mMap.get(key);
			if (obj instanceof int)
			{
				return (int)obj;
			}
			throw new Exception("format error");
		}
		else
			throw new Exception("error key");
	}
	
	public String getStringValue(String key) throws Exception
	{
		if (mMap.containsKey(key))
			return mMap.get(key).toString();
		else
			throw new Exception("error key");
	}
	
	public String getJsonString()
	{
		if (mMap != null && mMap.size() > 0)
		{
			Gson gson = new Gson();
			return gson.toJson(mMap);
		}
		return "";
	}
}
