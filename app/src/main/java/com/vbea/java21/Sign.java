package com.vbea.java21;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.os.Handler;
import android.os.Message;
import android.widget.Button; 
import android.widget.RadioButton;
import android.widget.EditText;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.SettingUtil;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.exception.BmobException;

public class Sign extends BaseActivity
{
	private RadioButton rdbMale;
	private EditText edtUsername, edtPass1, edtPass2, edtEmail, edtNick;
	private boolean isNameExist, isEmailExist, isRecheck, td1, td2;
	private Button btnSign;
	private boolean canGoback = true;

	@Override
	protected void before()
	{
		setContentView(R.layout.sign);
	}

	@Override
	public void after()
	{
		btnSign = bind(R.id.btnSign);
		edtUsername = bind(R.id.signUsername);
		edtPass1 = bind(R.id.signPassword);
		edtPass2 = bind(R.id.signQuerPass);
		edtEmail = bind(R.id.signEmail);
		edtNick = bind(R.id.signNick);
		rdbMale = bind(R.id.rdbMale);
		
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
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
					Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
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
		try
		{
			String check = "^([0-9A-z]+[_|-|\\.]?)+[0-9A-z]?@([0-9A-z]+\\.)+[A-z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			return matcher.matches();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("isEmail", e.toString());
			return false;
		}
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
					user.role = Common.HULUXIA ? 3 : 1;
					user.gender = rdbMale.isChecked();
					user.valid = true;
					//add on 20180405 -start
					user.dated = 0;
					user.lastLogin = new BmobDate(new Date());
					user.settings = Common.getSettingJson(new SettingUtil());
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
					Util.toastShortMessage(getApplicationContext(), "注册成功");
					supportFinishAfterTransition();
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "注册失败");
					break;
				case 9:
					Util.toastShortMessage(getApplicationContext(), "namec&email:" + isNameExist + "," + isEmailExist);
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
