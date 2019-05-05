package com.vbea.java21.classes;

import java.util.List;
import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.Pair;
import android.support.v4.app.ActivityOptionsCompat;

import com.vbea.java21.R;
import com.vbea.java21.ActivityManager;
import com.vbea.java21.audio.SoundLoad;
import com.vbea.java21.audio.AudioService;

public class Common
{
	private static boolean IsRun = false;//是否运行
	public static int APP_THEME_ID = -1;//主题
	public static int APP_BACK_ID = 0;//背景图片
	public static int VERSION_CODE = 0;
	public static boolean isShowFile = true;//显示所有文件
	public static SoundLoad SOUND = null;//音乐池
	public static boolean AUDIO = false;//是否显示音乐
	public static boolean MUSIC = true;//是否开启音乐
	public static boolean TIPS = true;//是否开启消息通知
	public static int PIANO_SIZE = 0;
	public final static boolean HULUXIA = true;
	public static boolean IsChangeICON = false;
	public static int AUDIO_STUDY_STATE = 0;
	public static int JAVA_TEXT_SIZE = 2;
	public static AudioService audioService;
	public static final String FileProvider = "com.vbea.java21.fileprovider";
	public static final String ExterPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String LocalPath = ExterPath + "/ZDApp/";
	public static void start(Context context)
	{
		if (IsRun)
			return;
		IsRun = true;
		ReadUtil.init(context);
		EasyPreferences spf = new EasyPreferences(context);
		init(spf);
        spf.apply();
		init(spf);
	}

	public static boolean isRun() {
		return IsRun;
	}
	
	public static void reStart(Context context)
	{
		IsRun = false;
		start(context);
	}

	private static void init(EasyPreferences spf)
	{
		VERSION_CODE = spf.getInt("check", 0);
		APP_THEME_ID = spf.getInt("theme", 0);
		APP_BACK_ID = spf.getInt("back", 0);
		//SDATE = spf.getString("checkCode", "");
		//IS_ACTIVE = spf.getBoolean("app", false);
		MUSIC = spf.getBoolean("music", true);
		//KEY = spf.getString("key", "");
		//SID = spf.getString("date","");
		//NO_ADV = spf.getBoolean("noadv", false);
		//WEL_ADV = spf.getBoolean("weladv", true);
		//USERID = spf.getString("uid", "");
		//USERPASS = spf.getString("sid", "");
		//AUTO_LOGIN_MODE = spf.getInt("loginmode", 0);
		TIPS = spf.getBoolean("tips", true);
		JAVA_TEXT_SIZE = spf.getInt("java_size", 2);
		PIANO_SIZE = spf.getInt("piano_size", 0);
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

	public static void showUserRole(TextView txtVip, int role)
 	{
		switch (role)
		{
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
	
	public static boolean isVipUser()
	{
		return false;
	}
	
	public static boolean isAdminUser()
	{
		return false;
	}
	
	public static boolean isSVipUser()
	{
		return false;
	}
	
	public static boolean isHuluxiaAd()
	{
		return true;
	}
	
	public static boolean isHuluxiaUser()
	{
		return HULUXIA;
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

	public static String getUpdatePath() {
		return LocalPath + "apk/";
	}
	
	public static String getCachePath()
	{
		return LocalPath + "Cache/";
	}

	public static String getDownloadPath()
	{
		return LocalPath + "Download/";
	}
	
	public static String getIconPath()
	{
		return LocalPath + "Portrait/";
	}
	
	public static String getDrawImagePath()
	{
		return getIconPath() + "back.jpg";
	}
	
	public static String getTempImagePath()
	{
		return getCachePath() + "temp.jpg";
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
	
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode)
	{
		startActivityForResult(requestCode, context, new Intent(context, cls));
	}
	
	public static void startActivityOptions(Activity context, Intent intent, Pair...pairs)
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
	
	public static void startActivityForResult(int requestCode, Activity context, Intent intent, Pair<View,String>...pairs)
	{
		try
		{
			if (isSupportMD())
			{
				ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, pairs);
				context.startActivityForResult(intent, requestCode, options.toBundle());
			}
			else
			{
				context.startActivityForResult(intent, requestCode);
				context.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			ActivityManager.getInstance().startActivity(context);
		}
		catch (Exception e)
		{
			context.startActivityForResult(intent, requestCode);
			ExceptionHandler.log("StartActivityForResultPair", e.toString());
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
	
	public interface LoginListener
	{
		void onLogin(int code);
		void onError(String error);
	}
	
	public static void gc(Context c)
	{
		//gc垃圾回收: 恢复到初始化状态
		IsRun = false;//运行状态
		SOUND = null;//音乐池
		//停止正在运行的音乐服务
		if (audioService != null)
		{
			if (audioService.isPlay())
				audioService.Stop();
			c.stopService(new Intent(c, AudioService.class));
		}
		//清空音节码加载池
		if (SOUND != null)
		{
			SOUND.clear();
			SOUND = null;
		}
		System.gc();
	}
}
