package com.vbea.java21.classes;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.Pair;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.vbea.java21.BuildConfig;
import com.vbea.java21.ui.DownloadService;
import com.vbea.java21.R;
import com.vbea.java21.ui.ActivityManager;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.audio.AudioService;
import com.vbea.java21.data.Users;
import com.vbea.java21.data.Tips;
import com.vbea.java21.data.Copys;
import com.vbes.util.EasyPreferences;
import com.vbes.util.VbeUtil;
import com.vbes.util.secret.Dec;
import com.vbes.util.secret.Key;
import com.vbes.util.secret.ProdKey;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.exception.BmobException;

public class Common
{
	private static boolean IsRun = false;//是否运行
	public static int APP_THEME_ID = -1;//主题
	public static int APP_BACK_ID = 0;//背景图片
	public static int VERSION_CODE = 0;//版本号
	public static int AUTO_LOGIN_MODE = 0;//自动登录类型，1为普通，2为QQ，3为微信
	public static String SDATE;//激活凭证
	public static String KEY;//密钥
	public static String SID;//激活时间
	public static String USERID;//用户名
	private static String USERPASS;//加密密码
	public static boolean IS_ACTIVE = false;//是否注册
	public static boolean NO_ADV = false;//去广告
	public static boolean WEL_ADV = true;//欢迎页广告
	//public static boolean EYESHIELD = false;//护眼模式
	public static boolean isShowFile = true;//显示所有文件
	public static SoundLoad SOUND = null;//音乐池
	public static boolean AUDIO = false;//是否显示音乐
	public static boolean MUSIC = true;//是否开启音乐
	public static boolean TIPS = true;//是否开启消息通知
	public static int PIANO_SIZE = 0;
	public final static boolean HULUXIA = false;//是否葫芦侠特别版
	public static Users mUser;
	public static boolean IsChangeICON = false;
	public static int AUDIO_STUDY_STATE = 0;
	public static int JAVA_TEXT_SIZE = 2;
	public static AudioService audioService;
	private static final String defaultKey = "JAVA8-APP-KEY21-APK-VBEST";
	public static final String FileProvider = "com.vbea.java21.fileprovider";
	public static final String ExterPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String LocalPath = ExterPath + "/ZDApp/";
	private static final String DataPath = "/data/data/com.vbea.java21/file/";
	private static final String DownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Java21/";
	private static List<Tips> mTips = null;
	public static InboxManager myInbox;
	private static long lastTipsTime;
	private static List<Copys> copyMsgs;
	public static String OldSerialNo, OldLoginDate;
	public static void start(Context context) {
		//startBmob(context);
		if (IsRun) {
			return;
		}
		init();
		if (BmobWrapper.getInstance() == null)
			Bmob.initialize(context, "1aa46b02605279e1a84935073af9fc82");
		IsRun = true;
		ReadUtil.init(context);
		if (VbeUtil.isNullOrEmpty(EasyPreferences.getString("key")) || VbeUtil.isNullOrEmpty(EasyPreferences.getString("date"))) {
			IS_ACTIVE = false;
			EasyPreferences.putBoolean("app", false);
		}
		if (IS_ACTIVE && regist(KEY)) {
            EasyPreferences.putBoolean("app", true);
            EasyPreferences.putBoolean("active", true);
		} else {
			IS_ACTIVE = false;
            EasyPreferences.putBoolean("app", false);
			EasyPreferences.putBoolean("active", false);
			EasyPreferences.putString("key", defaultKey);
		}
		if (getDrawerBack(context) == null)
            EasyPreferences.putInt("back", 0);
        EasyPreferences.apply();
		//init();
		SocialShare.onStart(context);
		if (isNet(context))
			getTips();
	}

	public static boolean isRun() {
		return IsRun;
	}
	
	public static void reStart(Context context) {
		IsRun = false;
		start(context);
	}
	
