package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;

public class SQLHtml extends ILearnList {
	final String publicPath = "sql/";
	@Override
	public String getPrefix() {
		return "";
	}

	@Override
	public String getSubTitle() {
		return title;
	}

	@Override
	public String getUrl() {
		return host + publicPath + url;
	}

	@Override
	public boolean isRead() {
		return ReadUtil.getInstance().isReadDatabase(title);
	}
}
