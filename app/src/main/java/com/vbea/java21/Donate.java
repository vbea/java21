package com.vbea.java21;

import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

import com.vbea.java21.classes.Common;

public class Donate extends BaseActivity
{
	@Override
	protected void before()
	{
		setContentView(R.layout.donate);
	}

	@Override
	public void after()
	{
		enableBackButton();
		ImageView alipay = bind(R.id.img_donate1);
		ImageView wechat = bind(R.id.img_donate2);
		
		alipay.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Donate.this, Decord.class);
				intent.putExtra("id", 1);
				Common.startActivityOptions(Donate.this, intent, v, "icon_pre");
			}
		});
		wechat.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Donate.this, Decord.class);
				intent.putExtra("id", 2);
				Common.startActivityOptions(Donate.this, intent, v, "icon_pre");
			}
		});
	}
}
