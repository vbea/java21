package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.secret.k;

import cn.bmob.v3.update.BmobUpdateAgent;

public class About extends AppCompatActivity
{
	LinearLayout actLayout;
	TextView install, active, txt_key, scrip, type, sign, status;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		actLayout = (LinearLayout) findViewById(R.id.abt_actLayout);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		TextView newv = (TextView) findViewById(R.id.about_new);
		install = (TextView) findViewById(R.id.about_install);
		active = (TextView) findViewById(R.id.about_active);
		txt_key = (TextView) findViewById(R.id.about_key);
		scrip = (TextView) findViewById(R.id.about_scrip);
		type = (TextView) findViewById(R.id.about_type);
		sign = (TextView) findViewById(R.id.about_sign);
		status = (TextView) findViewById(R.id.about_status);
		setSupportActionBar(tool);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		status.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(About.this, Machine.class);
			}
		});
		newv.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Util.showResultDialog(About.this, getString(R.string.abt_ver), "新版特性");
			}
		});
	}
	
	private void init()
	{
		try
		{
			k dec = new k();
			install.setText(dec.decrypt(Common.SDATE));
			sign.setText(Util.AuthKey(this) ? "验证通过" : "验证不通过");
		}
		catch (Exception e) {}
		BmobUpdateAgent.forceUpdate(About.this);
		if (Common.IS_ACTIVE)
		{
			actLayout.setVisibility(View.VISIBLE);
			SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
			active.setText(Common.SID);
			txt_key.setText(Common.HULUXIA ? "破解版" : Common.KEY);
			scrip.setText(Common.SDATE.substring(0,25)+ (int)(Math.random() * Common.VERSION_CODE));
			status.setText("已激活");
			if (spf.getBoolean("autok", false))
				type.setText("自动验证");
		}
	}

	@Override
	protected void onResume()
	{
		init();
		super.onResume();
	}
}
