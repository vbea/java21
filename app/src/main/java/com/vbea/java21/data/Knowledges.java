package com.vbea.java21.data;

public class Knowledges
{
	public String TITLE;
	public String SUB;
	public String URL;
	
	public Knowledges(String name, String sub, String url)
	{
		TITLE = name;
		SUB = sub;
		URL = url;
	}
	
	public String getId()
	{
		return this.URL;
		//String.valueOf(ID);
	}
	
	public String getUrl()
	{
		return "file:///android_asset/java/" + this.URL + ".html";
	}
}
