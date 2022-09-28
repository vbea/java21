package com.vbea.java21.data;

//import cn.bmob.v3.BmobObject;

public class Messages //extends BmobObject
{
	public String user;
	public String title;
	public String message;
	public Integer type;
	public String url;
	public Boolean read;
	public String refId;
	
	public Messages makeRead()
	{
		read = true;
		return this;
	}
	
	public Messages makeUnread()
	{
		read = false;
		return this;
	}
}
