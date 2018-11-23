package com.vbea.java21;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class Decord extends BaseActivity
{
	private ImageView img_icon;

	@Override
	protected void before()
	{
		setContentView(R.layout.decord);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		img_icon = (ImageView) findViewById(R.id.img_headicon);
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
