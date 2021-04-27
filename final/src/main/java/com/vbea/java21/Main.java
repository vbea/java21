package com.vbea.java21;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.tencent.stat.StatService;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.fragment.ChapterFragment;
import com.vbea.java21.fragment.KnowFragment;
import com.vbea.java21.adapter.FragmentAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.audio.SoundLoad;

public class Main extends BaseActivity
{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
	private long exitTime = 0;
	private LinearLayout drawMuic, drawTheme;
	private TextView txtAudioCode;
	private boolean START = false;
	private SoundThread soundThread;

	@Override
	protected void before()
	{
		setContentView(R.layout.main);
		StatService.trackCustomEvent(this, "onCreate", "");
	}

	@Override
    protected void after()
	{
		START = getIntent().getBooleanExtra("start", false);
        mDrawerLayout = bind(R.id.drawer_layout);
		ViewPager viewpager = bind(R.id.viewpager);
		TabLayout tabLayout = bind(R.id.tabLayout);
		drawMuic = bind(R.id.drawer_music);
		txtAudioCode = bind(R.id.main_showAudioCode);
		mHandler.sendEmptyMessageDelayed(2, 2000);
		FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager());
		fa.addItem(new ChapterFragment(), getString(R.string.contacts));
		fa.addItem(new KnowFragment(), getString(R.string.javaadv));
		/*fa.addItem(new JavaFragment(), "J2EE");
		fa.addItem(new DatabaseFragment(), "SQL");
		fa.addItem(new AndroidFragment(), "安卓基础");
		fa.addItem(new Android2Fragment(), "安卓进阶");
		fa.addItem(new AideFragment(), "AIDE");*/
        viewpager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewpager);
		tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.gray2));
		MyThemes.ISCHANGED = true;
		Common.IsChangeICON = true;
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
		mDrawerLayout.addDrawerListener(drawerToggle);
		mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
		{
			@Override
			public void onDrawerSlide(View p1, float p2)
			{
				
			}

			@Override
			public void onDrawerOpened(View p1)
			{
				closeTip();
			}

			@Override
			public void onDrawerClosed(View p1)
			{
			}

			@Override
			public void onDrawerStateChanged(int p1)
			{
				mDrawerLayout.requestFocus();
			}
		});
    }
	
	/*private void setHead()
	{
		if (Common.isNet(this) && Common.canLogin())
			onAutoLogin();
		//设置菜单图标
		/*if (!Common.isSupportMD())
		{
			imgTheme.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_theme)));
			imgMusic.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_music)));
			imgApi.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_api)));
			imgReplace.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_place)));
			imgSetting.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_setting)));
			imgHelp.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_help)));
			imgWifi.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_wifi)));
			imgSMS.setImageBitmap(handleBitmap((BitmapDrawable)getResources().getDrawable(R.mipmap.ic_warsms)));
		}*/
		//20180719-消除CoordinatorLayout底部空白
		/*if (Util.isAndroidM())
		{
			int statusBarHeight = Util.getStatusBarHeight(this);
			Util.toastShortMessage(getApplicationContext(), "height:" + statusBarHeight);
			if (statusBarHeight >= 0) {
				int sunHeight = statusBarHeight + DensityUtil.dip2px(this, 27);
				ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
				layoutParams.height = sunHeight;
				toolbar.setLayoutParams(layoutParams);
				View rootLayout = findViewById(R.id.mainRootLayout);
				rootLayout.setPadding(0, -statusBarHeight * 2, 0, 0);
			}
		}
	}*/
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
	{
		drawerToggle.syncState();
		super.onPostCreate(savedInstanceState);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig)
	{
		drawerToggle.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}
	
	public void closeTip()
	{
		//if (layoutTips.getVisibility() != View.GONE)
	}


	//抽屉
	public void goTheme(View v)
	{
		Common.startActivityOptions(Main.this, Themes.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goSetting(View v)
	{
		Common.startActivityOptions(Main.this, Setting.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goAPI(View v)
	{
		Common.startActivityOptions(Main.this, ApiWord.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goMusic(View v)
	{
		Common.startActivityOptions(Main.this, More.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goHelp(View v)
	{
		Common.startActivityOptions(Main.this, Help.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goEditor(View v)
	{
		Common.startActivityOptions(Main.this, TextReplace.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	/*public void goSms(View v)
	{
		if (!Common.isLogin())
		{
			Util.toastShortMessage(getApplicationContext(), "请先登录！");
			return;
		}
		Util.showConfirmCancelDialog(this,android.R.string.dialog_alert_title, "请不要做非法的事情！", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface d, int s)
			{
				Common.startActivityOptions(Main.this, MessageOOM.class);
				mHandler.sendEmptyMessageDelayed(1, 500);
			}
		});
	}*/
	
	public void goBack(View v)
	{
		saveStatus();
		/*if (!Common.isSVipUser())
		{
			List<Copys> msg = Common.getCopyMsg();
			if (msg != null)
				Util.addClipboard(this, "java21", msg.getMessage());
		}*/
		Common.gc(this);
		//toolbar.setTransitionName(null);
		if (Common.isSupportMD())
			finishAndRemoveTask();
		else
			finish();
		ActivityManager.getInstance().FinishAllActivities();
	}
	
	private void onAudioDialog()
	{
		if (Common.isAudio() && Common.AUDIO_STUDY_STATE >= 20) {
		MyAlertDialog builder = new MyAlertDialog(this);
		builder.setTitle("提示");
		builder.setCancelable(false);
		builder.setMessage("学累了吧，要不要去音乐盛典听听音乐放松下？");
		builder.setPositiveButton("去看看", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface d, int p)
			{
				Common.AUDIO_STUDY_STATE = 0;
				Common.startActivityOptions(Main.this, More.class);
			}
		});
		builder.setNegativeButton("不用了", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface d, int p)
			{
				Common.AUDIO_STUDY_STATE = 0;
			}
		});
		builder.show();
		}
	}
	
	public void closeDrawered()
	{
		mDrawerLayout.closeDrawer(Gravity.START);
	}

	@Override
	protected void onPause()
	{
		StatService.onPause(this);
		super.onPause();
	}
	
	/*private Bitmap handleBitmap(BitmapDrawable draw)
	{
		Bitmap bm = draw.getBitmap().extractAlpha();
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(getResources().getColor(MyThemes.getColorPrimary()));
		canvas.drawBitmap(bm, 0, 0, paint);
		return bmp;
	}*/
	
	private void saveStatus()
	{
		ReadUtil.getInstance().saveData();
		/*if (Common.isCanUploadUserSetting())
			Common.updateUser();
		//Common.getCopyMsg(this);
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putString("read_java", Util.Join(",", Common.READ_Java));
		editor.putString("read_android", Util.Join(",", Common.READ_Android));
		editor.putString("read_android2", Util.Join(",", Common.READ_AndroidAdvance));
		editor.putString("read_javaee", Util.Join(",", Common.READ_J2EE));
		return editor.commit();*/
	}


	@Override
	protected void onResume()
	{
		mHandler.sendEmptyMessage(5);
		if (Common.MUSIC)
		{
			if (soundThread == null || !soundThread.RUN)
			{
				soundThread = new SoundThread();
				soundThread.start();
			}
		}
		onAudioDialog();
		StatService.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	class SoundThread extends Thread implements Runnable
	{
		public boolean RUN = false;
		public void run()
		{
			synchronized(this){
			try
			{
				RUN = true;
				if (Common.SOUND == null)
					Common.SOUND = new SoundLoad(Main.this);
				if (!Common.SOUND.isCompeted)
				{
					Common.SOUND.load();
				}
				if (Common.audioService != null && Common.audioService.isPlay())
				{
					while (Common.audioService.isPlay())
					{
						Thread.sleep(10);
						mHandler.sendEmptyMessage(3);
					}
					mHandler.sendEmptyMessage(4);
				}
			}
			catch (Exception er)
			{
				ExceptionHandler.log("Main.SoundThread", er.toString());
				mHandler.sendEmptyMessage(4);
			}
			finally
			{
				mHandler.sendEmptyMessage(5);
				RUN = false;
			}}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					finish();
					break;
				case 1:
					closeDrawered();
					break;
				case 3:
					txtAudioCode.setText(Common.audioService.mid);
					break;
				case 4:
					txtAudioCode.setText("");
					break;
				case 5:
					if (Common.AUDIO && Common.MUSIC)
						drawMuic.setVisibility(View.VISIBLE);
					else
						drawMuic.setVisibility(View.GONE);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (!mDrawerLayout.isDrawerOpen(Gravity.START))
			{
				if((System.currentTimeMillis() - exitTime) > 2000)
				{
					//if (saveStatus())
					Util.toastShortMessage(getApplicationContext(), "再按一次退出程序");
					exitTime = System.currentTimeMillis();
				}
				else
					onExit();
			}
			else
				closeDrawered();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (mDrawerLayout.isDrawerOpen(Gravity.START))
				closeDrawered();
			else
				mDrawerLayout.openDrawer(Gravity.START);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void onExit()
	{
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
		/*if (Common.isSupportMD())
			finishAndRemoveTask();
		else
			finish();*/
	}
}
