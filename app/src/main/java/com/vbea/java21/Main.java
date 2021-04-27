package com.vbea.java21;

import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.net.Uri;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.DialogInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

/*import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;*/
import com.tencent.stat.StatService;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.fragment.AideFragment;
import com.vbea.java21.fragment.Android2Fragment;
import com.vbea.java21.fragment.AndroidFragment;
import com.vbea.java21.fragment.ChapterFragment;
import com.vbea.java21.fragment.DatabaseFragment;
import com.vbea.java21.fragment.JavaFragment;
import com.vbea.java21.fragment.KnowFragment;
import com.vbea.java21.adapter.FragmentAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.InboxManager;
import com.vbea.java21.ui.About;
import com.vbea.java21.ui.ActivityManager;
import com.vbea.java21.ui.AdminActivity;
import com.vbea.java21.ui.ApiWord;
import com.vbea.java21.ui.Help;
import com.vbea.java21.ui.HtmlViewer;
import com.vbea.java21.ui.Login;
import com.vbea.java21.ui.More;
import com.vbea.java21.ui.MyInbox;
import com.vbea.java21.ui.MyThemes;
import com.vbea.java21.ui.QRScannerActivity;
import com.vbea.java21.ui.SetDrawerImage;
import com.vbea.java21.ui.TextReplace;
import com.vbea.java21.ui.Themes;
import com.vbea.java21.ui.UserCentral;
import com.vbea.java21.ui.WifiViews;
import com.vbea.java21.update.MyUpdateAgent;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.widget.CustomTextView;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.data.Copys;
import com.vbea.java21.data.Tips;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

