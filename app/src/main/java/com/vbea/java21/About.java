package com.vbea.java21;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.MyAlertDialog;
import com.vbea.secret.k;

import cn.bmob.v3.update.BmobUpdateAgent;

public class About extends BaseActivity
{
	LinearLayout actLayout;
	TextView install, active, txt_key, scrip, type, sign, status, android, channel;

	@Override
	public void before()
	{
		setContentView(R.layout.about);
	}

	@Override
	public void after()
	{
		enableBackButton();
		actLayout = bind(R.id.abt_actLayout);
		TextView newv = (TextView) findViewById(R.id.about_new);
		TableRow rowStatus = (TableRow) findViewById(R.id.tab_actStatus);
		install = (TextView) findViewById(R.id.about_install);
		active = (TextView) findViewById(R.id.about_active);
		txt_key = (TextView) findViewById(R.id.about_key);
		scrip = (TextView) findViewById(R.id.about_scrip);
		type = (TextView) findViewById(R.id.about_type);
		sign = (TextView) findViewById(R.id.about_sign);
		android = (TextView) findViewById(R.id.about_android);
		status = (TextView) findViewById(R.id.about_status);
		channel = (TextView) findViewById(R.id.about_channel);
		if (Common.isHuluxiaUser())
			rowStatus.setVisibility(View.GONE);
		BmobUpdateAgent.forceUpdate(this);
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
				MyAlertDialog dia = new MyAlertDialog(About.this);
				dia.setTitle("新版特性");
				dia.setMessage(R.string.abt_ver);
				dia.setPositiveButton("知道了", null);
				dia.setNeutralButton("复制到剪贴板", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int p)
					{
						Util.addClipboard(About.this, getString(R.string.abt_ver));
					}
				});
				dia.show();
			}
		});
	}
	
	private void init()
	{
		try
		{
			android.setText("Android " + Build.VERSION.RELEASE + ", API " + Build.VERSION.SDK);
			channel.setText(Util.getMetaValue(this, "InstallChannel"));
			k dec = new k();
			install.setText(dec.decrypt(Common.SDATE));
			sign.setText(Util.AuthKey(this) ? "验证通过" : "验证不通过");
		}
		catch (Exception e) {}
		if (Common.HULUXIA)
		{
			actLayout.setVisibility(View.GONE);
			sign.setText("验证不通过");
			return;
		}
		if (Common.IS_ACTIVE)
		{
			actLayout.setVisibility(View.VISIBLE);
			SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
			active.setText(Common.SID);
			txt_key.setText(Common.KEY);
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
