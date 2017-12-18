package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.AlertDialog;

public class BrowserSet extends AppCompatActivity
{
	private SharedPreferences spf;
	private String SH_home;
	private String[] Searchs, UserAgents;
	private int SH_UA, SH_search;
	private TextView btnHome, txtUserAgent, txtSearch;
	private LinearLayout btnUserAgent, btnSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browserset);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		txtSearch = (TextView) findViewById(R.id.txt_webSearch);
		txtUserAgent = (TextView) findViewById(R.id.txt_webUa);
		btnHome = (TextView) findViewById(R.id.btnSetHome);
		btnSearch = (LinearLayout) findViewById(R.id.btnSetSearch);
		btnUserAgent = (LinearLayout) findViewById(R.id.btnSetUa);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				setSetting();
				supportFinishAfterTransition();
			}
		});
		btnHome.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				View view = LayoutInflater.from(BrowserSet.this).inflate(R.layout.webhome, null);
				final EditText edt = (EditText) view.findViewById(R.id.edt_webhome);
				edt.setText(Util.isNullOrEmpty(SH_home) ? "http://" : SH_home);
				AlertDialog dialog = new AlertDialog(BrowserSet.this);
				dialog.setTitle("设置主页");
				dialog.setView(view);
				dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						SH_home = edt.getText().toString();
						if (SH_home.equalsIgnoreCase("http://") || SH_home.equalsIgnoreCase("https://"))
							SH_home = "";
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
				AlertDialog dialog = new AlertDialog(BrowserSet.this);
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
				AlertDialog dialog = new AlertDialog(BrowserSet.this);
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
		onSettings();
	}
	
	public void onSettings()
	{
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		SH_home = spf.getString("web_home", "http://");
		SH_search = spf.getInt("web_search", 0);
		SH_UA = spf.getInt("web_ua", 0);
		Searchs = getResources().getStringArray(R.array.array_search);
		UserAgents = getResources().getStringArray(R.array.array_ua_name);
		txtSearch.setText(Searchs[SH_search]);
		txtUserAgent.setText(UserAgents[SH_UA]);
	}

	public void setSetting()
	{
		SharedPreferences.Editor edt = spf.edit();
		edt.putString("web_home", SH_home);
		edt.putInt("web_search", SH_search);
		edt.putInt("web_ua", SH_UA);
		edt.commit();
	}

	@Override
	public void onBackPressed()
	{
		setSetting();
		super.onBackPressed();
	}
}
