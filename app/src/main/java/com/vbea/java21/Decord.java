package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Decord extends AppCompatActivity
{
	private ImageView img_icon;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decord);

		img_icon = (ImageView) findViewById(R.id.img_headicon);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		int id = getIntent().getIntExtra("id", 0);
		if (id > 0)
			img_icon.setImageResource(id == 1 ? R.mipmap.donate_alipay : R.mipmap.donate_wechat);
		LayoutParams params = img_icon.getLayoutParams();
		params.height = getWindowManager().getDefaultDisplay().getWidth();
		img_icon.setLayoutParams(params);

		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
	}
}
