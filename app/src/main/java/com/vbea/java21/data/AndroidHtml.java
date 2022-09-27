package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.classes.Util;

import cn.bmob.v3.BmobObject;

public class AndroidHtml implements ILearnList {
	public String title;
	public String prefix;
	public String remark;
	public String url;
	public Boolean isTitle;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getSubTitle() {
		return remark;
	}

	@Override
	public String getPrefix() {
		if (Util.isNullOrEmpty(prefix))
			return "┗ ";
		return prefix;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Boolean isTitle() {
		return isTitle;
	}

	@Override
	public boolean isRead() {
		return ReadUtil.getInstance().isReadAndroid(title);
	}
}
