package com.vbea.java21.classes;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.Pair;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import com.vbea.java21.R;
import com.vbea.java21.ActivityManager;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.audio.AudioService;
import com.vbea.java21.data.Users;
import com.vbea.java21.data.Tips;
import com.vbea.java21.MyThemes;
import com.vbea.secret.*;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.exception.BmobException;
import com.tencent.tauth.Tencent;

public class Common
{
	private static boolean IsRun = false;//是否运行
	public static int APP_THEME_ID = -1;//主题
	public static int APP_BACK_ID = 0;//背景图片
	public static int VERSION_CODE = 0;//版本号
	public static int AUTO_LOGIN_MODE = 0;//自动登录类型，1为普通，2为QQ
	public static String SDATE = "";//激活凭证
	public static String KEY = "";//密钥
	public static String SID = "";//激活时间
	public static String USERID = "";//用户名
	public static String USERPASS = "";//加密密码
	public static boolean IS_ACTIVE = false;//是否注册
	public static boolean NO_ADV = false;//去广告
	public static boolean WEL_ADV = true;//欢迎页广告
	//public static boolean EYESHIELD = false;//护眼模式
	//public static boolean PRO = false;//专业版
	public static SoundLoad SOUND = null;//音乐池
	public static boolean AUDIO = false;//是否显示音乐
	public static boolean MUSIC = true;//是否开启音乐
	public static boolean TIPS = true;//是否开启消息通知
	public final static boolean HULUXIA = false;//是否葫芦侠特别版
	public static Users mUser;
	public static boolean IsChangeICON = false;
	public static int onLogin = 0;
	public static int AUDIO_STUDY_STATE = 0;
	public static int JAVA_TEXT_SIZE = 2;
	public static AudioService audioService;
	private static final String defaultKey = "JAVA8-APP-KEY21-APK-VBEST";
	public static String LocalPath = Environment.getExternalStorageDirectory()+"/ZDApp/";
	public static String IconPath = LocalPath + "Cache/";
	public static String DrawImagePath = IconPath + "back.jpg";
	public static List<Tips> mTips = null;
	public static List<String> READ_Android, READ_J2EE, READ_AndroidAdvance;
	public static void start(Context context)
	{
		startBmob(context);
		if (IsRun)
			return;
		IsRun = true;
		SharedPreferences spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		init(spf);
		if (spf.getString("key", "").trim().equals("") || spf.getString("date","").trim().equals(""))
		{
			IS_ACTIVE = false;
			editor.putBoolean("app", false);
		}
		if (IS_ACTIVE && regist(KEY))
		{
			editor.putBoolean("app", true);
			editor.putBoolean("active", true);
		}
		else
		{
			IS_ACTIVE = false;
			editor.putBoolean("app", false);
			editor.putBoolean("active", false);
			editor.putString("key", defaultKey);
		}
		if (getDrawerBack() == null)
			editor.putInt("back", 0);
		editor.commit();
		init(spf);
		SocialShare.onStart(context);
		if (mTips == null && isNet(context))
			getTips();
	}
	
	public static void reStart(Context context)
	{
		IsRun = false;
		start(context);
	}
	
	private static void startBmob(Context context)
	{
		if (BmobWrapper.getInstance() == null)
			Bmob.initialize(context, "1aa46b02605279e1a84935073af9fc82");
	}
	
	public static void update(Context context, boolean check)
	{
		int code = 0;
		SharedPreferences spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		code = context.getResources().getInteger(R.integer.versionCode);
		if (check)
			editor.putInt("check", code);
		else
		{
			String codes = "";
			try
			{
				k dec = new k();
				codes = dec.encrypt(format.format(new Date()));
				File cathe = new File(LocalPath + File.separator + ".nomedia");
				if (!cathe.exists())
					cathe.createNewFile();
			}
			catch (Exception e)
			{
			}
			editor.putInt("check", code);
			editor.putString("checkCode", codes);
		}
		editor.putBoolean("chartip", false);
		editor.putBoolean("androidtip", false);
		editor.commit();
		init(spf);
	}
	
	public static void updateUser()
	{
		try
		{
			if (mUser == null)
				return;
			Users user = new Users();
			user.setObjectId(mUser.getObjectId());
			user.psd = mUser.psd;
			user.nickname = mUser.nickname;
			user.birthday = mUser.birthday;
			user.address = mUser.address;
			user.key = mUser.key;
			user.mark = mUser.mark;
			user.gender = mUser.gender;
			user.icon = mUser.icon;
			user.qq = mUser.qq;
			user.qqId = mUser.qqId;
			user.mobile = mUser.mobile;
			user.Set_Backimg = APP_BACK_ID;
			user.Set_Theme = APP_THEME_ID;
			//user.Set_Music = MUSIC;
			user.update(new UpdateListener()
			{
				public void done(BmobException e)
				{
					if (e != null)
						ExceptionHandler.log("Bmob_updateUser", e.toString());
				}
			});
		}
		catch (Exception e)
		{
			ExceptionHandler.log("updateUser", e.toString());
		}
	}
	