	public static void update(Context context, boolean check) {
		int code = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		code = BuildConfig.VERSION_CODE;
		if (check)
			EasyPreferences.putInt("check", code);
		else {
			String codes = "";
			try {
				Dec dec = new Dec();
				codes = dec.encrypt(format.format(new Date()));
				File cathe = new File(LocalPath + File.separator + ".nomedia");
				if (!cathe.exists())
					cathe.createNewFile();
			} catch (Exception e) {
			}
			EasyPreferences.putInt("check", code);
			EasyPreferences.putString("checkCode", codes);
		}
		EasyPreferences.commit();
		init();
	}
	
	public static String getSettingJson(SettingUtil utils) throws Exception {
		utils.addSettings(SettingUtil.SET_THEME, APP_THEME_ID);
		utils.addSettings(SettingUtil.SET_BACKIMG, APP_BACK_ID);
		utils.addSettings(SettingUtil.SET_FONTSIZE, JAVA_TEXT_SIZE);
		return utils.getJsonString();
	}
	
	public static boolean checkUpdateSetting(Context context) {
		try {
			SettingUtil utils = new SettingUtil();
			if (!Util.isNullOrEmpty(mUser.settings)) {
				utils.synaxSetting(mUser.settings);
				int back_id = utils.getIntValue(SettingUtil.SET_BACKIMG);
				int theme_id = utils.getIntValue(SettingUtil.SET_THEME);
				if (!mUser.serialNo.equals(OldSerialNo) || APP_BACK_ID != back_id || APP_THEME_ID != theme_id) {
					APP_BACK_ID = back_id;
					APP_THEME_ID = theme_id;
					JAVA_TEXT_SIZE = utils.getIntValue(SettingUtil.SET_FONTSIZE);
				}
				return true;
			}
		} catch (Exception e) {
			ExceptionHandler.log("checkUpdateSetting", e);
		}
		return false;
	}
	
	public static void updateUserLogin(Context context) {
		if (HULUXIA)//add code on 20180707
			mUser.role = 3;
		if (mUser.role == null)
			mUser.role = 1;
		if (Util.isNullOrEmpty(mUser.serialNo)) {
			OldSerialNo = "";
			mUser.serialNo = "";
		}
		else
			OldSerialNo = mUser.serialNo;
		Date now = new Date();
		if (mUser.lastLogin != null) {
			OldLoginDate = mUser.lastLogin.getDate();
			if (mUser.dated == null)
				mUser.dated = 1;
			mUser.device = Build.DEVICE; //Util.getDeviceId(context);
			mUser.serialNo = Build.MODEL; //Util.getSerialNo(context);
			Date loginDate = new Date(BmobDate.getTimeStamp(mUser.lastLogin.getDate()));
			if (loginDate.getDate() != now.getDate())
			{
				if (now.getYear() >= loginDate.getYear() || now.getMonth() >= loginDate.getMonth())
				{
					if (mUser.dated != null)
						mUser.dated += 1;
				}
			}
			mUser.lastLogin = new BmobDate(now);
			updateUser();
		} else {
			mUser.lastLogin = new BmobDate(now);
			OldLoginDate = mUser.lastLogin.getDate();
			mUser.dated = 1;
			updateUser();
		}
	}
	
	public static void updateUser() {
		try {
			if (mUser == null)
				return;
			Users user = new Users();
			user.setObjectId(mUser.getObjectId());
			user.psd = mUser.psd;
			//user.role = mUser.role;
			user.nickname = mUser.nickname;
			user.birthday = mUser.birthday;
			user.address = mUser.address;
			user.key = mUser.key;
			user.mark = mUser.mark;
			user.gender = mUser.gender;
			user.icon = mUser.icon;
			user.qq = mUser.qq;
			user.qqId = mUser.qqId;
			user.wxId = mUser.wxId;
			user.weixin = mUser.weixin;
			user.mobile = mUser.mobile;
			user.settings = mUser.settings;
			user.lastLogin = mUser.lastLogin;
			user.dated = mUser.dated;
			user.device = mUser.device;
			user.serialNo = mUser.serialNo;
			user.update(new UpdateListener() {
				public void done(BmobException e) {
					if (e != null)
						ExceptionHandler.log("Bmob_updateUser", e.toString());
				}
			});
		} catch (Exception e) {
			ExceptionHandler.log("updateUser", e.toString());
		}
	}
	