public class Main extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView mImgHead;
    private long exitTime = 0;
    private LinearLayout drawMuic, drawTheme, drawHelp, drawCodeEditor, layoutTips;
    private RelativeLayout drawUser;
    private TextView txtUserName, txtSignature, txtAudioCode, txtVip;
    private CustomTextView txtCotation;
    private boolean START = false;
    private SoundThread soundThread;
    private ViewGroup bannerLayout;
    //private UnifiedBannerView bannerView;
    private InboxCallback myCallback;
    private boolean menuReady = false;

    @Override
    protected void before() {
        MyThemes.initBackColor(this);
        setContentView(R.layout.main);
        StatService.registerActivityLifecycleCallbacks(this.getApplication());
    }

    @Override
    protected void after() {
        START = getIntent().getBooleanExtra("start", false);
        mDrawerLayout = bind(R.id.drawer_layout);
        mImgHead = bind(R.id.img_head);
        ViewPager viewpager = bind(R.id.viewpager);
        TabLayout tabLayout = bind(R.id.tabLayout);
        drawMuic = bind(R.id.drawer_music);
        drawTheme = bind(R.id.draw_item1);
        drawHelp = bind(R.id.drawer_helpLayout);
        //drawWifi = bind(R.id.drawer_wifiLayout);
        drawCodeEditor = bind(R.id.drawer_codeEditor);
        drawUser = bind(R.id.draw_user);
        layoutTips = bind(R.id.layout_tips);
        txtUserName = bind(R.id.draw_txtName);
        txtSignature = bind(R.id.draw_txtMark);
        txtAudioCode = bind(R.id.main_showAudioCode);
        bannerLayout = bind(R.id.mainBanner);
        txtCotation = bind(R.id.txt_quotation);
        txtVip = bind(R.id.draw_isVIP);
        mHandler.sendEmptyMessageDelayed(2, 2000);
        FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager());
        fa.addItem(new ChapterFragment(), getString(R.string.contacts));
        fa.addItem(new KnowFragment(), getString(R.string.javaadv));
        fa.addItem(new JavaFragment(), "J2EE");
        fa.addItem(new DatabaseFragment(), "SQL");
        fa.addItem(new AndroidFragment(), "安卓基础");
        fa.addItem(new Android2Fragment(), "安卓进阶");
        fa.addItem(new AideFragment(), "AIDE");
        viewpager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.gray2));
        MyThemes.ISCHANGED = true;
        Common.IsChangeICON = true;
        enableToolBar();
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(drawerToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View p1, float p2) {

            }

            @Override
            public void onDrawerOpened(View p1) {
                txtCotation.pause();
                if (Common.IsChangeICON)
                    setIcon();
                closeTip();
            }

            @Override
            public void onDrawerClosed(View p1) {
                setTip(Common.getTip());
            }

            @Override
            public void onDrawerStateChanged(int p1) {
                mDrawerLayout.requestFocus();
            }
        });

        drawTheme.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Common.startActivityOptions(Main.this, SetDrawerImage.class);
                mHandler.sendEmptyMessageDelayed(1, 500);
                return false;
            }
        });

        drawUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Common.isLogin()) {
                    Common.startActivityOptions(Main.this, new Intent(Main.this, UserCentral.class), Pair.create(drawUser, "share_user")
						/*(Pair<View,String>)Pair.create(mImgHead, "icon_pre"),
						(Pair<View,String>)Pair.create(txtUserName, "share_nick")*/);
                } else {
                    if (txtUserName.getText().toString().equals("正在登录..."))
                        return;
                    Common.startActivityOptions(Main.this, Login.class);
                    mHandler.sendEmptyMessageDelayed(1, 500);
                }
            }
        });

        txtCotation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VbeUtil.showResultDialog(Main.this, txtCotation.getText().toString(), "消息通知", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int i) {
                        setTip(Common.getTip());
                    }
                });
            }
        });

        myCallback = new InboxCallback();
        if (START) {
            if (Common.isNet(this) && Common.canLogin())
                onAutoLogin();
            mHandler.sendEmptyMessageDelayed(6, 500);
        }
        StatService.trackCustomEvent(this, "onCreate", "");
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

    public void setIcon() {
        //Glide.with(this).load(Common.getAvatarPath()).placeholder(R.drawable.head).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(mImgHead);
        Common.setIcon(mImgHead, true);
        showPlugin();
    }

    public void checkVersion() {
        if (Common.VERSION_CODE != BuildConfig.VERSION_CODE) {
            VbeUtil.showResultDialog(this, String.format(getString(R.string.abt_ver), BuildConfig.VERSION_NAME), "新版特性");
            Common.update(Main.this, true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        drawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    public void closeTip() {
        //if (layoutTips.getVisibility() != View.GONE)
        layoutTips.setVisibility(View.GONE);
    }

    public void setTip(Tips tip) {
        if (tip != null) {
            //if (!Common.HULUXIA)
            //drawSms.setVisibility(tip.openSMS ? View.VISIBLE : View.GONE);
            layoutTips.setVisibility(View.VISIBLE);
            txtCotation.setText(tip.message);
            txtCotation.start();
        } else
            closeTip();
    }

    //抽屉
    public void goTheme(View v) {
        Common.startActivityOptions(Main.this, Themes.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goSetting(View v) {
        Common.startActivityOptions(Main.this, Setting.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goAPI(View v) {
        Common.startActivityOptions(Main.this, ApiWord.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goMusic(View v) {
        Common.startActivityOptions(Main.this, More.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goHelp(View v) {
        Common.startActivityOptions(Main.this, Help.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goEditor(View v) {
        if (!Common.isLogin()) {
            Util.toastShortMessage(getApplicationContext(), "请先登录！");
            return;
        }
        Common.startActivityOptions(Main.this, TextReplace.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goCodeEditor(View v) {
        Common.startActivityOptions(Main.this, AdminActivity.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goScanner(View v) {
        startActivity(new Intent(Main.this, QRScannerActivity.class));
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goQQMessage(View v) {
        if (!Common.isLogin()) {
            Util.toastShortMessage(getApplicationContext(), "请先登录！");
            return;
        }
        Common.startActivityOptions(Main.this, WifiViews.class);
        mHandler.sendEmptyMessageDelayed(1, 500);
    }

    public void goBrowser(View v) {
        Common.startActivityOptions(Main.this, HtmlViewer.class);
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

    private void showPlugin() {
        if (Common.HULUXIA)
            drawHelp.setVisibility(View.GONE);
        if (Common.isLogin()) {
            //drawSms.setVisibility(Common.isVipUser() ? View.VISIBLE : View.GONE);
            //drawWifi.setVisibility(Common.isVipUser() ? View.VISIBLE : View.GONE);
            drawCodeEditor.setVisibility(Common.isAdminUser() ? View.VISIBLE : View.GONE);
        } else {
            //drawSms.setVisibility(View.GONE);
            //drawWifi.setVisibility(View.VISIBLE);
            drawCodeEditor.setVisibility(View.GONE);
        }
    }

    public void goBack(View v) {
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

    private void onAudioDialog() {
        if (Common.isAudio() && Common.AUDIO_STUDY_STATE >= 20) {
            MyAlertDialog builder = new MyAlertDialog(this);
            builder.setTitle("提示");
            builder.setCancelable(false);
            builder.setMessage("学累了吧，要不要去音乐小窝听听音乐放松下？");
            builder.setPositiveButton("去看看", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int p) {
                    Common.AUDIO_STUDY_STATE = 0;
                    Common.startActivityOptions(Main.this, More.class);
                }
            });
            builder.setNegativeButton("不用了", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int p) {
                    Common.AUDIO_STUDY_STATE = 0;
                }
            });
            builder.show();
        }
    }

    public void closeDrawered() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuReady = false;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuReady) {
            List<Copys> msg = Common.getCopyMsg();
            if (msg != null) {
                menuReady = true;
                if (msg.size() > 0) {
                    menu.clear();
                    for (int i = 0; i < msg.size(); i++)
                        menu.add(10, i, 0, msg.get(i).getTitle());
                    getMenuInflater().inflate(R.menu.main_menu, menu);
                }
                //menu.findItem(R.id.item_alipay).setVisible(true);
                //menu.findItem(R.id.item_alipay).setTitle(msg.getTitle());
            }
        }
        //else
        //menu.findItem(R.id.item_alipay).setVisible(false);
        if (Common.getInbox().getCount() > 0) {
            menu.findItem(R.id.item_newmsg).setVisible(true);
            menu.findItem(R.id.item_msg).setVisible(false);
        } else {
            menu.findItem(R.id.item_newmsg).setVisible(false);
            menu.findItem(R.id.item_msg).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == 10) {
            showDynamicMenu(item.getItemId());
            return true;
        }
        //Common.myInbox.refreshMessage();
        switch (item.getItemId()) {
            case R.id.item_msg:
            case R.id.item_newmsg:
                if (Common.isLogin()) {
                    Common.startActivityOptions(this, MyInbox.class);
                    Common.getInbox().clearCount();
                    invalidateOptionsMenu();
                } else
                    Common.startActivityOptions(this, Login.class);
                break;
            case R.id.item_about:
                Common.startActivityOptions(Main.this, About.class);
                break;
			/*case R.id.item_alipay:
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setData(Uri.parse(Common.getCopyMsg().getUrl()));
					startActivity(intent);
					Util.toastLongMessage(getApplicationContext(), "感谢您的支持，领取后请记得使用哦！");
				} catch (Exception e) {
					Util.toastShortMessage(getApplicationContext(), "未安装支付宝");
				}
				break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDynamicMenu(int id) {
        final Copys msg = Common.getCopyMsg(id);
        if (msg != null) {
            if (msg.getType() == 0)
                VbeUtil.showResultDialog(this, msg.getMessage(), msg.getTitle());
            else if (msg.getType() == 1) {
                VbeUtil.showConfirmCancelDialog(this, msg.getTitle(), msg.getMessage(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int p) {
                        Util.addClipboard(Main.this, msg.getResult());
                        Util.toastShortMessage(getApplicationContext(), "复制成功");
                    }
                });
            } else {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                if (msg.getType() == 2)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                else if (msg.getType() == 3)
                    intent.setClass(this, HtmlViewer.class);
                if (!Util.isNullOrEmpty(msg.getUrl()))
                    intent.setData(Uri.parse(msg.getUrl()));
                if (!Util.isNullOrEmpty(msg.getMessage())) {
                    VbeUtil.showConfirmCancelDialog(this, msg.getTitle(), msg.getMessage(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int p) {
                            VbeUtil.startActivityOptions(Main.this, intent);
                            if (!Util.isNullOrEmpty(msg.getResult()))
                                Util.toastLongMessage(getApplicationContext(), msg.getResult());
                        }
                    });
                } else {
                    VbeUtil.startActivityOptions(this, intent);
                    if (!Util.isNullOrEmpty(msg.getResult()))
                        Util.toastLongMessage(getApplicationContext(), msg.getResult());
                }
            }
        }
    }

    @Override
    protected void onPause() {
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

    private void saveStatus() {
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

    private void showDrawerImage() {
        if (MyThemes.ISCHANGED) {
            MyThemes.ISCHANGED = false;
            if (Common.APP_BACK_ID == 100)
                drawUser.setBackground(Common.getHomeBack(this));
            else
                drawUser.setBackgroundResource(MyThemes.getDrawerBack());
            if (MyThemes.homeTextColor != 0) {
                txtUserName.setTextColor(MyThemes.homeTextColor);
                txtSignature.setTextColor(MyThemes.homeTextColor);
                txtUserName.setShadowLayer(2f, 2f, 2f, MyThemes.homeTextShadow);
                txtSignature.setShadowLayer(2f, 2f, 2f, MyThemes.homeTextShadow);
            }
        }
    }

    @Override
    protected void onResume() {
        showUserInfo();
        mHandler.sendEmptyMessage(5);
        if (Common.MUSIC) {
            if (soundThread == null || !soundThread.RUN) {
                soundThread = new SoundThread();
                soundThread.start();
            }
        }
        showDrawerImage();
        onAudioDialog();
        //showBanner();
        if (!menuReady && Common.isNet(this))
            Common.getTestMsg();
        StatService.onResume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
		/*if (bannerView != null) {
			bannerLayout.removeAllViews();
			bannerView.destroy();
			bannerView = null;
		}*/
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
	
	/*private void initBanner() {
		Log.i("--initBanner--", "initBanner");
		bannerView = new UnifiedBannerView(this, AdvConfig.APPID, AdvConfig.NewBannerHome, new UnifiedBannerADListener() {
			@Override
			public void onNoAD(AdError adError) {
				Log.i("--onNoAD--", adError.getErrorMsg());
			}

			@Override
			public void onADReceive() {
				Log.i("--onADReceive--", "Banner加载完成");
			}

			@Override
			public void onADExposure() {
				Log.i("--onADExposure--", "initBanner");
			}

			@Override
			public void onADClosed() {
				Log.i("--onADClosed--", "initBanner");
			}

			@Override
			public void onADClicked() {
				Log.i("--onADClicked--", "initBanner");
			}

			@Override
			public void onADLeftApplication() {
				Log.i("--onADLeftApplication--", "initBanner");
			}

			@Override
			public void onADOpenOverlay() {
				Log.i("--onADOpenOverlay--", "initBanner");
			}

			@Override
			public void onADCloseOverlay() {
				Log.i("--onADCloseOverlay--", "initBanner");
			}
		});
		bannerView.setRefresh(30);
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
	}*/
	
	/*private void showBanner() {
		if (Common.isNoadv() && !Common.isHuluxiaUser()) {
			if (bannerView != null) {
				bannerLayout.removeAllViews();
				bannerView.destroy();
				bannerView = null;
			}
		} else {
			if (bannerView == null)
				initBanner();
		}
	}*/

    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x, Math.round(screenSize.x / 6.4F));
    }

    class SoundThread extends Thread implements Runnable {
        public boolean RUN = false;

        public void run() {
            synchronized (this) {
                try {
                    RUN = true;
                    if (Common.SOUND == null)
                        Common.SOUND = new SoundLoad(Main.this);
                    if (!Common.SOUND.isCompeted) {
                        Common.SOUND.load();
                    }
                    if (Common.audioService != null && Common.audioService.isPlay()) {
                        while (Common.audioService.isPlay()) {
                            Thread.sleep(10);
                            mHandler.sendEmptyMessage(3);
                        }
                        mHandler.sendEmptyMessage(4);
                    }
                } catch (Exception er) {
                    ExceptionHandler.log("Main.SoundThread", er.toString());
                    mHandler.sendEmptyMessage(4);
                } finally {
                    mHandler.sendEmptyMessage(5);
                    RUN = false;
                }
            }
        }
    }

    public void onAutoLogin() {
        try {
            if (!Common.isLogin()) {
                mHandler.sendEmptyMessage(10);
                Common.Login(Main.this, new Common.LoginListener() {
                    @Override
                    public void onLogin(int code) {
                        if (code == 1) {
                            if (!Common.OldSerialNo.equals(Common.mUser.serialNo) && !Util.isNullOrEmpty(Common.OldSerialNo)) {
                                Util.toastShortMessage(Main.this, "帐号在其他设备登录，您的登录态已失效，请重新登录");
                                Common.Logout(Main.this);
                            } else {
                                Common.IsChangeICON = true;
                            }
                            showUserInfo();
                        }
                        if (code == 0 || code == 2) {
                            toastLoginError("0x000" + code);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        ExceptionHandler.log("mainLogin", error);
                        toastLoginError("0x0001");
                    }
                });
            }
        } catch (Exception er) {
            ExceptionHandler.log("mainLogin", er.toString());
            toastLoginError("0x0003");
        }
    }

    private void showUserInfo() {
        if (Common.isLogin()) {
            if (Util.isNullOrEmpty(Common.mUser.nickname))
                txtUserName.setText("");
            else
                txtUserName.setText(Common.mUser.nickname);
            if (Util.isNullOrEmpty(Common.mUser.mark))
                txtSignature.setText("");
            else
                txtSignature.setText(Common.mUser.mark);
            Common.getInbox().getMyInbox(System.currentTimeMillis(), myCallback);
            Common.showUserRole(txtVip);
        } else {
            txtUserName.setText("请登录");
            txtSignature.setText("");
            txtVip.setVisibility(View.GONE);
            closeDrawered();
        }
        mHandler.sendEmptyMessage(7);
    }

    private void toastLoginError(String code) {
        toastShortMessage("登录失败(" + code + ")");
        txtUserName.setText("请登录");
    }

    private class InboxCallback implements InboxManager.InboxCallback {
        @Override
        public void onSuccess() {
            invalidateOptionsMenu();
        }

        @Override
        public void onFailure() {
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    finish();
                    break;
                case 1:
                    closeDrawered();
                    break;
                case 2:
					/*if (Common.isSupportMD())
						toolbar.setTransitionName(null);*/
                    checkVersion();
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
                    if (!mDrawerLayout.isDrawerOpen(Gravity.START))
                        setTip(Common.getTip());
                    showPlugin();
                    break;
                case 6:
                    //BmobUpdateAgent.setUpdateOnlyWifi(false);
                    //BmobUpdateAgent.update(getApplicationContext());
                    MyUpdateAgent.update(Main.this);
                    //QbSdk.initX5Environment(Main.this, null);
                    break;
                case 7:
                    setIcon();
                    Common.IsChangeICON = false;
                    break;
                case 10:
                    txtUserName.setText("正在登录...");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onStop() {
		/*if (Common.isSupportMD())
			toolbar.setTransitionName(getString(R.string.shared));*/
        StatService.onStop(this);
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    //if (saveStatus())
                    Util.toastShortMessage(getApplicationContext(), "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else
                    onExit();
            } else
                closeDrawered();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(Gravity.START))
                closeDrawered();
            else
                mDrawerLayout.openDrawer(Gravity.START);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onExit() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
		/*if (Common.isSupportMD())
			finishAndRemoveTask();
		else
			finish();*/
    }
}
