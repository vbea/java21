package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.classes.Util;

public class AndroidHtml extends ILearnList {
	final String publicPath = "android/";

	@Override
	public String getPrefix() {
		if (Util.isNullOrEmpty(prefix))
			return "┗ ";
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
		return ReadUtil.getInstance().isReadAndroid(title);
	}
}
