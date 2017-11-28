package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Button; 
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.exception.BmobException;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.connect.common.Constants;
import org.json.JSONObject;

public class Login extends AppCompatActivity
{
	private EditText edtUid, edtPass;
	private Button btnLogin, btnSign;
	private CheckBox chkLogin;
	private ProgressDialog mProgressDialog;
	private boolean canGoback = true;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		TextView forget = (TextView) findViewById(R.id.btn_forgetpsd);
		TextView qqLogin = (TextView) findViewById(R.id.btn_qqLogin);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSign = (Button) findViewById(R.id.btnSign);
		edtUid = (EditText) findViewById(R.id.loginUid);
		edtPass = (EditText) findViewById(R.id.loginPassword);
		chkLogin = (CheckBox) findViewById(R.id.chkLogin);
		setSupportActionBar(tool);
		if (Common.HULUXIA && !Common.IS_ACTIVE)
		{
			//btnSign.setVisibility(View.GONE);
			qqLogin.setVisibility(View.GONE);
		}
		edtUid.setText(Common.USERID);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (canGoback)
					supportFinishAfterTransition();
			}
		});
		
		btnLogin.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isEmpty(edtUid, true, "请输入用户名"))
					return;
				if (isEmpty(edtPass, false, "请输入密码"))
					return;
				if (!Common.isNet(Login.this))
				{
					Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
					return;
				}
				canGoback = false;
				btnLogin.setEnabled(false);
				btnSign.setEnabled(false);
				btnLogin.setText("登录中...");
				new LoginThread().start();
			}
		});
		
		btnSign.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Login.this, Sign.class);
			}
		});
		
		forget.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOptions(Login.this, ForgetPassword.class);
			}
		});
		
		qqLogin.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (SocialShare.mTencent != null)
				{
					SocialShare.mTencent.login(Login.this, "all", new MyIListener());
					mProgressDialog = ProgressDialog.show(Login.this, "", "请稍候……", true, false);
				}
			}
		});
	}
	
	private boolean isEmpty(EditText view, boolean trim, String tip)
	{
		if (trim)
		{
			if (view.getText().toString().trim().length() == 0)
			{
				view.requestFocus();
				view.setError(tip);
				return true;
			}
		}
		else
		{
			if (view.getText().toString().length() == 0)
			{
				view.requestFocus();
				view.setError(tip);
				return true;
			}
		}
		return false;
	}
	
	public void reloadLogin()
	{
		if (Common.mUser.Set_Theme != null)
			Common.APP_THEME_ID = Common.mUser.Set_Theme;
		if (Common.mUser.Set_Backimg != null)
			Common.APP_BACK_ID = Common.mUser.Set_Backimg;
		//Common.MUSIC = Common.mUser.Set_Music;
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		SharedPreferences.Editor edt = spf.edit();
		edt.putInt("theme", Common.APP_THEME_ID);
		edt.putInt("back", Common.APP_BACK_ID);
		edt.putBoolean("music", Common.MUSIC);
		edt.commit();
		Common.reStart(Login.this);
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					if (Common.mUser.valid)
					{
						Util.toastShortMessage(getApplicationContext(), "登录成功");
						if (Common.isCanUploadUserSetting())
						{
							if (Common.mUser.Set_Theme == null || Common.mUser.Set_Backimg == null)
							{
								reloadLogin();
								supportFinishAfterTransition();
							}
							else
							{
								reloadLogin();
								ActivityManager.getInstance().FinishAllActivities();
								Common.startActivityOptions(Login.this, Main.class);
							}
						}
						else
						{
							Common.reStart(Login.this);
							supportFinishAfterTransition();
						}
					}
					else
					{
						Util.toastShortMessage(getApplicationContext(), "该帐号已被封禁，无法登录！");
						Common.Logout();
						if (mProgressDialog != null)
							mProgressDialog.dismiss();
					}
					break;
				case 1:
					Util.toastShortMessage(getApplicationContext(), "登录失败，请重试");
					break;
				case 2:
					btnLogin.setEnabled(true);
					btnSign.setEnabled(true);
					canGoback = true;
					btnLogin.setText(R.string.java_login);
					break;
				case 3:
					Util.toastShortMessage(getApplicationContext(), "用户名或密码错误，请重试");
					break;
				case 4:
					if (Common.isNet(Login.this))
					{
						Common.startActivityOptions(Login.this, QQLoginReg.class);
						finish();
					}
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	class LoginThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				Common.onLogin = 0;
				Common.Login(Login.this, edtUid.getText().toString(), MD5Util.getMD5(edtPass.getText().toString()), chkLogin.isChecked() ? 1 : 0);
				while (Common.onLogin == 0) { Thread.sleep(500); }
				if (Common.mUser != null && Common.onLogin == 4)
				{
					mHandler.sendEmptyMessage(0);
				}
				else if (Common.onLogin == 1)
					mHandler.sendEmptyMessage(3);
				else
					mHandler.sendEmptyMessage(1);
			}
			catch (Exception e)
			{
				ExceptionHandler.log("Login.LoginThread", e.toString());
				mHandler.sendEmptyMessage(1);
			}
			finally
			{
				mHandler.sendEmptyMessage(2);
			}
		}
	}
	
	class QQLoginThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				Common.onLogin = 0;
				Common.qqLogin(Login.this, SocialShare.mTencent.getOpenId());
				while (Common.onLogin == 0) { Thread.sleep(500); }
				if (Common.mUser != null && Common.onLogin == 4)
					mHandler.sendEmptyMessage(0);
				else
					mHandler.sendEmptyMessage(4);
			}
			catch (Exception e)
			{
				ExceptionHandler.log("Login.qqLoginThread", e.toString());
				mHandler.sendEmptyMessage(1);
			}
			finally
			{
				mHandler.sendEmptyMessage(2);
			}
		}
	}
	
	class MyIListener implements IUiListener
	{
		@Override
		public void onComplete(Object p)
		{
			JSONObject json = (JSONObject)p;
			try
			{
				//ExceptionHandler.log("json", p.toString());
				if (json.has(Constants.PARAM_OPEN_ID))
				{
					String token = json.getString(Constants.PARAM_ACCESS_TOKEN);
					String expires = json.getString(Constants.PARAM_EXPIRES_IN);
					String openId = json.getString(Constants.PARAM_OPEN_ID);
					SocialShare.mTencent.setAccessToken(token, expires);
					SocialShare.mTencent.setOpenId(openId);
					new QQLoginThread().start();
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("qqLoginIUiListener", e.toString());
			}
			/*
			JSONObject p1 = (JSONObject)p;
			try
			{
				ExceptionHandler.log("json", p.toString());
				Util.toastShortMessage(getApplicationContext(), "登录成功");
				Util.toastShortMessage(getApplicationContext(), ""+Common.mTencent.getOpenId());
				if (mInfo == null)
				{
					mInfo = new UserInfo(Login.this, Common.mTencent.getQQToken());
					mInfo.getUserInfo(new MyIListener());
				}
				else
				{
					Toast.makeText(getApplicationContext(), p1.getString("figureurl_qq_2")+"|"+p1.getString("nickname"), Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("uiListener", e.toString());
			}*/
		}
		@Override
		public void onError(UiError p1)
		{
			Util.toastShortMessage(getApplicationContext(), "登录失败");
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
		}
		@Override
		public void onCancel()
		{
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.REQUEST_LOGIN)
		{
			Tencent.onActivityResultData(requestCode, resultCode, data, new MyIListener());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed()
	{
		if (canGoback)
			supportFinishAfterTransition();
	}
	
}
