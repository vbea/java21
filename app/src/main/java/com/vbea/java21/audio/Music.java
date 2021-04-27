package com.vbea.java21.audio;

public class Music
{
	public String name;
	public int max;
	public int min;
	public boolean isTop;
	public String key;

	public String getName()
	{
		return name;
	}

	public String[] getKeys()
	{
		try {
			return key.split(",");
		} catch (Exception e) {
			return null;
		}
	}
}
