package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;

public class JavaEE extends ILearnList {
	final String publicPath = "java/";

	@Override
	public String getSubTitle() {
		return title;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public String getUrl() {
		return host + publicPath + url;
	}

	@Override
	public boolean isRead() {
		if (isTitle)
			return false;
		return ReadUtil.getInstance().isReadJavaEE(title);
	}
}
