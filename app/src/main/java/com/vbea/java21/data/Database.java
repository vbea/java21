package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;

import cn.bmob.v3.BmobObject;

public class Database extends BmobObject implements ILearnList
{
	public String title;
	public String url;
	public Integer order;
	public Boolean isTitle;
	public Boolean enable;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getSubTitle() {
		return title;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Integer getOrder() {
		return order;
	}

	@Override
	public Boolean isTitle() {
		return isTitle;
	}

	@Override
	public boolean isRead() {
		return ReadUtil.getInstance().isReadDatabase(getObjectId());
	}
}
