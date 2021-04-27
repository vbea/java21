package com.vbea.java21.ui;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;

public class Decord extends BaseActivity
{
	@Override
	protected void before()
	{
		setContentView(R.layout.decord);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		ImageView img_icon = bind(R.id.img_headicon);
		int id = getIntent().getIntExtra("id", 0);
		if (id > 0)
			img_icon.setImageResource(id == 1 ? R.drawable.donate_alipay : R.drawable.donate_wechat);
		LayoutParams params = img_icon.getLayoutParams();
		params.height = getWindowManager().getDefaultDisplay().getWidth();
		img_icon.setLayoutParams(params);
	}
}
