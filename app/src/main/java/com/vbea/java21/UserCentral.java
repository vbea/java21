package com.vbea.java21;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import org.json.JSONObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.exception.BmobException;

public class UserCentral extends AppCompatActivity
{
	private ImageView icon;
	private LinearLayout topLayout;
	private RelativeLayout userTop;
	private AppBarLayout appbar;
	private AlphaAnimation appears, disappears;
	private TextView titleName, username, nickname, level, gender, mobile, email, qq, birthday, address, mark, roles;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		
		appbar = (AppBarLayout) findViewById(R.id.appbar);
		userTop = (RelativeLayout) findViewById(R.id.user_top);
		topLayout = (LinearLayout) findViewById(R.id.topLayout);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		icon = (ImageView) findViewById(R.id.user_icon);
		titleName = (TextView) findViewById(R.id.user_topName);
		username = (TextView) findViewById(R.id.info_username);
		nickname = (TextView) findViewById(R.id.info_nickname);
		gender = (TextView) findViewById(R.id.info_gender);
		level = (TextView) findViewById(R.id.info_level);
		mobile = (TextView) findViewById(R.id.info_mobile);
		email = (TextView) findViewById(R.id.info_email);
		qq = (TextView) findViewById(R.id.info_qq);
		birthday = (TextView) findViewById(R.id.info_birthday);
		address = (TextView) findViewById(R.id.info_address);
		roles = (TextView) findViewById(R.id.info_role);
		mark = (TextView) findViewById(R.id.info_mark);
		Button logout = (Button) findViewById(R.id.btn_logout);
		TableRow btnMobile = (TableRow) findViewById(R.id.btn_info_mobile);
		TableRow btnQQ = (TableRow) findViewById(R.id.btn_info_qq);
		titleName.setText(Common.mUser.nickname);
		ViewGroup.LayoutParams para = appbar.getLayoutParams();
		para.height = (int)(getWindowManager().getDefaultDisplay().getWidth() / 1.4);
		appbar.setLayoutParams(para);
		setSupportActionBar(tool);
		if (Common.APP_BACK_ID != 0)
		{
			if (Common.APP_BACK_ID == 100)
				userTop.setBackground(Common.getHomeBack());
			else
				userTop.setBackgroundResource(MyThemes.getDrawerBack());
		}
		else
			userTop.setBackgroundResource(MyThemes.getColorPrimary());
		if (MyThemes.homeTextColor != 0)
		{
			titleName.setTextColor(MyThemes.homeTextColor);
			titleName.setShadowLayer(2f, 2f, 2f, MyThemes.homeTextShadow);
		}
		userTop.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (topLayout.getVisibility() == View.VISIBLE)
				{
					topLayout.startAnimation(disappears);
				}
				else
				{
					topLayout.startAnimation(appears);
					topLayout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (topLayout.getVisibility() == View.GONE)
				{
					topLayout.startAnimation(appears);
					topLayout.setVisibility(View.VISIBLE);
				}
				supportFinishAfterTransition();
			}
		});
		icon.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOption(UserCentral.this, new Intent(UserCentral.this, IconPreview.class), icon, "icon_pre");
			}
		});
		btnMobile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (Common.mUser.mobile != null && Common.mUser.mobile.length() > 0)
					mobileDialog();
				else
					Common.startActivityOptions(UserCentral.this, BindMobile.class);
			}
		});
		btnQQ.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (Common.mUser.qq != null && Common.mUser.qq.length() > 0)
					qqDialog();
				else
					SocialShare.mTencent.login(UserCentral.this, "all", new MyIListener());
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (Common.isCanUploadUserSetting())
					Common.updateUser();
				Util.showConfirmCancelDialog(UserCentral.this, "注销", "您确定要退出登录？",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int w)
					{
						Common.Logout(UserCentral.this);
						Common.startActivityOptions(UserCentral.this, Login.class);
						new Handler().postDelayed(new Runnable()
						{
							public void run()
							{
								finish();
							}
						}, 500);
					}
				});
			}
		});
		setIcon();
	}
	
	public void setIcon()
	{
		Common.setIcon(icon, this, false);
		appears = new AlphaAnimation(0, 1);
		appears.setDuration(500);
		disappears = new AlphaAnimation(1, 0);
		disappears.setDuration(500);
		disappears.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation p1)
			{
				
			}

			@Override
			public void onAnimationEnd(Animation p1)
			{
				topLayout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation p1)
			{
				
			}
		});
	}
	
	private void qqDialog()
	{
		AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
		dialogBuild.setTitle("绑定QQ");
		dialogBuild.setItems(new String[] {"换绑","取消绑定"}, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				dialog.dismiss();
				switch (item)
				{
					case 0:
						SocialShare.mTencent.login(UserCentral.this, "all", new MyIListener());
						break;
					case 1:
					{
						unbindDialog("QQ", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								Common.mUser.qq = "";
								Common.mUser.qqId = "";
								Common.updateUser();
								inicBind(Common.mUser);
							}
						});
						break;
					}
				}
			}
		});
		dialogBuild.show();
	}
	
	private void mobileDialog()
	{
		AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
		dialogBuild.setTitle("绑定手机");
		dialogBuild.setItems(new String[] {"换绑","取消绑定"}, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{
				dialog.dismiss();
				switch (item)
				{
					case 0:
						Common.startActivityOptions(UserCentral.this, BindMobile.class);
						break;
					case 1:
					{
						unbindDialog("手机", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								Common.mUser.mobile = "";
								Common.updateUser();
								inicBind(Common.mUser);
							}
						});
						break;
					}
				}
			}
		});
		dialogBuild.show();
	}
	
	public void unbindDialog(String type, DialogInterface.OnClickListener lis)
	{
		Util.showConfirmCancelDialog(this, "解绑" + type, "您确定要解绑"+type+"？", lis);
	}
	
	public void inic()
	{
		Users user = Common.mUser;
		username.setText(user.name);
		nickname.setText(user.nickname);
		titleName.setText(user.nickname);
		level.setText("Lv " + user.level);
		roles.setText(user.roles);
		gender.setText(user.gender ? "男" : "女");
		birthday.setText(user.birthday);
		address.setText(user.address);
		mark.setText(user.mark);
		inicBind(user);
	}
	
	private void inicBind(Users user)
	{
		if (user.email != null && !user.email.equals(""))
			email.setText(getSecstr(user.email, 4, 4));
		else
			email.setText("未绑定");
		if (user.mobile != null && !user.mobile.equals(""))
			mobile.setText(getSecstr(user.mobile, 3, 4));
		else
			mobile.setText("未绑定");
		if (user.qq != null && !user.qq.equals(""))
			qq.setText(user.qq);
		else
			qq.setText("未绑定");
	}
	
	private String getSecstr(String str, int start, int end)
	{
		String regex = "(\\S{"+start+"})(.*)(\\S{"+end+"})";
		Matcher m = Pattern.compile(regex).matcher(str);
		if (m.find())
		{
			String rep = m.group(2);
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<rep.length(); i++)
			{
				sb.append("*");
			}
			return str.replaceAll(rep, sb.toString());
		}
		return str;
	}

	@Override
	protected void onResume()
	{
		if (Common.IsChangeICON)
			setIcon();
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				//appbar.setTransitionName(null);
				inic();
			}
		}, 500);
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.user_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_edit)
		{
			Common.startActivityOptions(UserCentral.this, UserEdit.class);
		}
		return true;
	}

	@Override
	public void onBackPressed()
	{
		if (topLayout.getVisibility() == View.GONE)
		{
			topLayout.startAnimation(appears);
			topLayout.setVisibility(View.VISIBLE);
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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
					final String openId = json.getString(Constants.PARAM_OPEN_ID);
					SocialShare.mTencent.setAccessToken(token, expires);
					SocialShare.mTencent.setOpenId(openId);
					BmobQuery<Users> sql = new BmobQuery<Users>();
					sql.addWhereEqualTo("qqId", openId);
					sql.addWhereEqualTo("valid", true);
					sql.count(Users.class, new CountListener()
					{
						@Override
						public void done(Integer count, BmobException e)
						{
							if (e == null)
							{
								if (count > 0)
								{
									if (Common.mUser.qqId.equals(openId))
										mHandler.sendEmptyMessage(1);
									else
										mHandler.sendEmptyMessage(2);
								}
								else
								{
									UserInfo mInfo = new UserInfo(UserCentral.this, SocialShare.mTencent.getQQToken());
									mInfo.getUserInfo(new MyIListener());
								}
							}
							else
								mHandler.sendEmptyMessage(0);
						}
					});
				}
				else if (json.has("nickname"))
				{
					String nickname = json.getString("nickname").trim();
					Common.mUser.qq = nickname;
					Common.mUser.qqId = SocialShare.mTencent.getOpenId();
					Common.updateUser();
					mHandler.sendEmptyMessage(3);
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("qqBindIUiListener", e.toString());
			}
		}
		@Override
		public void onError(UiError p1)
		{
			Util.toastShortMessage(getApplicationContext(), "绑定失败");
		}
		@Override
		public void onCancel()
		{
			
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
	
	Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					Util.toastShortMessage(getApplicationContext(), "操作失败");
					break;
				case 1:
					Util.toastShortMessage(getApplicationContext(), "已绑定此QQ，不需要重新绑定");
					break;
				case 2:
					Util.toastShortMessage(getApplicationContext(), "此QQ已绑定到其他用户，不能再次绑定");
					break;
				case 3:
					Util.toastShortMessage(getApplicationContext(), "绑定成功，以后可用此QQ快捷登录");
					inicBind(Common.mUser);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
