package com.vbea.java21.data;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.BmobObject;

public class Users extends BmobObject
{
	//public String objectId;
	public String name;
	public String psd;
	public String nickname;
	public Boolean gender;
	public String email;
	public String birthday;
	public Bitmap icon;
	public String key;
	public Integer dated;
	public String mark;
	public String qq;
	public String qqId;
	public String weixin;
	public String wxId;
	public String mobile;
	public String address;
	public Integer role;
	public Boolean valid;
	public String settings;
	public BmobDate lastLogin;
	public String device;
	public String serialNo;
}