	private static void init() {
		APP_THEME_ID = EasyPreferences.getInt("theme", 0);
		VERSION_CODE = EasyPreferences.getInt("check", 0);
		APP_BACK_ID = EasyPreferences.getInt("back", 0);
		SDATE = EasyPreferences.getString("checkCode", "");
		IS_ACTIVE = EasyPreferences.getBoolean("app", false);
		MUSIC = EasyPreferences.getBoolean("music", true);
		KEY = EasyPreferences.getString("key", "");
		SID = EasyPreferences.getString("date","");
		NO_ADV = EasyPreferences.getBoolean("noadv", false);
		WEL_ADV = EasyPreferences.getBoolean("weladv", true);
		USERID = EasyPreferences.getString("uid", "");
		USERPASS = EasyPreferences.getString("sid", "");
		AUTO_LOGIN_MODE = EasyPreferences.getInt("loginmode", 0);
		TIPS = EasyPreferences.getBoolean("tips", true);
		JAVA_TEXT_SIZE = EasyPreferences.getInt("java_size", 2);
		PIANO_SIZE = EasyPreferences.getInt("piano_size", 0);
	}
	
	public static InboxManager getInbox() {
		if (myInbox == null)
			myInbox = new InboxManager();
		return myInbox;
	}
	
	public static boolean canLogin() {
		if (AUTO_LOGIN_MODE <= 0 || USERID.equals("") || USERPASS.equals("") || mUser != null || !IS_ACTIVE)
			return false;
		return true;
	}
	
	public static boolean isAudio() {
		if (AUDIO && audioService != null) {
			if (audioService.isPlay())
				return false;
		}
		return AUDIO;
	}
	
	public static boolean isNoadv() {
		return (Common.IS_ACTIVE && Common.NO_ADV);
	}
	
	public static boolean isWeladv() {
		return (!Common.IS_ACTIVE || Common.WEL_ADV);
	}
	
	public static void showUserRole(TextView txtVip) {
		if (mUser.role != null)
			showUserRole(txtVip, mUser.role);
		else
			txtVip.setVisibility(View.GONE);
	}

	public static void showUserRole(TextView txtVip, int role) {
		switch (role) {
			case 10:
				txtVip.setText("管理员");
				txtVip.setBackgroundResource(R.drawable.ic_bg_svip);
				txtVip.setTextColor(-3007950);
				txtVip.setVisibility(View.VISIBLE);
				break;
			case 11:
				txtVip.setText("VIP");
				txtVip.setBackgroundResource(R.drawable.ic_bg_vip);
				txtVip.setTextColor(-1);
				txtVip.setVisibility(View.VISIBLE);
				break;
			case 12:
				txtVip.setText("SVIP");
				txtVip.setBackgroundResource(R.drawable.ic_bg_svip);
				txtVip.setTextColor(-3007950);
				txtVip.setVisibility(View.VISIBLE);
				break;
			default:
				txtVip.setVisibility(View.GONE);
		}
 	}
	
	public static boolean isVipUser() {
		if (mUser != null && mUser.role != null && IS_ACTIVE)
			return mUser.role >= 10;
		return false;
	}
	
	public static boolean isAdminUser() {
		if (mUser != null && mUser.role != null)
			return mUser.role == 10;
		return false;
	}
	
	public static boolean isSVipUser() {
		if (mUser != null && mUser.role != null && IS_ACTIVE)
			return mUser.role == 10 || mUser.role == 12;
		return false;
	}
	
	public static boolean isHuluxiaAd() {
		if (mUser != null && mUser.role != null && mUser.role != 3 && IS_ACTIVE)
			return false;
		return true;
	}
	
