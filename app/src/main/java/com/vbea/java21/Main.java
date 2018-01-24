package com.vbea.java21;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.CircularBorderDrawable;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;

import cn.bmob.v3.update.BmobUpdateAgent;
import com.tencent.stat.StatService;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.comm.util.AdError;
import com.vbea.java21.list.FragmentAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.AdvConfig;
import com.vbea.java21.classes.InboxManager;
import com.vbea.java21.classes.AlertDialog;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.widget.CustomTextView;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.data.Tips;
import com.tencent.smtt.sdk.QbSdk;

public class Main extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
	private ViewPager viewpager;
	private ImageView mImgHead;//,imgTheme,imgMusic,imgApi,imgReplace,imgSetting,imgHelp,imgWifi,imgSMS;//imgExit;
    private Toolbar toolbar;
	private TabLayout tabLayout;
	private long exitTime = 0;
	private LinearLayout drawMuic, drawTheme, drawSms, drawWifi, drawCodeEditor, layoutTips;
	private RelativeLayout drawUser;
	private TextView txtUserName, txtSignature, txtAudioCode; //btnTips;
	private CustomTextView txtCotation;
	private boolean START = false;
	private SoundThread soundThread;
	private ViewGroup bannerLayout;
	private BannerView bannerView;
	private InboxCallback myCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
		Common.start(getApplicationContext());
		MyThemes.initBackColor(this);
		setTheme(MyThemes.getTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		StatService.trackCustomEvent(this, "onCreate", "");
		START = getIntent().getBooleanExtra("start", false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mImgHead = (ImageView) findViewById(R.id.img_head);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		drawMuic = (LinearLayout) findViewById(R.id.drawer_music);
		drawTheme = (LinearLayout) findViewById(R.id.draw_item1);
		drawSms = (LinearLayout) findViewById(R.id.drawer_smsLayout);
		drawWifi = (LinearLayout) findViewById(R.id.drawer_wifiLayout);
		drawCodeEditor = (LinearLayout) findViewById(R.id.drawer_codeEditor);
		drawUser = (RelativeLayout) findViewById(R.id.draw_user);
		layoutTips = (LinearLayout) findViewById(R.id.layout_tips);
		txtUserName = (TextView) findViewById(R.id.draw_txtName);
		txtSignature = (TextView) findViewById(R.id.draw_txtMark);
		txtAudioCode = (TextView) findViewById(R.id.main_showAudioCode);
		bannerLayout = (ViewGroup) findViewById(R.id.mainBanner);
		/*imgTheme = (ImageView) findViewById(R.id.draw_image1);
		imgMusic = (ImageView) findViewById(R.id.draw_image2);
		imgApi = (ImageView) findViewById(R.id.draw_image3);
		imgReplace = (ImageView) findViewById(R.id.draw_image4);
		imgSetting = (ImageView) findViewById(R.id.draw_image5);
		imgHelp = (ImageView) findViewById(R.id.draw_image6);
		imgWifi = (ImageView) findViewById(R.id.draw_image7);
		imgSMS = (ImageView) findViewById(R.id.draw_image8);*/
		//btnTips = (TextView) findViewById(R.id.txt_mainTipsbtn);
		txtCotation = (CustomTextView) findViewById(R.id.txt_quotation);
		setSupportActionBar(toolbar);
		//toolbar.setNavigationIcon(R.mipmap.ic_ab_drawer);
		//toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mHandler.sendEmptyMessageDelayed(2, 2000);
		FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager());
		fa.addItem(new ChapterFragment(), getString(R.string.contacts));
		fa.addItem(new KnowFragment(), getString(R.string.javaadv));
		if (!Common.HULUXIA || Common.IS_ACTIVE)
		{
			fa.addItem(new JavaFragment(), "J2EE");
			fa.addItem(new AndroidFragment(), "安卓基础");
			fa.addItem(new Android2Fragment(), "安卓进阶");
			fa.addItem(new AideFragment(), "AIDE");
		}
        viewpager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewpager);
		tabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.gray2));
		MyThemes.ISCHANGED = true;
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
		mDrawerLayout.addDrawerListener(drawerToggle);
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
		{
			@Override
			public void onDrawerSlide(View p1, float p2)
			{
				
			}

			@Override
			public void onDrawerOpened(View p1)
			{
				txtCotation.pause();
				closeTip();
			}

			@Override
			public void onDrawerClosed(View p1)
			{
				setTip(Common.getTip());
			}

			@Override
			public void onDrawerStateChanged(int p1)
			{
				mDrawerLayout.requestFocus();
			}
		});
		drawTheme.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				Common.startActivityOptions(Main.this, SetDrawerImage.class);
				mHandler.sendEmptyMessageDelayed(1, 500);
				return false;
			}
		});
		drawUser.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (Common.isNotLogin())
				{
					Common.startActivityOptions(Main.this, Login.class);
					mHandler.sendEmptyMessageDelayed(1, 500);
				}
				if (Common.isLogin())
				{
					Common.startActivityOptions(Main.this, new Intent(Main.this, UserCentral.class),
						(Pair<View,String>)Pair.create(drawUser, "share_user")		
						/*(Pair<View,String>)Pair.create(mImgHead, "icon_pre"),
						(Pair<View,String>)Pair.create(txtUserName, "share_nick")*/);
				}
			}
		});
		setHead();
		myCallback = new InboxCallback();
		if (START)
			mHandler.sendEmptyMessageDelayed(10, 500);
    }
	
	private void setHead()
	{
		if (Common.mUser == null && Common.isNet(this) && Common.canLogin())
			new LoginThread().start();
		setIcon();
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
	}
	
	public void setIcon()
	{
		Common.setIcon(mImgHead, this, false);
		showPlugin();
	}
	
	public void checkVersion()
	{
		if (Common.VERSION_CODE != getResources().getInteger(R.integer.versionCode))
		{
			Util.showResultDialog(this, getString(R.string.abt_ver), "新版特性", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int s)
				{
					Common.update(Main.this, true);
				}
			});
		}
	}
	
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
		layoutTips.setVisibility(View.GONE);
	}
	
	public void setTip(Tips tip)
	{
		if (tip != null)
		{
			//if (!Common.HULUXIA)
			//drawSms.setVisibility(tip.openSMS ? View.VISIBLE : View.GONE);
			layoutTips.setVisibility(View.VISIBLE);
			txtCotation.setText(tip.message);
			txtCotation.start();
		}
		else
			closeTip();
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
		if (Common.isNotLogin())
		{
			Util.toastShortMessage(getApplicationContext(), "请先登录！");
			return;
		}
		Common.startActivityOptions(Main.this, TextReplace.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goCodeEditor(View v)
	{
		if (Common.isNotLogin())
		{
			Util.toastShortMessage(getApplicationContext(), "请先登录！");
			return;
		}
		Common.startActivityOptions(Main.this, CodeEditor.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goQQMessage(View v)
	{
		if (Common.isNotLogin())
		{
			Util.toastShortMessage(getApplicationContext(), "请先登录！");
			return;
		}
		Common.startActivityOptions(Main.this, WifiViews.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goBrowser(View v)
	{
		Common.startActivityOptions(Main.this, HtmlViewer.class);
		mHandler.sendEmptyMessageDelayed(1, 500);
	}
	
	public void goSms(View v)
	{
		if (Common.isNotLogin())
		{
			Util.toastShortMessage(getApplicationContext(), "请先登录！");
			return;
		}
		Util.showConfirmCancelDialog(this,android.R.string.dialog_alert_title, "请不要做非法的事情！", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface d, int s)
			{
				Common.startActivityOptions(Main.this, SmsBomb.class);
				mHandler.sendEmptyMessageDelayed(1, 500);
			}
		});
	}
	
	private void showPlugin()
	{
		if (Common.mUser != null)
		{
			drawSms.setVisibility(Common.isVipUser() ? View.VISIBLE : View.GONE);
			drawWifi.setVisibility(Common.isVipUser() ? View.VISIBLE : View.GONE);
			//drawCodeEditor.setVisibility(Common.IS_ACTIVE ? View.VISIBLE : View.GONE);
		}
		else
		{
			drawSms.setVisibility(View.GONE);
			drawWifi.setVisibility(View.VISIBLE);
			//drawCodeEditor.setVisibility(View.VISIBLE);
		}
	}
	
	public void goBack(View v)
	{
		Common.getCopyMsg(this);
		Common.gc();
		if (Common.isSupportMD())
			finishAndRemoveTask();
		else
			finish();
		ActivityManager.getInstance().FinishAllActivities();
	}
	
	private void onAudioDialog()
	{
		AlertDialog builder = new AlertDialog(this);
		builder.setTitle("提示");
		builder.setCancelable(false);
		builder.setMessage("学累了吧，要不要听听音乐放松下？");
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
	
	public void closeDrawered()
	{
		mDrawerLayout.closeDrawer(Gravity.START);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (Common.getInbox().getCount() > 0)
		{
			menu.findItem(R.id.item_newmsg).setVisible(true);
			menu.findItem(R.id.item_msg).setVisible(false);
		}
		else
		{
			menu.findItem(R.id.item_newmsg).setVisible(false);
			menu.findItem(R.id.item_msg).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//Common.myInbox.refreshMessage();
		if (Common.isLogin())
			Common.startActivityOptions(this, MyInbox.class);
		return super.onOptionsItemSelected(item);
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
	
	private boolean saveStatus()
	{
		/*if (Common.isCanUploadUserSetting())
			Common.updateUser();*/
		Common.getCopyMsg(this);
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putString("read_android", Util.Join(",", Common.READ_Android));
		editor.putString("read_android2", Util.Join(",", Common.READ_AndroidAdvance));
		editor.putString("read_javaee", Util.Join(",", Common.READ_J2EE));
		return editor.commit();
	}

	@Override
	protected void onResume()
	{
		mHandler.sendEmptyMessage(3);
		mHandler.sendEmptyMessage(5);
		if (Common.MUSIC)
		{
			if (soundThread == null || !soundThread.RUN)
			{
				soundThread = new SoundThread();
				soundThread.start();
			}
		}
		if (MyThemes.ISCHANGED)
		{
			MyThemes.ISCHANGED = false;
			if (Common.APP_BACK_ID == 0)
				drawUser.setBackgroundResource(MyThemes.getColorPrimary());
			else
			{
				if (Common.APP_BACK_ID == 100)
					drawUser.setBackground(Common.getHomeBack());
				else
					drawUser.setBackgroundResource(MyThemes.getDrawerBack());
			}
			if (MyThemes.homeTextColor != 0)
			{
				txtUserName.setTextColor(MyThemes.homeTextColor);
				txtSignature.setTextColor(MyThemes.homeTextColor);
				txtUserName.setShadowLayer(2f, 2f, 2f, MyThemes.homeTextShadow);
				txtSignature.setShadowLayer(2f, 2f, 2f, MyThemes.homeTextShadow);
			}
			//Toast.makeText(getApplicationContext(), MyThemes.homeTextColor + "|" + MyThemes.homeTextShadow, Toast.LENGTH_SHORT).show();
		}
		if (Common.IsChangeICON)
		{
			Common.IsChangeICON = false;
			setIcon();
		}
		if (Common.isAudio() && Common.AUDIO_STUDY_STATE >= 10)
			onAudioDialog();
		if (Common.isNoadv())
		{
			if (bannerView != null)
			{
				bannerLayout.removeAllViews();
				bannerView.destroy();
				bannerView = null;
			}
		}
		else
		{
			if (bannerView == null)
				initBanner();
		}
		if (Common.isNet(this))
			Common.getTestMsg();
		StatService.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		if (bannerView != null)
		{
			bannerLayout.removeAllViews();
			bannerView.destroy();
			bannerView = null;
		}
		super.onDestroy();
	}
	
	private void initBanner()
	{
		bannerView = new BannerView(this, ADSize.BANNER, AdvConfig.APPID, AdvConfig.Banner2);
		bannerView.setRefresh(30);
		bannerView.setADListener(new AbstractBannerADListener()
		{
			@Override
			public void onNoAD(AdError e)
			{
				ExceptionHandler.log("ad:"+e.getErrorCode(), e.getErrorMsg());
			}

			@Override
			public void onADReceiv()
			{

			}
		});
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
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
				if (!Common.SOUND.isCometed)
				{
					Common.SOUND.load();
				}
				if (Common.audioService != null && Common.audioService.isPlay())
				{
					while (Common.audioService.isPlay())
					{
						Thread.sleep(10);
						mHandler.sendEmptyMessage(8);
					}
					mHandler.sendEmptyMessage(9);
				}
			}
			catch (Exception er)
			{
				ExceptionHandler.log("Main.SoundThread", er.toString());
				mHandler.sendEmptyMessage(9);
			}
			finally
			{
				mHandler.sendEmptyMessage(5);
				RUN = false;
			}}
		}
	}
	
	class LoginThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				/*ExceptionHandler.KeyLog log = new ExceptionHandler.KeyLog("LOGIN");
				log.add("LoginMode", Common.AUTO_LOGIN_MODE);
				log.add("UserId", Common.USERID);
				log.add("UserPass", Common.USERPASS);
				log.add("CanLogin", Common.canLogin());
				log.toLog();*/
				if (Common.canLogin())
				{
					mHandler.sendEmptyMessage(4);
					if (Common.mUser == null)
					{
						Common.onLogin = 3;
						Common.Login(Main.this);
						while (Common.onLogin == 3)
						{
							Thread.sleep(500);
						}
						if (Common.onLogin == 2)
							mHandler.sendEmptyMessage(6);
					}
					else
						Common.onLogin = 0;
				}
			}
			catch (Exception er)
			{
				ExceptionHandler.log("mainLogin",er.toString());
				mHandler.sendEmptyMessage(6);
			}
			finally
			{
				mHandler.sendEmptyMessage(3);
				mHandler.sendEmptyMessage(7);
			}
		}
	}
	
	class InboxCallback implements InboxManager.InboxCallback
	{
		@Override
		public void onSuccess()
		{
			invalidateOptionsMenu();
		}
		@Override
		public void onFailure(){}
	}
	
	Handler mHandler = new Handler()
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
				case 2:
					if (Common.isSupportMD())
						toolbar.setTransitionName(null);
					checkVersion();
					break;
				case 3:
					if (Common.mUser != null)
					{
						txtUserName.setText(Common.mUser.nickname);
						txtSignature.setText(Common.mUser.mark);
						Common.getInbox().getMyInbox(System.currentTimeMillis(), myCallback);
					}
					else
					{
						if (Common.isNotLogin())
						{
							txtUserName.setText("请登录");
							txtSignature.setText("");
							closeDrawered();
						}
					}
					break;
				case 4:
					txtUserName.setText("正在登录...");
					break;
				case 5:
					if (Common.AUDIO && Common.MUSIC)
						drawMuic.setVisibility(View.VISIBLE);
					else
						drawMuic.setVisibility(View.GONE);
					if (!mDrawerLayout.isDrawerOpen(Gravity.START))
						setTip(Common.getTip());
					showPlugin();
					break;
				case 6:
					Common.onLogin = 0;
					Util.toastShortMessage(getApplicationContext(), "登录失败");
					break;
				case 7:
					if (Common.mUser != null)
						setIcon();
					break;
				case 8:
					txtAudioCode.setText(Common.audioService.mid);
					break;
				case 9:
					txtAudioCode.setText("");
					break;
				case 10:
					BmobUpdateAgent.update(getApplicationContext());
					QbSdk.initX5Environment(Main.this, null);
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onStop()
	{
		if (Common.isSupportMD())
			toolbar.setTransitionName(getString(R.string.shared));
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (!mDrawerLayout.isDrawerOpen(Gravity.START))
			{
				if((System.currentTimeMillis() - exitTime) > 2000)
				{
					if (saveStatus())
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
