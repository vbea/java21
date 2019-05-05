package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.tencent.stat.StatService;

public class MainActivity extends AppCompatActivity
{
	private View based;
	private ImageView imgIcon;
	private TextView txtTitle, txtTip, txtDic, txtSkip;
	private LinearLayout layoutMain;
	private AlphaAnimation disappears;
	private String[] homeTips, homeDics;
	private ViewGroup container;
	private final String SKIP_TEXT = "跳过 %d";
	private boolean canJump = false;
	private int requestOnce = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		Common.start(getApplicationContext());
		setTheme(MyThemes.getTheme());
		getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
		based = findViewById(R.id.welLayout);
		container = (ViewGroup) findViewById(R.id.splashContainer);
		layoutMain = (LinearLayout) findViewById(R.id.welcome_layout);
		imgIcon = (ImageView) findViewById(R.id.img_welicon);
		txtTitle = (TextView) findViewById(R.id.text_retry);
		txtTip = (TextView) findViewById(R.id.welcome_tips);
		txtDic = (TextView) findViewById(R.id.welcome_dics);
		txtSkip = (TextView) findViewById(R.id.welcome_skip);
		StatService.trackCustomEvent(this, "onCreate", "");
		homeTips = getResources().getStringArray(R.array.array_homeTips);
		homeDics = getResources().getStringArray(R.array.array_homeDics);
		disappears = new AlphaAnimation(1, 0);
		disappears.setDuration(1000);
		disappears.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation p1)
			{
			}

			@Override
			public void onAnimationEnd(Animation p1)
			{
				imgIcon.setVisibility(View.INVISIBLE);
				txtTip.setVisibility(View.INVISIBLE);
				txtDic.setVisibility(View.INVISIBLE);
			}
				
			@Override
			public void onAnimationRepeat(Animation p1)
			{
			}
		});
		if (Util.isAndroidM())
			checkAndRequestPermission();
		/*try
		{
			//StatService.startStatService(this, "Aqc1150078134", com.tencent.stat.common.StatConstants.VERSION);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("mta", e.toString());
		}*/
    }
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					getRandomTip();
					break;
				case 1:
					if (Common.isSupportMD())
					{
						getRandomDic();
						imgIcon.startAnimation(disappears);
						txtTip.startAnimation(disappears);
						txtDic.startAnimation(disappears);
					}
					break;
				case 2:
					Intent intent = new Intent();
					intent.putExtra("start", true);
					intent.setClass(MainActivity.this, Main.class);
					Common.startActivityOption(MainActivity.this, intent, based, getString(R.string.shared));
					break;
				case 3:
					if (canJump)
					{
						Intent intent1 = new Intent(MainActivity.this, Main.class);
						intent1.putExtra("start", true);
						Common.startActivityOptions(MainActivity.this, intent1);
						mHandler.sendEmptyMessageDelayed(4, 1000);
					}
					else
						canJump = true;
					break;
				case 4:
					finish();
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onPause()
	{
		super.onPause();
		canJump = false;
	}

	@Override
	protected void onResume()
	{
		StatService.onResume(this);
		imgIcon.setVisibility(View.VISIBLE);
		getRandomTips();
		mHandler.sendEmptyMessage(3);
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		
	}
	
	private void getRandomTips()
	{
		if (layoutMain.getVisibility() == View.VISIBLE)
		{
			getRandomTip();
			getRandomDic();
		}
	}
	
	private void getRandomTip()
	{
		txtTip.setText(homeTips[(int)(Math.random() * (double)homeTips.length)]);
	}
	private void getRandomDic()
	{
		txtDic.setText(homeDics[(int)(Math.random() * (double)homeDics.length)]);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (Util.hasAllPermissionsGranted(grantResults))
			new MainThread().start();
		else
			checkAndRequestPermission();
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private void checkAndRequestPermission()
	{
		requestOnce+=1;
		if (requestOnce > 2) {
			new MainThread().start();
			return;
		}
		List<String> lackedPermission = new ArrayList<String>();
		if (!Util.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
			lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (!Util.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (lackedPermission.size() > 0) {
			String[] requestPermission = new String[lackedPermission.size()];
			requestPermission = lackedPermission.toArray(requestPermission);
			Util.requestPermission(this, 100, requestPermission);
		} else {
			new MainThread().start();
		}
	}
	
	class MainThread extends Thread implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(1000);
				mHandler.sendEmptyMessage(0);
				Thread.sleep(500);
				mHandler.sendEmptyMessage(1);
				Thread.sleep(1000);
				mHandler.sendEmptyMessage(2);
				Thread.sleep(1000);
				mHandler.sendEmptyMessage(4);
			}
			catch (Exception e)
			{
				ExceptionHandler.log("MainActivity", e.toString());
			}
		}
	}
}