	private static void init(SharedPreferences spf)
	{
		VERSION_CODE = spf.getInt("check", 0);
		APP_THEME_ID = spf.getInt("theme", 0);
		APP_BACK_ID = spf.getInt("back", 0);
		SDATE = spf.getString("checkCode", "");
		IS_ACTIVE = spf.getBoolean("app", false);
		MUSIC = spf.getBoolean("music", true);
		KEY = spf.getString("key", "");
		SID = spf.getString("date","");
		NO_ADV = spf.getBoolean("noadv", false);
		WEL_ADV = spf.getBoolean("weladv", true);
		USERID = spf.getString("uid", "");
		USERPASS = spf.getString("sid", "");
		AUTO_LOGIN_MODE = spf.getInt("loginmode", 0);
		TIPS = spf.getBoolean("tips", true);
		JAVA_TEXT_SIZE = spf.getInt("java_size", 2);
		READ_Android = new ArrayList<String>();
		READ_J2EE = new ArrayList<String>();
		READ_AndroidAdvance = new ArrayList<String>();
		String[] android = spf.getString("read_android", "").split(",");
		if (android != null && android.length > 0)
		{
			for (String s : android)
				READ_Android.add(s);
		}
		String[] android2 = spf.getString("read_android2", "").split(",");
		if (android2 != null && android2.length > 0)
		{
			for (String s : android2)
				READ_AndroidAdvance.add(s);
		}
		String[] javaee = spf.getString("read_javaee", "").split(",");
		if (javaee != null && javaee.length > 0)
		{
			for (String s : javaee)
				READ_J2EE.add(s);
		}
	}
	
	public static void addAndroidRead(String num)
	{
		AUDIO_STUDY_STATE+=1;
		if (READ_Android.contains(num))
			return;
		READ_Android.add(num);
	}
	
	public static void addAndroid2Read(String num)
	{
		AUDIO_STUDY_STATE+=1;
		if (READ_AndroidAdvance.contains(num))
			return;
		READ_AndroidAdvance.add(num);
	}
	
	public static void addJavaEeRead(String num)
	{
		AUDIO_STUDY_STATE+=1;
		if (READ_J2EE.contains(num))
			return;
		READ_J2EE.add(num);
	}
	
	public static void clearReadHistory()
	{
		READ_J2EE.clear();
		READ_Android.clear();
		READ_AndroidAdvance.clear();
	}
	
	public static boolean canLogin()
	{
		if (AUTO_LOGIN_MODE <= 0 || USERID.equals("") || USERPASS.equals("") || mUser != null)
			return false;
		return true;
	}
	
	public static void Login(Context context)
	{
		if (AUTO_LOGIN_MODE == 1)
			Login(context, USERID, USERPASS, 3);
		else if (AUTO_LOGIN_MODE == 2)
			qqLogin(context, USERPASS);
	}
	
	public static boolean isAudio()
	{
		if (AUDIO && audioService != null)
		{
			if (audioService.isPlay())
				return false;
		}
		return AUDIO;
	}
	
	public static boolean isNoadv()
	{
		return (Common.IS_ACTIVE && Common.NO_ADV);
	}
	
	public static boolean isWeladv()
	{
		return (!Common.IS_ACTIVE || Common.WEL_ADV);
	}
	
	public static boolean isVipUser()
	{
		if (mUser != null && IS_ACTIVE)
			return mUser.roles.equals("管理员") || Common.mUser.roles.equals("VIP会员");
		return false;
	}
	
	public static void qqLogin(final Context context, String openId)
	{
		BmobQuery<Users> sql = new BmobQuery<Users>();
		sql.addWhereEqualTo("qqId", openId);
		//sql.addWhereEqualTo("valid", true);
		sql.findObjects(new FindListener<Users>()
		{
			@Override
			public void done(List<Users> list, BmobException e)
			{
				if (e == null)
				{
					if (list.size() > 0)
					{
						mUser = list.get(0);
						SharedPreferences spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = spf.edit();
						editor.putString("uid", Common.mUser.name);
						editor.putString("sid", Common.mUser.qqId);
						editor.putInt("loginmode", 2);
						USERID = mUser.name;
						KEY = mUser.key;
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						editor.putString("key", KEY);
						if (!IS_ACTIVE)
						{
							if (regist(KEY))
							{
								IS_ACTIVE = true;
								editor.putBoolean("autok", true);
								editor.putString("date", format.format(new Date()));
								editor.putBoolean("app", true);
							}
						}
						editor.commit();
						onLogin = 4;
						IsChangeICON = true;
					}
					else
						onLogin = 1;
				}
				else
				{
					//ExceptionHandler.log("登录失败:"+e.toString());
					onLogin = 2;
				}
			}
		});
	}
	