	public static boolean isHuluxiaUser() {
		if (mUser != null && mUser.role != null)
			return mUser.role == 3;
		return HULUXIA;
	}

	//自动登录
	public static void Login(Context context, LoginListener listener) throws Exception {
		if (AUTO_LOGIN_MODE == 1)
			Login(context, USERID, USERPASS, true, listener);
		else if (AUTO_LOGIN_MODE == 2)
			qqLogin(context, USERPASS, true, listener);
		else if (AUTO_LOGIN_MODE == 3)
			wxLogin(context, USERPASS, true, listener);
	}

	public static void qqLogin(Context context, String openId, boolean isAuto, LoginListener listener) throws Exception {
		socialLogin(context, "qqId", openId, isAuto, 2, listener);
	}

	public static void wxLogin(Context context, String wxId, boolean isAuto, LoginListener listener) throws Exception {
		socialLogin(context, "wxId", wxId, isAuto, 3, listener);
	}
	
	private static void socialLogin(final Context context, String column, String openId, boolean isAuto, int mode, final LoginListener listener) throws Exception {
		BmobQuery<Users> sql = new BmobQuery<Users>();
		sql.addWhereEqualTo(column, openId);
		sql.findObjects(new FindListener<Users>() {
			@Override
			public void done(List<Users> list, BmobException e) {
				if (e == null) {
					if (list.size() > 0) {
						mUser = list.get(0);
						USERID = mUser.name;
						if (!mUser.valid) {
							if (listener != null)
								listener.onLogin(2);
							return;
						}
						if (!isAuto) {
							saveLoginData(openId, mode);
						}
						updateUserLogin(context);
						if (listener != null)
							listener.onLogin(1);
						IsChangeICON = true;
					} else {
						if (listener != null)
							listener.onLogin(0);
					}
				} else {
					//ExceptionHandler.log("登录失败:"+e.toString());
					if (listener != null)
						listener.onError(e.toString());
				}
			}
		});
	}
	
	public static void Login(final Context context, final String username, final String pasdword, final boolean isAuto, final LoginListener listener) throws Exception {
		BmobQuery<Users> sql1 = new BmobQuery<Users>();
		sql1.addWhereEqualTo("name", username);
		BmobQuery<Users> sql2 = new BmobQuery<Users>();
		sql2.addWhereEqualTo("email", username);
		BmobQuery<Users> sql3 = new BmobQuery<Users>();
		sql3.addWhereEqualTo("mobile", username);
		List<BmobQuery<Users>> sqls = new ArrayList<BmobQuery<Users>>();
		sqls.add(sql1);
		sqls.add(sql2);
		sqls.add(sql3);
		BmobQuery<Users> sql = new BmobQuery<Users>();
		sql.or(sqls);
		sql.addWhereEqualTo("psd", pasdword);
		//sql.addWhereEqualTo("valid", true);
		sql.findObjects(new FindListener<Users>() {
			@Override
			public void done(List<Users> list, BmobException e) {
				if (e == null) {
					if (list.size() > 0) {
						Common.mUser = list.get(0);
						USERID = mUser.name;
						if (!mUser.valid) {
							if (listener != null)
								listener.onLogin(2);
							mUser = null;
							return;
						}
						if (!isAuto) {
                            saveLoginData(Common.mUser.psd, 1);
						}
						updateUserLogin(context);
						if (listener != null)
							listener.onLogin(1);
						IsChangeICON = true;
					} else {
						if (listener != null)
							listener.onLogin(0);
					}
				} else {
					//ExceptionHandler.log("登录失败:"+e.toString());
					if (listener != null)
						listener.onError(e.toString());
				}
			}
		});
	}

