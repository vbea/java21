package com.vbea.java21;

import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.view.View;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.DataCleanManager;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;

public class Setting extends BaseActivity
{
	private SharedPreferences spf;
	private TextView txtCacheSize, txtImageSize;
	private TextView btnSetheme, btnSetimg, btnScore,btnJoin,btnFeed, btnDonate, btnHistory, btnTextsize;
	private LinearLayout btnAdver, btnWelAdv, btnUpdate, btnMusic, btnTips;
	private Switch swiMusic, swiTips, swiAdv, swiWelAdv;
	private boolean isTipsChange = false;

	@Override
	protected void before()
	{
		setContentView(R.layout.settings);
	}
	
	@Override
	public void after()
	{
		enableBackButton();
		btnAdver = bind(R.id.btnSetadv);
		btnSetheme = bind(R.id.btn_setTheme);
		btnSetimg = bind(R.id.btn_setImage);
		btnScore = bind(R.id.btn_setScore);
		btnJoin = bind(R.id.btn_setJoin);
		btnFeed = bind(R.id.btn_setHelp);
		btnHistory = bind(R.id.btn_setHistory);
		btnDonate = bind(R.id.btn_setDonate);
		btnTextsize = bind(R.id.btn_setTextsize);
		btnUpdate = bind(R.id.btnUpdate);
		RelativeLayout btnCache = bind(R.id.btn_setClearCache);
		RelativeLayout btnImageCache = bind(R.id.btn_setClearCacheImage);
		btnMusic = bind(R.id.btnSetmusic);
		btnTips = bind(R.id.btnSettips);
		btnWelAdv = bind(R.id.btnSetadvwel);
		swiMusic = bind(R.id.swtMusic);
		swiTips = bind(R.id.swtTips);
		swiAdv = bind(R.id.swtAdv);
		swiWelAdv = bind(R.id.swtAdvwel);
		txtCacheSize = bind(R.id.txt_setCacheSize);
		txtImageSize = bind(R.id.txt_setCacheImageSize);
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		
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
				if (!p2 || Common.IS_ACTIVE || Common.isVipUser())
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
					toastShortMessage("你需要激活后才能做此操作");
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
					toastShortMessage("未检测到应用商店");
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
							toastShortMessage("操作成功");
						}
						else
							toastShortMessage("清除失败，请重试");
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
					toastShortMessage("已经很干净了，不需要再清咯");
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
			if (Common.isHuluxiaUser())
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
	protected void onFinish()
	{
		saveState(false);
		super.onFinish();
	}
	
	@Override
	protected void onDestroy()
	{
		saveState(false);
		super.onDestroy();
	}
}
