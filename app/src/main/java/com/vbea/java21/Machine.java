package com.vbea.java21;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.net.SocketTimeoutException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.method.LinkMovementMethod;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.Util;
import com.vbea.secret.*;

public class Machine extends AppCompatActivity
{
	private SharedPreferences spf;
	private EditText edtKey;
	private ProgressDialog mDialog;
	private final String PDATE = "2018-05-20";
	//private boolean START = false;
	//private int DATED = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.machine);
		
		//START = getIntent().getBooleanExtra("start", false);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		Button btnSubm = (Button) findViewById(R.id.btn_commitRegist);
		//TextView txtCheck = (TextView) findViewById(R.id.text_probation);
		TextView txtGroup = (TextView) findViewById(R.id.txt_macgroup);
		edtKey = (EditText) findViewById(R.id.edt_key);
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		try
		{
			txtGroup.setMovementMethod(LinkMovementMethod.getInstance());
			txtGroup.setText(getGrouptext());
			SimpleDateFormat text = new SimpleDateFormat("yyyy-MM-dd");
			Date pdate = text.parse(PDATE);
			Date now = new Date();
			if (pdate.getYear() == now.getYear() &&
				pdate.getMonth() == now.getMonth() &&
				pdate.getDate() == now.getDate())
				edtKey.setText("25A15-8C0-0AAAD-0A2-AD714");
			//delete on 20170606
			/*k dec = new k();
			SimpleDateFormat text = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(new Date());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(text.parse(dec.decrypt(Common.SDATE)));
			int dated = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis()) / (1000*3600*24));
			if (dated < 0)//重置为30
				dated = 0;
			if (dated > 30)//重置为0
				dated = 30;
			DATED = 30 - dated;//得到剩余天数
			txtCheck.setText("剩余试用时间：" + DATED + "天" + (DATED == 0 ? "，请尽快激活" : ""));*/
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Machine_main", e.toString());
			closePage();
		}
		btnSubm.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (edtKey.getText().toString().trim().equals(""))
				{
					Util.toastLongMessage(getApplicationContext(), "请输入产品密钥并点击下一步");
					return;
				}
				else if (edtKey.getText().toString().trim().length() != 25)
				{
					Util.toastLongMessage(getApplicationContext(), "请检查你的密钥并重试");
					return;
				}
				activeApp(true);
				//regist(edtKey.getText().toString());
			}
		});
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				closePage();
			}
		});
	}
	
	private SpannableStringBuilder getGrouptext()
	{
		String g1 = "222876252";
		String g2 = "49321718";
		String g3 = "63492019";
		String g4 = "135482843";
		final String g1k = "SqNsfqwYaq_UjKM9SKHvSnQGUMdXPh5g";
		final String g2k = "RfeoDQnuCW9LiWhpb6bdCq5xtZU8KbS-";
		final String g3k = "wLSipl8_yReFcb9yp9a2WeEQYueaSdLm";
		final String g4k = "w5mru7iovlDM0jhLutgQ-ptgn6p93808";
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(getString(R.string.abt_invate));
		ssb.append(g1, new ClickableSpan()
		{
			public void onClick(View v)
			{
				SocialShare.mTencent.joinQQGroup(Machine.this, g1k);
			}
		}, 0);
		ssb.append(getString(R.string.abt_invate0));
		ssb.append(g2, new ClickableSpan()
		{
			public void onClick(View v)
			{
				SocialShare.mTencent.joinQQGroup(Machine.this, g2k);
			}
		}, 0);
		ssb.append(getString(R.string.abt_invate1));
		ssb.append(g3, new ClickableSpan()
		{
			public void onClick(View v)
			{
				SocialShare.mTencent.joinQQGroup(Machine.this, g3k);
			}
		}, 0);
		ssb.append(getString(R.string.abt_invate2));
		ssb.append(g4, new ClickableSpan()
		{
			public void onClick(View v)
			{
				SocialShare.mTencent.joinQQGroup(Machine.this, g4k);
			}
		}, 0);
		ssb.append(getString(R.string.abt_invate3));
		return ssb;
	}
	
	private void activeApp(boolean login)
	{
		try
		{
			String[] keys = edtKey.getText().toString().split("-");
			if (keys.length != 5 || keys[0].length() != 5 || keys[1].length() != 3 || keys[2].length() != 5 || keys[3].length() != 3 || keys[4].length() != 5 || !Util.AuthKey(Machine.this))
			{
				Util.toastLongMessage(getApplicationContext(), "此密钥不适合此程序，请重试");
				edtKey.setText("");
				return;
			}
			if (Common.isNet(Machine.this))
			{
				if (login && !Common.isLogin())
				{
					Util.showConfirmCancelDialog(this, "仍要激活", "请先注册帐号并登录后再激活，以便于将密钥存入账户，以后可在其他设备同步激活", "返回登录", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int s)
						{
							activeApp(false);
						}
					});
					return;
				}
				getDialog("请稍候……");
				new KeyThread(edtKey.getText().toString()).start();
			}
			else
				Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
		}
		catch (Exception e)
		{
			Util.toastLongMessage(getApplicationContext(), "请检查你的密钥并重试");
		}
	}
	
	@Override
	public void onBackPressed()
	{
		closePage();
	}
	
	class KeyThread extends Thread implements Runnable
	{
		String key;
		public KeyThread(String _key)
		{
			key = _key;
		}
		
		private boolean regist() throws Exception
		{
			if (key.length() == 25)
			{
				Keys keys = new SecretKey(key).getInstance();
				return keys.invoke();
			}
			else
				return false;
		}
		
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(3000);
				if (regist())
				{
					Message msg = new Message();
					msg.what = 1;
					msg.obj = key;
					mHandler.sendMessage(msg);
				}
				else
					mHandler.sendEmptyMessage(5);
			}
			catch(Exception e)
			{
				mHandler.sendEmptyMessage(4);
			}
		}
	}
	
	public void getDialog(String title)
	{
		mDialog = ProgressDialog.show(this, "", title, false, false);
	}
		
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					Util.toastShortMessage(getApplicationContext(), "已应用该密钥");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Common.IS_ACTIVE = true;
					if (Common.isLogin())
					{
						Common.mUser.key = msg.obj.toString();
						Common.updateUser();
					}
					SharedPreferences.Editor editor = spf.edit();
					editor.putBoolean("app", true);
					editor.putString("key", msg.obj.toString());
					editor.putString("date", format.format(new Date()));
					editor.putBoolean("active", true);
					if (editor.commit())
						Common.reStart(Machine.this);
					closePage();
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "此密钥不适合此产品，请重试");
					break;
				case 3:
					Util.toastShortMessage(getApplicationContext(), "抱歉，该注册码次数已用完，无法继续注册");
					break;
				case 4:
					Util.toastShortMessage(getApplicationContext(), "密钥不正确");
					break;
				case 5:
					Util.toastShortMessage(getApplicationContext(), "抱歉，该密钥已过期");
					break;
				case 8:
					Util.toastShortMessage(getApplicationContext(), "服务器连接失败");
					break;
				case 9:
					finish();
					break;
			}
			if (mDialog != null && msg.what < 6)
				mDialog.dismiss();
			super.handleMessage(msg);
		}
	};
	
	void closePage()
	{
		//update on 20170606
		/*if (START && DATED > 0)
		{
			Intent intent = new Intent(Machine.this, Main.class);
			if (START)
				intent.putExtra("start", true);
			Common.startActivityOptions(Machine.this, intent);
			mHandler.sendEmptyMessageDelayed(10, 500);
		}
		else*/
			supportFinishAfterTransition();
	}
}