	public static void saveLoginData(String sid, int mode) {
		EasyPreferences.putString("uid", Common.mUser.name);
		EasyPreferences.putString("sid", sid);
		EasyPreferences.putInt("loginmode", mode);
		if (!IS_ACTIVE) {//自动激活
			KEY = mUser.key;
			if (regist(KEY)) {
				@SuppressLint("SimpleDateFormat")
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				IS_ACTIVE = true;
				EasyPreferences.putBoolean("autok", true);
				EasyPreferences.putString("date", format.format(new Date()));
				EasyPreferences.putBoolean("app", true);
			}
		}
		if (!isVipUser())
			EasyPreferences.putBoolean("weladv", true);
		EasyPreferences.putString("key", KEY);
		EasyPreferences.commit();
	}
	
	public static void Logout() {
		mUser = null;
		IsChangeICON = true;
		if (myInbox != null) {
			myInbox.logout();
			myInbox = null;
		}
	}
	
	public static void Logout(Context context) {
		mUser = null;
		IsChangeICON = true;
		EasyPreferences.putInt("loginmode", 0);
		EasyPreferences.apply();
		AUTO_LOGIN_MODE = 0;
	}
	
	public static boolean isLogin() {
		return (mUser != null);
	}
	
	private static boolean regist(String key) {
		try {
			if (key.length() == 25 && !key.equals(defaultKey)) {
				Key plug = new ProdKey(key).getInstance();
				return plug.verify();
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isNet(Context context) {
		ConnectivityManager zdapp = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (zdapp == null)
			return false;
		else {
			NetworkInfo[] info = zdapp.getAllNetworkInfo();
			if (info != null) {
				for (int i=0; i<info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						return true;
				}
				//zdapp.getActiveNetworkInfo().isAvailable()
			}
		}
		return false;
	}

	private static String getLocalPath(Context context) {
		if (Util.hasAllPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			return LocalPath;
		}
		return DownloadPath;
	}

	public static String getUpdatePath(Context context) {
		return getLocalPath(context) + "apk/";
	}
	
	public static String getCachePath(Context context) {
		return getLocalPath(context) + "Cache/";
	}

	public static String getDownloadPath(Context context) {
		return getLocalPath(context) + "Download/";
	}
	
	public static String getIconPath(Context context) {
		return getLocalPath(context) + "Portrait/";
	}

	public static String getAvatarPath(Context context) {
		if (mUser != null)
			return getIconPath(context)+ mUser.name + ".jpg";
		else
			return "";
	}
	
	public static String getDrawImagePath(Context context) {
		return getIconPath(context) + "back.jpg";
	}
	
	public static String getTempImagePath(Context context) {
		return getCachePath(context) + "temp.jpg";
	}

	@Deprecated
	public static Bitmap getIcon(Context context) {
		if (mUser.icon != null) {
			File path = new File(getIconPath(context));
			if (!path.exists()) {
				path.mkdirs();
			}
			File file = new File(path, mUser.icon.getFilename());
			if (file.exists())
				return BitmapFactory.decodeFile(file.getAbsolutePath());
			else {
				try {
					mUser.icon.download(file, new DownloadFileListener() {
						@Override
						public void done(String p1, BmobException p2) {
							
						}

						@Override
						public void onProgress(Integer p1, long p2) {

						}
					});
				} catch (Exception e) {
					ExceptionHandler.log("getIcon", e.toString());
				}
			}
		}
		return null;
	}
	
	public static void setIcon(ImageView v, boolean round) {
		//if (!downed)
			//v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.head)));
		if (mUser != null) {
			File path = new File(getIconPath(v.getContext()));
			if (!path.exists())
				path.mkdirs();
			/*if (round)
				Glide.with(v.getContext()).asBitmap().onlyRetrieveFromCache(false).skipMemoryCache(true).load(new File(getAvatarPath())).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.head_circle)).into(v);
			else
				Glide.with(v.getContext()).asBitmap().skipMemoryCache(IsChangeICON).load(new File(getAvatarPath())).placeholder(R.mipmap.head).into(v);*/
			File file = new File(getAvatarPath(v.getContext()));
			if (file.exists()) {
				if (round)
					v.setImageDrawable(getRoundedIconDrawable(v.getContext(), BitmapFactory.decodeFile(file.getAbsolutePath())));
				else
					v.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
			} else {
				if (round)
					v.setImageResource(R.drawable.head_circle);
				else
					v.setImageResource(R.drawable.head);
			}
			/*else {
				if (downed) {
					v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.head)));
					return;
				}
				try {
					mUser.icon.download(file, new DownloadFileListener() {
						@Override
						public void done(String p1, BmobException p2) {
							setIcon(v, context, true);
						}

						@Override
						public void onProgress(Integer p1, long p2) {
						}
					});
				} catch (Exception e) {
					ExceptionHandler.log("getIcon", e.toString());
				}
			}*/
		}
	}
	
	public static void setMyIcon(ImageView v, Context context) {
		if (mUser != null && mUser.icon != null) {
			File file = new File(getAvatarPath(v.getContext()));
			if (file.exists())
				v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeFile(file.getAbsolutePath())));
			else
				v.setImageResource(R.drawable.head_circle);
		}
	}
	
	public static BitmapDrawable getHomeBack(Context context) {
		Bitmap bit = getDrawerBack(context);
		if (bit != null)
			return new BitmapDrawable(bit);
		return null;
	}
	
	public static Bitmap getDrawerBack(Context context) {
		try {
			File path = new File(getIconPath(context));
			if (!path.exists())
				path.mkdirs();
			File file = new File(getDrawImagePath(context));
			if (file.exists())
				return BitmapFactory.decodeFile(file.getAbsolutePath());
		} catch (Exception e) {
			
		}
		return null;
	}

	public static Bitmap getUserIcon(Context context, String username) {
		File file = new File(Common.getCachePath(context), username + ".png");
		if (file.exists())
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		return null;
	}


	public static RoundedBitmapDrawable getRoundedIconDrawable(Context context, Bitmap src) {
		if (src != null) {
			RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(context.getResources(), src);
			rbd.setCornerRadius(src.getWidth()/2);
			rbd.setAntiAlias(true);//设置反走样
			return rbd;
		}
		return null;
	}
	
	public static boolean isMyUser(String name) {
		if (mUser != null && !Util.isNullOrEmpty(name)) {
			return mUser.name.equals(name);
		}
		return false;
	}
	
	public static boolean isContainsUser(String nameList) {
		if (mUser != null && !Util.isNullOrEmpty(nameList)) {
			return nameList.contains(mUser.name);
		}
		return false;
	}
	
	public static String getUsername() {
		if (mUser != null)
			return mUser.name;
		return "";
	}
	
	public static void getTestMsg() {
		BmobQuery<Copys> sql = new BmobQuery<>();
		sql.addWhereEqualTo("enable", true);
		sql.findObjects(new FindListener<Copys>() {
			@Override
			public void done(List<Copys> list, BmobException e) {
				if (e == null && list.size() > 0) {
					copyMsgs = list;
				}
			}
		});
	}
	
	public static List<Copys> getCopyMsg() {
		return copyMsgs;
	}
	
	public static Copys getCopyMsg(int index) {
		return copyMsgs.get(index);
	}
	
	public static void getTips() {
		lastTipsTime = System.currentTimeMillis();
		BmobQuery<Tips> sql1 = new BmobQuery<>();
		sql1.addWhereGreaterThanOrEqualTo("dates", new BmobDate(new Date()));
		BmobQuery<Tips> sql2 = new BmobQuery<>();
		sql2.addWhereEqualTo("islong", true);
		List<BmobQuery<Tips>> sqls = new ArrayList<>();
		sqls.add(sql1);
		sqls.add(sql2);
		BmobQuery<Tips> sql = new BmobQuery<>();
		sql.or(sqls);
		sql.addWhereEqualTo("enable", true);
		if (!TIPS)
			sql.addWhereEqualTo("openSMS", true);
		sql.findObjects(new FindListener<Tips>() {
			@Override
			public void done(List<Tips> list, BmobException e) {
				if (e == null) {
					if (list.size() > 0)
						mTips = list;
					else
						mTips.clear();
				}
			}
		});
	}
	
	public static Tips getTip() {
		try {
			if (mTips != null) {
				if (mTips.size() == 0) {
					getTips();
					return null;
				}
				else if (System.currentTimeMillis() - lastTipsTime > 60000)
					getTips();
				int ma = (int)(Math.random() * (double)(mTips.size()));
				if (ma >= mTips.size())
					ma = mTips.size() - 1;
				return mTips.get(ma);
			} else
				getTips();
		} catch (Exception e) {
			ExceptionHandler.log("getTip_Random", e.toString());
		}
		return null;
	}
	
	public static boolean isSupportMD() {
		return Build.VERSION.SDK_INT > 20;
	}
	
	public static void startActivityOption(Activity context, Intent intent, View view, String shareName) {
		startActivityOptions(context, intent, view, shareName, false);
	}
	
	public static void startActivityOptions(Activity context, Intent intent, View view, String shareName) {
		startActivityOptions(context, intent, view, shareName, true);
	}
	
	public static void startActivityOptions(Activity context, Class<?> cls) {
		startActivityOptions(context, new Intent(context, cls));
	}
	
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
		startActivityForResult(requestCode, context, new Intent(context, cls));
	}
	
