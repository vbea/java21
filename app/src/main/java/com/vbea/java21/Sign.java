package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Button; 
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Switch;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.exception.BmobException;

public class Sign extends AppCompatActivity
{
	private RadioButton rdbMale;
	private EditText edtUsername, edtPass1, edtPass2, edtEmail, edtNick;
	private boolean isNameExist, isEmailExist, isRecheck, td1, td2;
	private Button btnSign;
	private boolean canGoback = true;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		btnSign = (Button) findViewById(R.id.btnSign);
		edtUsername = (EditText) findViewById(R.id.signUsername);
		edtPass1 = (EditText) findViewById(R.id.signPassword);
		edtPass2 = (EditText) findViewById(R.id.signQuerPass);
		edtEmail = (EditText) findViewById(R.id.signEmail);
		edtNick = (EditText) findViewById(R.id.signNick);
		rdbMale = (RadioButton) findViewById(R.id.rdbMale);
		setSupportActionBar(tool);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (canGoback)
					supportFinishAfterTransition();
			}
		});
		edtUsername.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				
			}

			@Override
			public void afterTextChanged(Editable p1)
			{
				isRecheck = true;
			}
		});
		
		edtEmail.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{

			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{

			}

			@Override
			public void afterTextChanged(Editable p1)
			{
				isRecheck = true;
			}
		});
		
		btnSign.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (edtUsername.getError() != null)
					return;
				if (edtEmail.getError() != null)
					return;
				if (isEmpty(edtUsername, true, "请输入用户名"))
					return;
				if (!isUsername(edtUsername.getText().toString()))
				{
					edtUsername.requestFocus();
					edtUsername.setError("用户名填写不规范");
					return;
				}
				if (isEmpty(edtPass1, false, "请输入密码"))
					return;
				if (edtPass1.getText().toString().trim().length() < 4)
				{
					edtPass1.requestFocus();
					edtPass1.setError("密码长度过短");
					return;
				}
				if (!edtPass1.getText().toString().equals(edtPass2.getText().toString()))
				{
					edtPass2.requestFocus();
					edtPass2.setError("两次输入密码不一致");
					return;
				}
				if (!isEmail(edtEmail.getText().toString()))
				{
					edtEmail.requestFocus();
					edtEmail.setError("邮箱格式不正确");
					return;
				}
				if (isEmpty(edtNick, true, "请输入昵称"))
					return;
				if (!Common.isNet(Sign.this))
				{
					Toast.makeText(getApplicationContext(), "请检查你的网络连接", Toast.LENGTH_SHORT).show();
					return;
				}
				canGoback = false;
				btnSign.setEnabled(false);
				btnSign.setText("注册请求中...");
				new MyThread().start();
			}
		});
	}

	private void nameExist(String name)
	{
		BmobQuery<Users> sql1 = new BmobQuery<Users>();
		sql1.addWhereEqualTo("name", name);
		BmobQuery<Users> sql2 = new BmobQuery<Users>();
		sql2.addWhereEqualTo("mobile", name);
		List<BmobQuery<Users>> sqls = new ArrayList<BmobQuery<Users>>();
		sqls.add(sql1);
		sqls.add(sql2);
		BmobQuery<Users> sql = new BmobQuery<Users>();
		sql.or(sqls);
		sql.addWhereEqualTo("valid", true);
		sql.count(Users.class, new CountListener()
		{
			@Override
			public void done(Integer count, BmobException e)
			{
				if (e == null)
					isNameExist = (count > 0);
				else
					isNameExist = true;
				td1 = false;
			}
		});
	}
	
	private void mailExist(String mail)
	{
		BmobQuery<Users> sql = new BmobQuery<Users>();
		sql.addWhereEqualTo("email", mail);
		sql.addWhereEqualTo("valid", true);
		sql.count(Users.class, new CountListener()
		{
			@Override
			public void done(Integer count, BmobException e)
			{
				if (e == null)
					isEmailExist = (count > 0);
				else
					isEmailExist = true;
				td2 = false;
			}
		});
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
	
	class MyThread extends Thread implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if (isRecheck)
				{
					td1 = td2 = true;
					nameExist(edtUsername.getText().toString());
					mailExist(edtEmail.getText().toString());
					while (td1 || td2) { Thread.sleep(500);}
					//mHandler.sendEmptyMessage(9);
					if (isNameExist)
						mHandler.sendEmptyMessage(10);
					if (isEmailExist)
						mHandler.sendEmptyMessage(11);
					if (!isNameExist && !isEmailExist)
						isRecheck = false;
				}
				if (!isRecheck)
				{
					Users user = new Users();
					user.name = edtUsername.getText().toString().trim();
					user.psd = MD5Util.getMD5(edtPass2.getText().toString());
					user.email = edtEmail.getText().toString().trim();
					user.nickname = edtNick.getText().toString().trim();
					user.gender = rdbMale.isChecked();
					user.mark = "他很懒，什么也没留下";
					user.roles = Common.HULUXIA ? "三楼用户" : "普通用户";
					user.gender = true;
					user.valid = true;
					user.level = 1;
					//add on 20170613 -start
					user.Set_Backimg = Common.APP_BACK_ID;
					user.Set_Theme = Common.APP_THEME_ID;
					//user.Set_Music = Common.MUSIC;
					//-end
					if (Common.IS_ACTIVE)
						user.key = Common.KEY;
					user.save(new SaveListener<String>()
					{
						@Override
						public void done(String s, BmobException e)
						{
							if (e == null)
								mHandler.sendEmptyMessage(1);
							else
								mHandler.sendEmptyMessage(2);
						}
					});
				}
			}
			catch (Exception e)
			{
				mHandler.sendEmptyMessage(2);
				ExceptionHandler.log("SignUserThread", e.toString());
			}
			finally
			{
				mHandler.sendEmptyMessage(12);
			}
		}
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					Common.USERID = "";
					Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
					supportFinishAfterTransition();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
					break;
				case 9:
					Toast.makeText(getApplicationContext(), "namec&email:" + isNameExist + "," + isEmailExist, Toast.LENGTH_SHORT).show();
					break;
				case 10:
					edtUsername.requestFocus();
					edtUsername.setError("该用户名已被使用");
					break;
				case 11:
					edtEmail.requestFocus();
					edtEmail.setError("该邮箱地址已存在");
					break;
				case 12:
					btnSign.setEnabled(true);
					canGoback = true;
					btnSign.setText(R.string.java_sign);
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onBackPressed()
	{
		if (canGoback)
			supportFinishAfterTransition();
	}

}
