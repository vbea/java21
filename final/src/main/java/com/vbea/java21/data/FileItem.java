package com.vbea.java21.data;

public class FileItem implements Comparable<FileItem>
{
	private String name;
	private String detail;
	private String size;
	private boolean enable;
	private boolean isUplev;
	private boolean isFolder;

	public FileItem()
	{
		isUplev = false;
		enable = true;
	}

	public void setSize(String s)
	{
		size = s;
	}

	public String getSize()
	{
		return size;
	}

	public boolean isEnable()
	{
		return enable;
	}

	public void setIsFolder(boolean folder)
	{
		isFolder = folder;
	}

	public boolean isFolder()
	{
		return isFolder;
	}
	
	public void setIsUplev(boolean uplev)
	{
		isUplev = uplev;
	}

	public boolean isUplev()
	{
		return isUplev;
	}

	public void setDetail(String d)
	{
		detail = d;
	}

	public String getDetail()
	{
		return detail;
	}

	public void setName(String n)
	{
		name = n;
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
		isFolder = true;
		return this;
	}
	
	public FileItem addLoading()
	{
		isUplev = true;
		name = "..";
		detail = "加载中";
		isFolder = true;
		enable = false;
		return this;
	}
	
	@Override
	public int compareTo(FileItem p1)
	{
		return this.name.toLowerCase().compareTo(p1.getName().toLowerCase());
	}
}
