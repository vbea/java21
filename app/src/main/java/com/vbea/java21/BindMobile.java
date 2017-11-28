package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.exception.BmobException;
import com.vbea.java21.data.Users;
import com.vbea.java21.classes.ExceptionHandler;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import com.vbea.java21.classes.*;

public class BindMobile extends AppCompatActivity
{
	private EditText edtPhone, edtCheckCode;
	private TextView btnCheck;
	private Button btnBinding;
	private boolean isNameExist, isRecheck, td;
	private int smsCode = 0;
	private String getCheckC = "获取验证码";
	private String getReCheck = " 重试(%02d) ";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bdphone);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		edtPhone = (EditText) findViewById(R.id.edt_bdphone);
		edtCheckCode = (EditText) findViewById(R.id.edt_checkcode);
		btnCheck = (TextView) findViewById(R.id.bd_getcheck);
		btnBinding = (Button) findViewById(R.id.btn_bdingPhone);
		setSupportActionBar(tool);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		btnCheck.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isEmpty(edtPhone, "请输入手机号码"))
					return;
				if (!isPhoneNumber(edtPhone.getText().toString()))
				{
					edtPhone.setError("请输入正确的手机号码");
					edtPhone.requestFocus();
					return;
				}
				btnCheck.setEnabled(false);
				btnCheck.setTextColor(getResources().getColor(R.color.gray));
				new CheckThread().start();
			}
		});
		edtPhone.addTextChangedListener(new TextWatcher()
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
		
		btnBinding.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (isEmpty(edtPhone, "请输入手机号码"))
					return;
				if (!isPhoneNumber(edtPhone.getText().toString()))
				{
					edtPhone.setError("请输入正确的手机号码");
					edtPhone.requestFocus();
					return;
				}
				if (isEmpty(edtCheckCode, "请输入验证码"))
					return;
				btnBinding.setText("请稍候……");
				btnBinding.setEnabled(false);
				new VerifyThread().start();
			}
		});
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
	
	private boolean isPhoneNumber(String phone)
	{
		String check = "^((13[0-9])|(15[^4,\\D])|(147)|(17[0-9])|(18[0-9]))\\d{8}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(phone);
		return matcher.matches();
	}
	
	private void sendSMS(String phone)
	{
		BmobSMS.requestSMSCode(phone, "短信验证码", new QueryListener<Integer>()
		{
			@Override
			public void done(Integer p, BmobException e)
			{
				if (e == null)
				{
					mHandler.sendEmptyMessage(4);
					smsCode = p;
				}
				else
				{
					mHandler.sendEmptyMessage(5);
					smsCode = -1;
				}
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
				td = false;
			}
		});
	}
	
	class CheckThread extends Thread
	{
		public void run()
		{
			try
			{
				if (isRecheck)
				{
					td = true;
					nameExist(edtPhone.getText().toString());
					while (td) { Thread.sleep(500);}
					if (isNameExist)
						mHandler.sendEmptyMessage(2);
					else
						isRecheck = false;
				}
				if (!isRecheck)
				{
					sendSMS(edtPhone.getText().toString());
					while (smsCode == 0) {sleep(500);}
					if (smsCode > 0)
					{
						mHandler.sendEmptyMessage(3);
						int sec = 60;
						while (sec > 0)
						{
							Message msg = new Message();
							msg.what = 1;
							msg.arg1 = sec;
							sec--;
							mHandler.sendMessage(msg);
							sleep(1000);
						}
					}
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("bdf_thread", e.toString());
			}
			finally
			{
				mHandler.sendEmptyMessage(0);
			}
		}
	}
	
	class VerifyThread extends Thread
	{
		public void run()
		{
			try
			{
				BmobSMS.verifySmsCode(edtPhone.getText().toString(), edtCheckCode.getText().toString(), new UpdateListener()
				{
					public void done(BmobException e)
					{
						if (e == null)
						{
							Common.mUser.mobile = edtPhone.getText().toString();
							Common.updateUser();
							mHandler.sendEmptyMessage(6);
						}
						else
							mHandler.sendEmptyMessage(7);
					}
				});
			}
			catch (Exception e)
			{
				ExceptionHandler.log("bg_verify", e.toString());
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
				case 0:
					btnCheck.setEnabled(true);
					btnCheck.setText(getCheckC);
					btnCheck.setTextColor(getResources().getColor(MyThemes.getColorAccent()));
					edtPhone.setEnabled(true);
					break;
				case 1:
					btnCheck.setText(String.format(getReCheck, msg.arg1));
					break;
				case 2:
					edtPhone.setError("该号码已被使用");
					edtPhone.requestFocus();
					break;
				case 3:
					edtPhone.setEnabled(false);
					break;
				case 4:
					Util.toastShortMessage(getApplicationContext(), "验证码发送成功，请注意查收");
					break;
				case 5:
					Util.toastShortMessage(getApplicationContext(), "验证码发送失败");
					break;
				case 6:
					Util.toastShortMessage(getApplicationContext(), "绑定成功");
					supportFinishAfterTransition();
					break;
				case 7:
					Util.toastShortMessage(getApplicationContext(), "验证码不正确，请重试");
					btnBinding.setEnabled(true);
					btnBinding.setText(R.string.java_binding);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
