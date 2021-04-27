package com.vbea.java21.ui;

import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.LinearLayout;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.BuildConfig;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.update.MyUpdateAgent;
import com.vbes.util.EasyPreferences;
import com.vbes.util.secret.Dec;
import com.vbes.util.view.MyAlertDialog;

public class About extends BaseActivity
{
	LinearLayout actLayout;
	TextView install, active, txt_key, /*scrip,*/ type, sign, status, android, channel, version;

	@Override
	public void before() {
		setContentView(R.layout.about);
	}

	@Override
	public void after() {
		enableBackButton();
		actLayout = bind(R.id.abt_actLayout);
		TableRow rowStatus = bind(R.id.tab_actStatus);
		install = bind(R.id.about_install);
		active = bind(R.id.about_active);
		txt_key = bind(R.id.about_key);
		//scrip = bind(R.id.about_scrip);
		version = bind(R.id.about_version);
		type = bind(R.id.about_type);
		sign = bind(R.id.about_sign);
		android = bind(R.id.about_android);
		status = bind(R.id.about_status);
		channel = bind(R.id.about_channel);

		//added on 2021/03/10
		status.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		if (Common.isHuluxiaUser())
			rowStatus.setVisibility(View.GONE);
		MyUpdateAgent.update(this);
		version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
		status.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Common.startActivityOptions(About.this, Machine.class);
			}
		});
		String message = String.format(getString(R.string.abt_ver), BuildConfig.VERSION_NAME);
		bind(R.id.about_new).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MyAlertDialog dia = new MyAlertDialog(About.this);
				dia.setTitle("新版特性");
				dia.setMessage(message);
				dia.setPositiveButton("知道了", null);
				dia.setNeutralButton("复制到剪贴板", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int p) {
						Util.addClipboard(About.this, message);
					}
				});
				dia.show();
			}
		});
	}
	
	private void init() {
		try {
			android.setText("Android " + Build.VERSION.RELEASE + ", API " + Build.VERSION.SDK_INT);
			channel.setText(Util.getMetaValue(this, "InstallChannel"));
			Dec dec = new Dec();
			install.setText(dec.decrypt(Common.SDATE));
			sign.setText(Util.AuthKey(this) ? "验证通过" : "验证不通过");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Common.HULUXIA) {
			actLayout.setVisibility(View.GONE);
			sign.setText("验证不通过");
			return;
		}
		if (Common.IS_ACTIVE) {
			actLayout.setVisibility(View.VISIBLE);
			active.setText(Common.SID);
			txt_key.setText(Common.KEY);
			//scrip.setText(Common.SDATE.substring(0,25)+ (int)(Math.random() * Common.VERSION_CODE));
			status.setText("已激活");
			if (EasyPreferences.getBoolean("autok", false))
				type.setText("自动验证");
		}
	}

	@Override
	protected void onResume() {
		init();
		super.onResume();
	}
}