	public static void Login(final Context context, final String username, final String pasdword, final int mode)
	{
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
		sql.findObjects(new FindListener<Users>()
		{
			@Override
			public void done(List<Users> list, BmobException e)
			{
				if (e == null)
				{
					if (list.size() > 0)
					{
						Common.mUser = list.get(0);
						SharedPreferences spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = spf.edit();
						KEY = mUser.key;
						if (mode < 3)
						{
							editor.putString("uid", mode == 1 ? Common.mUser.name : "");
							editor.putString("sid", mode == 1 ? Common.mUser.psd : "");
							editor.putInt("loginmode", mode);
							USERID = username;
							if (!IS_ACTIVE)//自动激活
							{
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								if (regist(KEY))
								{
									IS_ACTIVE = true;
									editor.putBoolean("autok", true);
									editor.putString("date", format.format(new Date()));
									editor.putBoolean("app", true);
								}
							}
						}
						else if (!mUser.valid)
						{
							onLogin = 2;
							mUser = null;
							return;
						}
						editor.putString("key", KEY);
						editor.commit();
						onLogin = 4;
						IsChangeICON = true;
					}
					else
						onLogin = 1;
				}
				else
				{
					//ExceptionHandler.log("登录失败:"+e.toString());
					onLogin = 2;
				}
			}
		});
	}
	
	public static void Logout()
	{
		mUser = null;
		onLogin = 0;
		IsChangeICON = true;
	}
	
	public static void Logout(Context context)
	{
		mUser = null;
		onLogin = 0;
		IsChangeICON = true;
		SharedPreferences spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = spf.edit();
		editor.putInt("loginmode", 0);
		editor.commit();
		AUTO_LOGIN_MODE = 0;
	}
	
	public static boolean isCanUploadUserSetting()
	{
		if (mUser != null)
		{
			if (mUser.Set_Backimg != null && mUser.Set_Theme != null)
				return mUser.Set_Backimg != APP_BACK_ID || mUser.Set_Theme != APP_THEME_ID;
			else
				return true;
		}
		return false;
	}
	
	public static boolean isLogin()
	{
		return (onLogin == 4 && mUser != null);
	}
	
	public static boolean isNotLogin()
	{
		if ((onLogin == 0 || onLogin == 2 || onLogin == 1) && mUser == null)
			return true;
		return false;
	}
	
