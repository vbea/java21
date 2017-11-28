package com.vbea.java21.list;

import java.io.Serializable;

public class WifiItem implements Serializable, Comparable<WifiItem>
{
	public String SSID;
	public String Password;
	public String Safety;
	public int Priority;
	
	@Override
	public int compareTo(WifiItem i)
	{
		return (this.Priority - i.Priority > 0 ? 1: 0);
	}
}
