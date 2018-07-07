package com.vbea.java21;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Button; 
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.view.View;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.DataCleanManager;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.MyAlertDialog;

public class Setting extends AppCompatActivity
{
	//private Switch sldGift,sldSkip,sldAdver,sldEyes;
	private SharedPreferences spf;
	private TextView txtCacheSize, txtImageSize;
	private TextView btnSetheme, btnSetimg, btnScore,btnJoin,btnFeed, btnDonate, btnHistory, btnTextsize;
	private LinearLayout btnAdver, btnWelAdv, btnUpdate, btnMusic, btnTips;
	private Switch swiMusic, swiTips, swiAdv, swiWelAdv;
	private boolean isTipsChange = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool); 
		btnAdver = (LinearLayout) findViewById(R.id.btnSetadv);
		btnSetheme = (TextView) findViewById(R.id.btn_setTheme);
		btnSetimg = (TextView) findViewById(R.id.btn_setImage);
		btnScore = (TextView) findViewById(R.id.btn_setScore);
		btnJoin = (TextView) findViewById(R.id.btn_setJoin);
		btnFeed = (TextView) findViewById(R.id.btn_setHelp);
		btnHistory = (TextView) findViewById(R.id.btn_setHistory);
		btnDonate = (TextView) findViewById(R.id.btn_setDonate);
		btnTextsize = (TextView) findViewById(R.id.btn_setTextsize);
		btnUpdate = (LinearLayout) findViewById(R.id.btnUpdate);
		RelativeLayout btnCache = (RelativeLayout) findViewById(R.id.btn_setClearCache);
		RelativeLayout btnImageCache = (RelativeLayout) findViewById(R.id.btn_setClearCacheImage);
		btnMusic = (LinearLayout) findViewById(R.id.btnSetmusic);
		btnTips = (LinearLayout) findViewById(R.id.btnSettips);
		btnWelAdv = (LinearLayout) findViewById(R.id.btnSetadvwel);
		swiMusic = (Switch) findViewById(R.id.swtMusic);
		swiTips = (Switch) findViewById(R.id.swtTips);
		swiAdv = (Switch) findViewById(R.id.swtAdv);
		swiWelAdv = (Switch) findViewById(R.id.swtAdvwel);
		/*title = (RelativeLayout) findViewById(R.id.bg_title);
		sldGift = (SlidButton) findViewById(R.id.slid_gift);
		sldSkip = (SlidButton) findViewById(R.id.slid_skip);
		sldEyes = (SlidButton) findViewById(R.id.slid_eyes);*/
		txtCacheSize = (TextView) findViewById(R.id.txt_setCacheSize);
		txtImageSize = (TextView) findViewById(R.id.txt_setCacheImageSize);
		spf = getSharedPreferences("java21", MODE_PRIVATE);

		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		btnMusic.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swiMusic.toggle();
			}
		});
		
		swiMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				Common.MUSIC = p2;
			}
		});
		
		btnTips.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swiTips.toggle();
			}
		});

		swiTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				Common.TIPS = p2;
				isTipsChange = true;
			}
		});
		
		btnAdver.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swiAdv.toggle();
				/*if (Common.IS_ACTIVE)
				{
					Common.startActivityOptions(Setting.this, About.class);
				}
				else
				{
					Common.startActivityOptions(Setting.this, Machine.class);
				}*/
			}
		});
		
		btnWelAdv.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				swiWelAdv.toggle();
			}
		});
		
		swiAdv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				if (!p2 || Common.IS_ACTIVE)
				{
					Common.NO_ADV = p2;
					if (p2 && Common.isVipUser())
						btnWelAdv.setVisibility(View.VISIBLE);
					else
						btnWelAdv.setVisibility(View.GONE);
					swiWelAdv.setChecked(true);
				}
				else
				{
					Util.toastShortMessage(getApplicationContext(), "你需要激活后才能做此操作");
					swiAdv.setChecked(false);
				}
			}
		});
		
		swiWelAdv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				if (p2 || Common.isVipUser())
				{
					Common.WEL_ADV = p2;
				}
				else
				{
					//Util.toastShortMessage(getApplicationContext(), "此功能仅限VIP用户使用");
					swiWelAdv.setChecked(true);
				}
			}
		});
		
		btnSetheme.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Setting.this, Themes.class);
			}
		});
		
		btnSetimg.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Setting.this, SetDrawerImage.class);
			}
		});
		
		btnScore.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					Uri uri = Uri.parse("market://details?id="+getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				catch (Exception e)
				{
					Util.toastShortMessage(getApplicationContext(), "未检测到应用商店");
				}
			}
		});
		btnJoin.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//Util.toastShortMessage(getApplicationContext(), "捐赠版无此功能");
				Common.startActivityOptions(Setting.this, VbeStudio.class);
			}
		});
		btnFeed.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Setting.this, Feedback.class);
			}
		});
		btnDonate.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//Util.toastShortMessage(getApplicationContext(), "捐赠版无此功能");
				Common.startActivityOptions(Setting.this, Donate.class);
			}
		});
		btnHistory.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Util.showConfirmCancelDialog(Setting.this, android.R.string.dialog_alert_title, "您确定要清除阅读记录吗？此操作将清空Java基础、JavaEE和Android初级及进阶教程的阅读记录。",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						if (saveState(true))
						{
							Common.clearReadHistory();
							Util.toastShortMessage(getApplicationContext(), "操作成功");
						}
						else
							Util.toastShortMessage(getApplicationContext(), "清除失败，请重试");
					}
				});
			}
		});
		
		btnTextsize.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyAlertDialog dialog = new MyAlertDialog(Setting.this);
				dialog.setSingleChoiceItems(getResources().getStringArray(R.array.array_textsize), Common.JAVA_TEXT_SIZE,
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int s)
						{
							Common.JAVA_TEXT_SIZE = s;
							d.dismiss();
						}
					});
				dialog.show();
			}
		});
		
		btnUpdate.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Setting.this, About.class);
				//Util.toastShortMessage(getApplicationContext(), "已是最新版本");
				//startActivity(new Intent(Setting.this, About.class));
			}
		});
		
		btnCache.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (txtCacheSize.getText().toString().equals("0.0B"))
				{
					Util.toastShortMessage(getApplicationContext(), "已经很干净了，不需要再清咯");
					return;
				}
				Util.showConfirmCancelDialog(Setting.this, android.R.string.dialog_alert_title, "您确定要清除缓存？此操作将清空所有网页和图片缓存。",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						try
						{
							clean();
							Util.toastShortMessage(getApplicationContext(), "操作成功");
						}
						catch (Exception e)
						{
							//txtCacheSize.setText("N/A");
							Util.toastShortMessage(getApplicationContext(), "操作失败");
						}
					}
				});
			}
		});
		
		btnImageCache.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (txtImageSize.getText().toString().equals("0.0B"))
				{
					Util.toastShortMessage(getApplicationContext(), "已经很干净了，不需要再清咯");
						return;
				}
				Util.showConfirmCancelDialog(Setting.this, android.R.string.dialog_alert_title, "您确定要清除图片缓存？此操作将清空教程评论页面缓存的其他用户头像。",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int s)
						{
							try
							{
								cleanImage();
								Util.toastShortMessage(getApplicationContext(), "操作成功");
							}
							catch (Exception e)
							{
								Util.toastShortMessage(getApplicationContext(), "操作失败");
							}
						}
					});
			}
		});
		init();
	}
	
	private void clean() throws Exception
	{
		DataCleanManager.cleanInternalCache(Setting.this);
		txtCacheSize.setText(DataCleanManager.getCacheSize(getApplicationContext()));
	}
	
	private void cleanImage() throws Exception
	{
		DataCleanManager.cleanCustomCache(Common.getCachePath());
		txtImageSize.setText(DataCleanManager.getFolderSize(Common.getCachePath()));
	}
	
	private void init()
	{
		try
		{
			swiMusic.setChecked(spf.getBoolean("music", true));
			swiTips.setChecked(spf.getBoolean("tips", true));
			swiAdv.setChecked(spf.getBoolean("noadv", false));
			swiWelAdv.setChecked(spf.getBoolean("weladv", true));
			txtCacheSize.setText(DataCleanManager.getCacheSize(this));
			txtImageSize.setText(DataCleanManager.getFolderSize(Common.getCachePath()));
			if (Common.HULUXIA)
			{
				btnAdver.setVisibility(View.GONE);
				btnJoin.setVisibility(View.GONE);
				return;
			}
			if (Common.isNoadv() && Common.isVipUser())
				btnWelAdv.setVisibility(View.VISIBLE);
		}
		catch (Exception e)
		{
			txtCacheSize.setText("N/A");
			txtImageSize.setText("N/A");
		}
	}
	
	public boolean saveState(boolean clear)
	{
		if (isTipsChange)
			Common.getTips();
		SharedPreferences.Editor edt = spf.edit();
		edt = spf.edit();
		edt.putBoolean("music",swiMusic.isChecked());
		edt.putBoolean("tips",swiTips.isChecked());
		edt.putBoolean("noadv", swiAdv.isChecked());
		edt.putBoolean("weladv", swiWelAdv.isChecked());
		edt.putInt("java_size", Common.JAVA_TEXT_SIZE);
		if (clear)
		{
			edt.putString("read_java", "");
			edt.putString("read_android", "");
		 	edt.putString("read_javaee", "");
			edt.putString("read_android2", "");
		}
		return edt.commit();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		saveState(false);
		super.onDestroy();
	}
}
