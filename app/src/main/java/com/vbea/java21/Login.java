package com.vbea.java21;

import android.app.ProgressDialog;
import android.animation.Animator;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Button; 
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Point;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.SettingUtil;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.widget.RippleColorView;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.connect.common.Constants;
import org.json.JSONObject;

public class Login extends BaseActivity
{
	private EditText edtUid, edtPass;
	private TextView forget, btnSign;
	private Button btnLogin;
	private ProgressDialog mProgressDialog;
	private boolean canGoback = true;
	private RippleColorView rippleColorView;

	@Override
	protected void before()
	{
		setContentView(R.layout.login);
	}

	@Override
	public void after()
	{
		forget = bind(R.id.btn_forgetpsd);
		btnSign = bind(R.id.btnSign);
		ImageView qqLogin = bind(R.id.btn_qqLogin);
		btnLogin = bind(R.id.btnLogin);
		edtUid = bind(R.id.loginUid);
		edtPass = bind(R.id.loginPassword);
		rippleColorView = bind(R.id.rippleColorView);
		if (Common.HULUXIA)// && !Common.IS_ACTIVE)
		{
			//btnSign.setVisibility(View.GONE);
			qqLogin.setVisibility(View.GONE);
		}
		edtUid.setText(Common.USERID);
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
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
				forget.setEnabled(false);
				btnLogin.setText("登录中...");
				loginUser();
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
		//Common.MUSIC = Common.mUser.Set_Music;
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		SharedPreferences.Editor edt = spf.edit();
		edt.putInt("theme", Common.APP_THEME_ID);
		edt.putInt("back", Common.APP_BACK_ID);
		edt.putInt("java_size", Common.JAVA_TEXT_SIZE);
		edt.commit();
		//Common.reStart(Login.this);
	}
	
	private Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					try
					{
						if (Common.checkUpdateSetting(Login.this))
						{
							reloadLogin();
							ActivityManager.getInstance().FinishAllActivities();
							Common.startActivityOptions(Login.this, Main.class);
						}
						else
						{
							if (Common.isLogin())
							{
								Common.mUser.settings = Common.getSettingJson(new SettingUtil());
								Common.updateUser();
							}
							//reloadLogin();
							supportFinishAfterTransition();
						}
					}
					catch (Exception e)
					{
						ExceptionHandler.log("login.handle1", e);
					}
					break;
				case 1:
					Util.toastShortMessage(getApplicationContext(), "登录失败，请重试");
					resetButton();
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "用户名或密码错误，请重试");
					resetButton();
					break;
				case 3:
					if (Common.isNet(Login.this))
					{
						Common.startActivityOptions(Login.this, QQLoginReg.class);
						mHandler.sendEmptyMessageDelayed(5, 300);
					}
					break;
				case 4:
					Util.toastShortMessage(getApplicationContext(), "该帐号已被封禁，无法登录！");
					Common.Logout();
					resetButton();
					break;
				case 5:
					finish();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void resetButton()
	{
		btnLogin.setEnabled(true);
		btnSign.setEnabled(true);
		forget.setEnabled(true);
		canGoback = true;
		btnLogin.setText(R.string.java_login);
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}
	
	private void loginUser()
	{
		try
		{
			Common.Login(Login.this, edtUid.getText().toString(), MD5Util.getMD5(edtPass.getText().toString()), false, new Common.LoginListener()
			{
				@Override
				public void onLogin(int code)
				{
					if (Common.mUser != null && code == 1) {
						((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edtUid.getWindowToken(), 0);
						Point p = getLocationInView(rippleColorView, btnLogin);
						rippleColorView.reveal(p.x, p.y, MyThemes.getColorAccent(Login.this), new Animator.AnimatorListener()
						{
							@Override
							public void onAnimationStart(Animator p1)
							{
								
							}

							@Override
							public void onAnimationEnd(Animator p1)
							{
								mHandler.sendEmptyMessage(0);
							}

							@Override
							public void onAnimationCancel(Animator p1)
							{
								
							}

							@Override
							public void onAnimationRepeat(Animator p1)
							{
								
							}
						});
					} else if (code == 0)
						mHandler.sendEmptyMessage(2);
					else if (code == 2)
						mHandler.sendEmptyMessage(4);
				}

				@Override
				public void onError(String error)
				{
					ExceptionHandler.log("Login.LoginUser", error);
					toastLoginError("0x0001");
					//mHandler.sendEmptyMessage(1);
				}
			});
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Login.LoginThread", e.toString());
			toastLoginError("0x0003");
			//mHandler.sendEmptyMessage(1);
		}
	}
	
	public void loginQQUser()
	{
		try
		{
			Common.qqLogin(Login.this, SocialShare.mTencent.getOpenId(), new Common.LoginListener()
			{
				@Override
				public void onLogin(int code)
				{
					if (Common.isLogin() && code == 1) {
						Util.toastShortMessage(getApplicationContext(), "登录成功");
						mHandler.sendEmptyMessage(0);
					} else if (code == 0)
						mHandler.sendEmptyMessage(3);
					else if (code == 2)
						mHandler.sendEmptyMessage(4);
				}

				@Override
				public void onError(String error)
				{
					ExceptionHandler.log("Login.LoginQQUser", error);
					toastLoginError("0x0001");
					//mHandler.sendEmptyMessage(1);
				}
			});
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Login.qqLoginThread", e.toString());
			toastLoginError("0x0003");
			//mHandler.sendEmptyMessage(1);
		}
	}
	
	private void toastLoginError(String code)
	{
		Util.toastShortMessage(getApplicationContext(), "登录失败，请重试(" + code + ")");
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
					loginQQUser();
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
