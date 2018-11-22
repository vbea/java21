package com.vbea.java21;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.exception.BmobException;

public class ForgetPassword extends BaseActivity
{
	private Button btnForget;
	private EditText edtUsername, edtEmail, edtPhone;
	private String objId="", userName="";

	@Override
	protected void before()
	{
		setContentView(R.layout.forget);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		btnForget = (Button) findViewById(R.id.btnForget);
		edtUsername = (EditText) findViewById(R.id.forUsername);
		edtEmail = (EditText) findViewById(R.id.forEmail);
		edtPhone = (EditText) findViewById(R.id.forPhone);
		
		btnForget.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isEmpty(edtUsername, "请输入用户名"))
					return;
				if (!isUsername(edtUsername.getText().toString()))
				{
					edtUsername.requestFocus();
					edtUsername.setError("用户名填写不规范");
					return;
				}
				if (!isEmail(edtEmail.getText().toString()))
				{
					edtEmail.requestFocus();
					edtEmail.setError("邮箱格式不正确");
					return;
				}
				if (!Common.isNet(ForgetPassword.this))
				{
					Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
					return;
				}
				btnForget.setText("请稍候...");
				btnForget.setEnabled(false);
				new ForThread().start();
				//Common.startActivityOptions(ForgetPassword.this, ResetPassword.class);
			}
		});
	}
	
	public void bandQuery()
	{
		Util.toastShortMessage(getApplicationContext(), "输入信息有错，请检查后重新输入");
		btnForget.setText(R.string.forgetpsd);
		btnForget.setEnabled(true);
	}
	
	private boolean isUsername(String user)
	{
		String check = "[\\da-zA-Z]{3,11}";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(user);
		return matcher.matches();
	}

	private boolean isEmail(String email)
	{
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	private boolean isEmpty(EditText view, String tip)
	{
		if (view.getText().toString().trim().length() == 0)
		{
			view.requestFocus();
			view.setError(tip);
			return true;
		}
		return false;
	}
	
	class ForThread extends Thread implements Runnable
	{
		boolean result;
		public void run()
		{
			result = false;
			try
			{
				BmobQuery<Users> sql = new BmobQuery<Users>();
				sql.addWhereEqualTo("name", edtUsername.getText().toString());
				sql.addWhereEqualTo("email", edtEmail.getText().toString());
				sql.addWhereEqualTo("valid", true);
				sql.findObjects(new FindListener<Users>()
				{
					@Override
					public void done(List<Users> list, BmobException e)
					{
						if (e == null)
						{
							if (list.size() == 1)
							{
								Users _user = list.get(0);
								if (_user.mobile == null || edtPhone.getText().toString().equals(_user.mobile))
								{
									objId = _user.getObjectId();
									userName = _user.name;
									result = true;
									//mHandler.sendEmptyMessage(3);
								}
							}
						}
						if (result)//成功
						{
							mHandler.sendEmptyMessage(3);
							mHandler.sendEmptyMessageDelayed(2, 500);
						}
						else
							mHandler.sendEmptyMessage(1);
					}
				});
			}
			catch (Exception e)
			{
				mHandler.sendEmptyMessage(1);
				ExceptionHandler.log("ForgetPsd", e.toString());
			}
		}
	};
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					bandQuery();
					break;
				case 2:
					finish();
					break;
				case 3:
					Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
					intent.putExtra("oid", objId);
					intent.putExtra("user", userName);
					Common.startActivityOptions(ForgetPassword.this, intent);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
