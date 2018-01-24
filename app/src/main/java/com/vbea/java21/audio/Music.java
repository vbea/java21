package com.vbea.java21.audio;

public class Music
{
	public String name;
	public int max;
	public int min;
	public int length;
	private String[] keys;

	public void setKeys(String[] k)
	{
		keys = k;
		length = k.length;
	}

	public String[] getKeys()
	{
		return keys;
	}
}
