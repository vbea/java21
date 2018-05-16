package com.vbea.java21;

import java.lang.reflect.Field;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Button; 
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.EditText;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;

public class UserEdit extends AppCompatActivity
{
	private LayoutInflater inflat;
	private RadioButton rdbMale, rdbFemale;
	private EditText edtNick, edtRemark, edtAddress, edtBirthday;
	private TextView username, chPassword;
	private Button btnSave;
	private Dialog mDialog;
	private View dialogView;
	private EditText oldpass;
	private EditText newpass;
	private EditText querpass;
	private boolean isNewPass = false;
	private String NewPass = "";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercmd);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		btnSave = (Button) findViewById(R.id.btnSave);
		username = (TextView) findViewById(R.id.info_username);
		edtRemark = (EditText) findViewById(R.id.udt_mark);
		edtNick = (EditText) findViewById(R.id.udt_nick);
		edtBirthday = (EditText) findViewById(R.id.udt_birthday);
		rdbMale = (RadioButton) findViewById(R.id.rdbMale);
		rdbFemale = (RadioButton) findViewById(R.id.rdbFemale);
		edtAddress = (EditText) findViewById(R.id.udt_address);
		chPassword = (TextView) findViewById(R.id.udt_password);
	
		setSupportActionBar(tool);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		edtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean f)
			{
				if (!f) return;
				v.clearFocus();
				String[] s = edtBirthday.getText().toString().split("-");
				int year=2017,month=1,day=1;
				if (s != null && s.length == 3)
				{
					year = Integer.parseInt(s[0]);
					month = Integer.parseInt(s[1]);
					day = Integer.parseInt(s[2]);
				}
				mDialog = new DatePickerDialog(UserEdit.this, new DatePickerDialog.OnDateSetListener()
				{
					public void onDateSet(DatePicker v, int _year, int _month, int _day)
					{
						edtBirthday.setText(String.format("%d-%02d-%02d",_year,(_month+1),_day));
						mDialog.dismiss();
					}
				}, year, month-1, day);
				mDialog.show();
			}
		});
		
		btnSave.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String nick = edtNick.getText().toString().trim();
				if (!nick.equals(""))
					Common.mUser.nickname = nick.trim();
				if (isNewPass)
					Common.mUser.psd = NewPass;
				Common.mUser.mark = edtRemark.getText().toString();
				Common.mUser.address = edtAddress.getText().toString();
				Common.mUser.gender = rdbMale.isChecked();
				Common.mUser.birthday = edtBirthday.getText().toString();
				Common.updateUser();
				supportFinishAfterTransition();
			}
		});
		
		chPassword.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shDialog();
			}
		});
		init(Common.mUser);
		Common.Login(this, new Common.LoginListener()
		{
			@Override
			public void onLogin(int code)
			{
				if (code == 1) //登录成功
					init(Common.mUser);
			}

			@Override
			public void onError(String error)
			{
				//登录失败
			}
		});
	}
	
	private void init(Users user)
	{
		username.setText(user.name);
		edtNick.setText(user.nickname);
		edtRemark.setText(toStrings(user.mark));
		if (Common.mUser.gender != null)
		{
			if (user.gender)
				rdbMale.setChecked(true);
			else
				rdbFemale.setChecked(true);
		}
		edtBirthday.setText(toStrings(user.birthday));
		edtAddress.setText(toStrings(user.address));
	}
	
	private boolean getView()
	{
		if (inflat == null)
			inflat = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		dialogView = inflat.inflate(R.layout.editpassword, null);
		TableRow oldRow = (TableRow) dialogView.findViewById(R.id.tabOldPassRow);
		oldpass = (EditText) dialogView.findViewById(R.id.edt_oldPasd);
		newpass = (EditText) dialogView.findViewById(R.id.edt_newPasd);
		querpass = (EditText) dialogView.findViewById(R.id.edt_querPasd);
		if (Common.mUser.psd == null || Common.mUser.psd.equals(""))
		{
			oldRow.setVisibility(View.GONE);
			return true;
		}
		return false;
	}
	
	private String toStrings(String str)
	{
		if (str != null)
			return str;
		return "";
	}
	
	public void shDialog()
	{
		final boolean isEmptyOdp = getView();
		AlertDialog.Builder builder = new AlertDialog.Builder(UserEdit.this);
		builder.setTitle("修改密码");
		builder.setView(dialogView);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int d)
			{
				isShowing(dialog, false);
				if (!isEmptyOdp)
				{
					if (isEmpty(oldpass, "请输入原密码"))
						return;
					if (oldpass.getText().toString().trim().length() < 4)
					{
						oldpass.requestFocus();
						oldpass.setError("密码长度过短");
						return;
					}
				}
				if (isEmpty(newpass, "请输入新密码"))
				{
					return;
				}
				if (newpass.getText().toString().trim().length() < 4)
				{
					newpass.requestFocus();
					newpass.setError("密码长度过短");
					return;
				}
				if (!newpass.getText().toString().equals(querpass.getText().toString()))
				{
					querpass.requestFocus();
					querpass.setError("两次输入密码不一致");
					return;
				}
				if (!isEmptyOdp)
				{
					if (!MD5Util.getMD5(oldpass.getText().toString()).equals(Common.mUser.psd))
					{
						oldpass.setText("");
						oldpass.requestFocus();
						oldpass.setError("原密码错误");
						return;
					}
				}
				isNewPass = true;
				NewPass = MD5Util.getMD5(querpass.getText().toString());
				Util.toastLongMessage(getApplicationContext(), "已提交密码修改请求，点击保存按钮即可修改成功，点击返回则放弃本次修改");
				dialog.cancel();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int d)
			{
				dialog.cancel();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			public void onCancel(DialogInterface dialog)
			{
				isShowing(dialog, true);
				dialog.dismiss();
			}
		});
		final AlertDialog passDialog = builder.create();
		passDialog.setOnShowListener(new DialogInterface.OnShowListener()
		{
			public void onShow(DialogInterface dialog)
			{
				int color = MyThemes.getColorAccent(UserEdit.this);
				Button negative = passDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
				if (negative != null)
					negative.setTextColor(color);
				Button positive = passDialog.getButton(DialogInterface.BUTTON_POSITIVE);
				if (positive != null)
					positive.setTextColor(color);
			}
		});
		passDialog.show();
	}
	
	private boolean isEmpty(EditText view, String tip)
	{
		if (view.getText().toString().length() == 0)
		{
			view.requestFocus();
			view.setError(tip);
			return true;
		}
		return false;
	}
	
	private void isShowing(DialogInterface dialog, boolean state)
	{
		try
		{
			Field f = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			f.setAccessible(true);
			f.set(dialog, state);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("DialogReflect", e.toString());
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
}
