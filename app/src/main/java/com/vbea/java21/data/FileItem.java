package com.vbea.java21.data;

public class FileItem implements Comparable<FileItem>
{
	private String name;
	private String detail;
	private boolean isUplev;

	public FileItem()
	{
		isUplev = false;
	}
	
	public void setIsUplev(boolean isUplev)
	{
		this.isUplev = isUplev;
	}

	public boolean isUplev()
	{
		return isUplev;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public FileItem addUplev()
	{
		isUplev = true;
		name = "..";
		detail = "上级目录";
		return this;
	}
	
	@Override
	public int compareTo(FileItem p1)
	{
		return this.name.toLowerCase().compareTo(p1.getName().toLowerCase());
	}
}