	public static void startActivityOptions(Activity context, Intent intent, Pair...pairs) {
		try {
			if (isSupportMD()) {
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
				context.startActivity(intent, options.toBundle());
			} else {
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			ActivityManager.getInstance().startActivity(context);
		} catch (Exception e) {
			context.startActivity(intent);
			ExceptionHandler.log("StartActivityOptionsPair", e.toString());
		}
	}
	
	public static void startActivityForResult(int requestCode, Activity context, Intent intent, Pair<View,String>...pairs) {
		try {
			if (isSupportMD()) {
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
				context.startActivityForResult(intent, requestCode, options.toBundle());
			} else {
				context.startActivityForResult(intent, requestCode);
				context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			ActivityManager.getInstance().startActivity(context);
		} catch (Exception e) {
			context.startActivityForResult(intent, requestCode);
			ExceptionHandler.log("StartActivityForResultPair", e.toString());
		}
	}
	
	private static void startActivityOptions(Activity context, Intent intent, View view, String shareName, boolean setName) {
		try {
			if (isSupportMD()) {
				if (setName)
					view.setTransitionName(shareName);
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, shareName);
				context.startActivity(intent, options.toBundle());
				ActivityManager.getInstance().startActivity(context);
			} else
				startActivityOptions(context, intent);
		} catch (Exception e) {
			context.startActivity(intent);
			ExceptionHandler.log("StartActivityOptionsShare", e.toString());
		}
	}
	
	public interface LoginListener {
		void onLogin(int code);
		void onError(String error);
	}
	
	public static void gc(Context c) {
		//gc垃圾回收: 恢复到初始化状态
		IsRun = false;//运行状态
		AUTO_LOGIN_MODE = 0;//自动登录类型，1为普通，2为QQ
		SDATE = null;//激活凭证
		KEY = null;//密钥
		SID = null;//激活时间
		USERID = null;//用户名
		USERPASS = null;//加密密码
		IS_ACTIVE = false;//是否注册
		NO_ADV = false;//去广告
		WEL_ADV = true;//欢迎页广告
		SOUND = null;//音乐池
		mUser = null;//登录用户
		mTips = null;//通知中心
		myInbox = null;//消息中心
		if (copyMsgs != null) {
			copyMsgs.clear();
			copyMsgs = null;
		}
		//停止正在运行的音乐服务
		if (audioService != null) {
			if (audioService.isPlay())
				audioService.Stop();
			c.stopService(new Intent(c, AudioService.class));
			c.stopService(new Intent(c, DownloadService.class));
		}
		//清空音节码加载池
		if (SOUND != null) {
			SOUND.clear();
			SOUND = null;
		}
		System.gc();
	}
}
