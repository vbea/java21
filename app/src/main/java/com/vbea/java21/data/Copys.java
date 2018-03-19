package com.vbea.java21.data;

import cn.bmob.v3.BmobObject;

public class Copys extends BmobObject
{
	private String title;
	private String message;
	private String url;
	private Boolean enable;

	public String getUrl()
	{
		return url;
	}

	public String getTitle()
	{
		return title;
	}

	public Boolean isEnable()
	{
		return enable;
	}

	public String getMessage()
	{
		return message;
	}
}
