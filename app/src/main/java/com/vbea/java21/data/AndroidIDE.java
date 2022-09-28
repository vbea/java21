package com.vbea.java21.data;

import com.vbea.java21.classes.ReadUtil;

public class AndroidIDE extends ILearnList {

	@Override
	public String getPrefix() {
		return "";
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public boolean isRead() {
		return ReadUtil.getInstance().isReadAide(title);
	}
}