	private static boolean regist(String key)
	{
		try
		{
			if (key.length() == 25 && !key.equals(defaultKey))
			{
				Keys plug = new SecretKey(key).getInstance();
				return plug.verify();
			}
			else
				return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean isNet(Context context)
	{
		ConnectivityManager zdapp = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (zdapp == null)
		{
			return false;
		}
		else
		{
			NetworkInfo[] info = zdapp.getAllNetworkInfo();
			if (info != null)
			{
				for (int i=0; i<info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
				//zdapp.getActiveNetworkInfo().isAvailable()
			}
		}
		return false;
	}
	
	public static Bitmap getIcon()
	{
		if (mUser.icon != null)
		{
			File path = new File(IconPath);
			if (!path.exists())
			{
				path.mkdirs();
			}
			File file = new File(IconPath + mUser.icon.getFilename());
			if (file.exists())
				return BitmapFactory.decodeFile(file.getAbsolutePath());
			else
			{
				try
				{
					mUser.icon.download(file, new DownloadFileListener()
					{
						@Override
						public void done(String p1, BmobException p2)
						{
							
						}

						@Override
						public void onProgress(Integer p1, long p2)
						{

						}
					});
				}
				catch (Exception e)
				{
					ExceptionHandler.log("getIcon", e.toString());
				}
			}
		}
		return null;
	}
	
	public static void setIcon(final ImageView v, final Context context, boolean downed)
	{
		if (!downed)
			v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.head)));
		if (mUser != null && mUser.icon != null)
		{
			File path = new File(IconPath);
			if (!path.exists())
				path.mkdirs();
			File file = new File(IconPath + mUser.icon.getFilename());
			if (file.exists())
				v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeFile(file.getAbsolutePath())));
			else
			{
				try
				{
					mUser.icon.download(file, new DownloadFileListener()
					{
						@Override
						public void done(String p1, BmobException p2)
						{
							setIcon(v, context, true);
						}

						@Override
						public void onProgress(Integer p1, long p2)
						{
						}
					});
				}
				catch (Exception e)
				{
					ExceptionHandler.log("getIcon", e.toString());
				}
			}
		}
	}
	
	public static void setMyIcon(ImageView v, Context context)
	{
		if (mUser != null && mUser.icon != null)
		{
			File file = new File(IconPath + mUser.icon.getFilename());
			if (file.exists())
				v.setImageDrawable(getRoundedIconDrawable(context, BitmapFactory.decodeFile(file.getAbsolutePath())));
		}
	}
	
	public static BitmapDrawable getHomeBack()
	{
		Bitmap bit = getDrawerBack();
		if (bit != null)
			return new BitmapDrawable(bit);
		return null;
	}
	
	public static Bitmap getDrawerBack()
	{
		File path = new File(IconPath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		File file = new File(DrawImagePath);
		if (file.exists())
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		return null;
	}
	
	public static RoundedBitmapDrawable getRoundedIconDrawable(Context context, Bitmap src)
	{
		if (src != null)
		{
			RoundedBitmapDrawable rbd = RoundedBitmapDrawableFactory.create(context.getResources(), src);
			rbd.setCornerRadius(src.getWidth()/2);
			rbd.setAntiAlias(true);//设置反走样
			return rbd;
		}
		return null;
	}
	
	public static boolean isMyUser(String name)
	{
		if (mUser != null)
		{
			return mUser.name.equals(name);
		}
		return false;
	}
	
	public static void getTips()
	{
		BmobQuery<Tips> sql1 = new BmobQuery<Tips>();
		sql1.addWhereGreaterThanOrEqualTo("dates", new BmobDate(new Date()));
		BmobQuery<Tips> sql2 = new BmobQuery<Tips>();
		sql2.addWhereEqualTo("islong", true);
		List<BmobQuery<Tips>> sqls = new ArrayList<BmobQuery<Tips>>();
		sqls.add(sql1);
		sqls.add(sql2);
		BmobQuery<Tips> sql = new BmobQuery<Tips>();
		sql.or(sqls);
		sql.addWhereEqualTo("enable", true);
		sql.findObjects(new FindListener<Tips>()
		{
			@Override
			public void done(List<Tips> list, BmobException e)
			{
				if (e == null)
				{
					if (list.size() > 0)
						mTips = list;
				}
			}
		});
	}
	
	public static Tips getTip()
	{
		if (!TIPS)
			return null;
		try
		{
			if (mTips != null)
			{
				if (mTips.size() == 1)
					getTips();
				int ma = (int)(Math.random() * (double)(mTips.size()));
				if (ma >= mTips.size())
					ma = mTips.size() - 1;
				return mTips.get(ma);
			}
			else
				getTips();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getTip_Random", e.toString());
		}
		return null;
	}
	
	public static boolean isSupportMD()
	{
		return Build.VERSION.SDK_INT > 20;
	}
	
	public static void startActivityOption(Activity context, Intent intent, View view, String shareName)
	{
		startActivityOptions(context, intent, view, shareName, false);
	}
	
	public static void startActivityOptions(Activity context, Intent intent, View view, String shareName)
	{
		startActivityOptions(context, intent, view, shareName, true);
	}
	
	public static void startActivityOptions(Activity context, Class<?> cls)
	{
		startActivityOptions(context, new Intent(context, cls));
	}
	
	public static void startActivityOptions(Activity context, Intent intent, Pair<View,String>...pairs)
	{
		try
		{
			if (isSupportMD())
			{
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
				context.startActivity(intent, options.toBundle());
			}
			else
			{
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			ActivityManager.getInstance().startActivity(context);
		}
		catch (Exception e)
		{
			context.startActivity(intent);
			ExceptionHandler.log("StartActivityOptionsPair", e.toString());
		}
	}
	
	private static void startActivityOptions(Activity context, Intent intent, View view, String shareName, boolean setName)
	{
		try
		{
			if (isSupportMD())
			{
				if (setName)
					view.setTransitionName(shareName);
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, shareName);
				context.startActivity(intent, options.toBundle());
				ActivityManager.getInstance().startActivity(context);
			}
			else
				startActivityOptions(context, intent);
		}
		catch (Exception e)
		{
			context.startActivity(intent);
			ExceptionHandler.log("StartActivityOptionsShare", e.toString());
		}
	}
}
