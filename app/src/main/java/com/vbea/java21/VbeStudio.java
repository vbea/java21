package com.vbea.java21;

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
