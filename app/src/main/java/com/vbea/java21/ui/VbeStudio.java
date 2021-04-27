package com.vbea.java21.ui;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;

public class VbeStudio extends BaseActivity
{
	@Override
	protected void before()
	{
		setContentView(R.layout.vbeatelier);
	}

	@Override
	public void after()
	{
		enableBackButton();
	}
}
