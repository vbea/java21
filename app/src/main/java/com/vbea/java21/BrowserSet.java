package com.vbea.java21;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.Intent;
import android.content.DialogInterface;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.EasyPreferences;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;

public class BrowserSet extends BaseActivity
{
	private EasyPreferences spf;
	private String SH_home, SH_savePath;
	private String[] Searchs, UserAgents;
	private int SH_UA, SH_search;
	private TextView txtUserAgent, txtSearch, txtSavePath;

	@Override
	protected void before()
	{
		setContentView(R.layout.browserset);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		txtSearch = bind(R.id.txt_webSearch);
		txtUserAgent = bind(R.id.txt_webUa);
		txtSavePath = bind(R.id.txt_webSavePath);
		TextView btnHome = bind(R.id.btnSetHome);
		LinearLayout btnSearch = bind(R.id.btnSetSearch);
		LinearLayout btnUserAgent = bind(R.id.btnSetUa);
		LinearLayout btnSavePath = bind(R.id.btnSetSavePath);
		//btnHistory = bind(R.id.btnSetHistory);

		btnHome.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				View view = LayoutInflater.from(BrowserSet.this).inflate(R.layout.webhome, null);
				final EditText edt = (EditText) view.findViewById(R.id.edt_webhome);
				edt.setText(Util.isNullOrEmpty(SH_home) ? "http://" : SH_home);
				MyAlertDialog dialog = new MyAlertDialog(BrowserSet.this);
				dialog.setTitle("设置主页");
				dialog.setView(view);
				dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						SH_home = edt.getText().toString();
					}
				});
				dialog.setNegativeButton(android.R.string.cancel, null);
				dialog.show();
			}
		});
		btnSearch.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyAlertDialog dialog = new MyAlertDialog(BrowserSet.this);
				dialog.setTitle("设置搜索引擎");
				dialog.setSingleChoiceItems(Searchs, SH_search, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						SH_search = s;
						txtSearch.setText(Searchs[SH_search]);
						d.dismiss();
					}
				});
				dialog.show();
			}
		});
		btnUserAgent.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyAlertDialog dialog = new MyAlertDialog(BrowserSet.this);
				dialog.setTitle("浏览器UA标识");
				dialog.setSingleChoiceItems(UserAgents, SH_UA, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						SH_UA = s;
						txtUserAgent.setText(UserAgents[SH_UA]);
						d.dismiss();
					}
				});
				dialog.show();
			}
		});
		btnSavePath.setOnClickListener(new View.OnClickListener()
		{
			@SuppressLint("RestrictedApi")
			public void onClick(View v)
			{
				Intent intent = new Intent(BrowserSet.this, FileSelect.class);
				intent.putExtra("default", SH_savePath);
				startActivityForResult(intent, 100, ActivityOptions.makeSceneTransitionAnimation(BrowserSet.this).toBundle());
			}
		});
		onSettings();
	}
	
	public void onSettings()
	{
		spf = new EasyPreferences(this);
		SH_home = spf.getString("web_home", "http://");
		SH_savePath = spf.getString("web_savepath", Common.ExterPath + "/DCIM/Java21");
		SH_search = spf.getInt("web_search", 0);
		SH_UA = spf.getInt("web_ua", 0);
		Searchs = getResources().getStringArray(R.array.array_search);
		UserAgents = getResources().getStringArray(R.array.array_ua_name);
		txtSearch.setText(Searchs[SH_search]);
		txtUserAgent.setText(UserAgents[SH_UA]);
		showSavePath();
	}

	public void setSetting()
	{
		if (SH_home.equalsIgnoreCase("http://") || SH_home.equalsIgnoreCase("https://") || SH_home.equalsIgnoreCase("ftp://"))
			SH_home = "";
		spf.putString("web_home", SH_home);
		spf.putInt("web_search", SH_search);
		spf.putInt("web_ua", SH_UA);
		spf.putString("web_savepath", SH_savePath);
		spf.commit();
	}
	
	private void showSavePath()
	{
		txtSavePath.setText(SH_savePath.replace(Common.ExterPath, ""));
	}

	@Override
	public void onBackPressed()
	{
		setSetting();
		super.onBackPressed();
	}

	@Override
	protected void onFinish()
	{
		setSetting();
		super.onFinish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == 100)
		{
			String _path = data.getStringExtra("path");
			if (Util.isNullOrEmpty(_path))
			{
				Util.toastShortMessage(getApplicationContext(), "无效的目录");
				return;
			}
			SH_savePath = _path;
			showSavePath();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
