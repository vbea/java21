package com.vbea.java21;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.data.Users;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MyAlertDialog;
import com.vbea.java21.classes.ExceptionHandler;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.exception.BmobException;

public class ResetPassword extends BaseActivity
{
	private EditText newPsd, queryPsd;
	private String objectId;
	private Button reset;

	@Override
	protected void before()
	{
		setContentView(R.layout.reset);
	}

	@Override
	protected void after()
	{
		reset = (Button) findViewById(R.id.btnReset);
		TextView username = (TextView) findViewById(R.id.resUsername);
		newPsd = (EditText) findViewById(R.id.resNewPassword);
		queryPsd = (EditText) findViewById(R.id.resQuerPsd);
		username.setText(getIntent().getExtras().getString("user", ""));
		objectId = getIntent().getExtras().getString("oid", "");
		if (objectId.equals(""))
		{
			Util.toastShortMessage(getApplicationContext(), "获取用户信息失败，请重试");
			reset.setEnabled(false);
		}
		toolbar.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				exitDialog();
			}
		});
		
		reset.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (newPsd.getText().toString().length() == 0)
				{
					newPsd.requestFocus();
					newPsd.setError("请输入密码");
					return;
				}
				if (newPsd.getText().toString().trim().length() < 4)
				{
					newPsd.requestFocus();
					newPsd.setError("密码长度过短");
					return;
				}
				if (!newPsd.getText().toString().equals(queryPsd.getText().toString()))
				{
					queryPsd.requestFocus();
					queryPsd.setError("两次输入密码不一致");
					return;
				}
				if (!Common.isNet(ResetPassword.this))
				{
					Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
					return;
				}
				reset.setText("请稍候...");
				reset.setEnabled(false);
				new MyThread().start();
			}
		});
	}
	
	private void exitDialog()
	{
		MyAlertDialog build = new MyAlertDialog(this);
		build.setTitle(android.R.string.dialog_alert_title);
		build.setMessage("您确定要放弃此次重置密码并返回？");
		build.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int i)
			{
				supportFinishAfterTransition();
			}
		});
		build.setNegativeButton(android.R.string.cancel, null);
		build.show();
	}

	@Override
	public void onBackPressed()
	{
		exitDialog();
	}
	
	class MyThread extends Thread implements Runnable
	{
		public void run()
		{
			Users user = new Users();
			user.setValue("psd", MD5Util.getMD5(queryPsd.getText().toString()));
			user.update(objectId, new UpdateListener()
			{
				@Override
				public void done(BmobException e)
				{
					if (e == null)
						handler.sendEmptyMessage(1);
					else
						handler.sendEmptyMessage(2);
				}
			});
		}
	}
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					Util.toastShortMessage(getApplicationContext(), "密码重置成功，你现在可以使用新密码登录了");
					supportFinishAfterTransition();
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "操作失败，请重试");
					reset.setEnabled(true);
					reset.setText(R.string.resetpsd);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
