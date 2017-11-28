package com.vbea.java21;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.data.Feedbacks;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.exception.BmobException;

import com.vbea.java21.classes.Common;

public class Feedback extends AppCompatActivity
{
	EditText txtFeedback,txtContact;
	Button btnFeed;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		btnFeed = (Button) findViewById(R.id.btn_feed);
		txtFeedback = (EditText) findViewById(R.id.txt_feed);
		txtContact = (EditText) findViewById(R.id.txt_feedCon);
		
		setSupportActionBar(tool);
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		btnFeed.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (txtFeedback.getText().toString().trim().length() <= 0)
				{
					Toast.makeText(getApplicationContext(), "写点什么吧(ಥ_ಥ)", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Common.isNet(Feedback.this))
				{
					Toast.makeText(getApplicationContext(), "网络开小差了，无法提交", Toast.LENGTH_SHORT).show();
					return;
				}
				btnFeed.setEnabled(false);
				btnFeed.setText("正在提交...");
				new Thread()
				{
					public void run()
					{
						try
						{
							PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
							String device = Build.MODEL + "(" + Build.DEVICE + ", Android " + Build.VERSION.RELEASE + ")";
							String version = info.versionName + "(" + info.versionCode + ")";
							Feedbacks feed = new Feedbacks();
							feed.message = txtFeedback.getText().toString().trim();
							feed.contact = txtContact.getText().toString().trim();
							feed.device = device;
							feed.version = version;
							if (Common.mUser != null)
								feed.user = Common.mUser.name;
							else
								feed.user = "游客";
							feed.save(new SaveListener<String>()
							{
								public void done(String s, BmobException e)
								{
									if (e == null)
										mHandler.sendEmptyMessage(1);
									else
										mHandler.sendEmptyMessage(2);
								}
							});
						}
						catch (Exception e)
						{
							mHandler.sendEmptyMessage(3);
						}
					}
				}.start();
				//Toast.makeText(getApplicationContext(), device, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					Toast.makeText(getApplicationContext(), "提交成功，感谢你的反馈！", Toast.LENGTH_SHORT).show();
					supportFinishAfterTransition();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "提交失败，你可以再试一次", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(getApplicationContext(), "提交失败，可能是网络问题", Toast.LENGTH_SHORT).show();
					break;
			}
			btnFeed.setEnabled(true);
			btnFeed.setText("提交");
			super.handleMessage(msg);
		}
	};
}
