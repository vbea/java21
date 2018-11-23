package com.vbea.java21.data;

public class DrawItem
{
	public int id;
	public int res;
	public boolean p;
	public DrawItem(int d, int r)
	{
		id = d;
		res = r;
		p = true;
	}
	public DrawItem()
	{
		id = 100;
		p = false;
	}
}
