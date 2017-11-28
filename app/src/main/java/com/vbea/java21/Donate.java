package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.classes.Common;

public class Donate extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donate);

		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		ImageView alipay = (ImageView) findViewById(R.id.img_donate1);
		ImageView wechat = (ImageView) findViewById(R.id.img_donate2);
		setSupportActionBar(tool);

		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
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
