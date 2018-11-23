package com.vbea.java21;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.design.widget.AppBarLayout;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.AstroUtil;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.view.MyAlertDialog;
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

public class UserCentral extends BaseActivity
{
	private ImageView icon;
	private LayoutInflater inflat;
	private LinearLayout topLayout;
	private RelativeLayout userTop;
	private AppBarLayout appbar;
	private AlphaAnimation appears, disappears;
	private TextView titleName, username, level, gender, mobile, email, qq, birthday, address, mark, roles;
	private View dialogView;
	private EditText oldpass, newpass, querpass;
	private StringBuilder sb;

	@Override
	protected void before()
	{
		setContentView(R.layout.userinfo);
	}
	
	@Override
	public void after()
	{
		enableBackButton();
		appbar = bind(R.id.appbar);
		userTop = bind(R.id.user_top);
		topLayout = bind(R.id.topLayout);
		icon = bind(R.id.user_icon);
		titleName = bind(R.id.user_topName);
		username = bind(R.id.info_username);
		//nickname = bind(R.id.info_nickname);
		gender = bind(R.id.info_gender);
		level = bind(R.id.info_level);
		mobile = bind(R.id.info_mobile);
		email = bind(R.id.info_email);
		qq = bind(R.id.info_qq);
		birthday = bind(R.id.info_birthday);
		address = bind(R.id.info_address);
		roles = bind(R.id.info_role);
		mark = bind(R.id.info_mark);
		Button logout = bind(R.id.btn_logout);
		TableRow btnMobile = bind(R.id.btn_info_mobile);
		TableRow btnQQ = bind(R.id.btn_info_qq);
		TableRow btnInfo = bind(R.id.btn_info_user);
		if (Common.isLogin())
			titleName.setText(Common.mUser.nickname);
		else
			return;
		ViewGroup.LayoutParams para = appbar.getLayoutParams();
		para.height = (int)(getWindowManager().getDefaultDisplay().getWidth() / 1.4);
		appbar.setLayoutParams(para);
		if (Common.APP_BACK_ID == 100)
			userTop.setBackground(Common.getHomeBack());
		else
			userTop.setBackgroundResource(MyThemes.getDrawerBack());
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
		
		icon.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Common.startActivityOption(UserCentral.this, new Intent(UserCentral.this, IconPreview.class), icon, "icon_pre");
			}
		});
		btnInfo.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyAlertDialog dialog = new MyAlertDialog(UserCentral.this);
				dialog.setTitle("个人档案");
				dialog.setMessage(sb.toString());
				dialog.setNeutralButton("修改密码", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i)
					{
						d.dismiss();
						shDialog();
					}
				});
				dialog.setNegativeButton("关闭", null);
				dialog.show();
			}
		});
		btnMobile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (!Util.isNullOrEmpty(Common.mUser.mobile))// != null && Common.mUser.mobile.length() > 0)
					mobileDialog();
				else
					Common.startActivityOptions(UserCentral.this, BindMobile.class);
			}
		});
		btnQQ.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (!Util.isNullOrEmpty(Common.mUser.qq))// != null && Common.mUser.qq.length() > 0)
					qqDialog();
				else
					SocialShare.mTencent.login(UserCentral.this, "all", new MyIListener());
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
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
		MyAlertDialog dialogBuild = new MyAlertDialog(this);
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
		MyAlertDialog dialogBuild = new MyAlertDialog(this);
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
	
	public void init()
	{
		Users user = Common.mUser;
		if (user == null)
			return;
		username.setText(getString(user.name));
		//nickname.setText(_nick);
		titleName.setText(getString(user.nickname));
		level.setText(getUserLevel(user.dated));
		roles.setText(getUserRole(user.role));
		if (user.gender != null)
			gender.setText(user.gender ? "男" : "女");
		else
			gender.setText("妖");
		birthday.setText(new AstroUtil().getInfo(user.birthday));
		address.setText(getString(user.address));
		mark.setText(getString(user.mark));
		inicBind(user);
		if (sb == null)
		{
			sb = new StringBuilder();
			sb.append("用户名：");
			sb.append(user.name);
			sb.append("\n等级：");
			sb.append(level.getText().toString());
			sb.append("\n注册时间：");
			sb.append(user.getCreatedAt());
			sb.append("\n上次登录：");
			sb.append(Common.OldLoginDate);
			sb.append("\n累计登录：");
			sb.append(user.dated);
			sb.append("天");
		}
	}
	
	private void inicBind(Users user)
	{
		if (!Util.isNullOrEmpty(user.email))
			email.setText(Util.getSecstr(user.email, 4, 4));
		else
			email.setText("未绑定");
		if (!Util.isNullOrEmpty(user.mobile))
			mobile.setText(Util.getSecstr(user.mobile, 3, 4));
		else
			mobile.setText("未绑定");
		if (!Util.isNullOrEmpty(user.qq))
			qq.setText(user.qq);
		else
			qq.setText("未绑定");
	}
	
	//获取用户等级
	private String getUserLevel(Integer dated)
	{
		if (dated == null)
			dated = 0;
		if (dated < 10)
			return "Lv 1";
		else if (dated < 30)
			return "Lv 2";
		else if (dated < 60)
			return "Lv 3";
		else if (dated < 100)
			return "Lv 4";
		else if (dated < 150)
			return "Lv 5";
		else if (dated < 200)
			return "Lv 6";
		else if (dated < 250)
			return "Lv 7";
		else if (dated < 300)
			return "Lv 8";
		else if (dated < 360)
			return "Lv 9";
		else if (dated > 500)
			return "Lv 10";
		return "";
	}
	
	//获取用户角色信息
	private String getUserRole(Integer roles)
	{
		String role = "注册用户";
		if (roles != null)
		{
			switch (roles)
			{
				case 1:
					role = "普通用户";
					break;
				case 2:
					role = "QQ用户";
					break;
				case 3:
					role = "三楼用户";
					break;
				case 4:
					role = "版主";
					break;
				case 5:
					role = "测试账号";
					break;
				case 10:
					role = "管理员";
					break;
				case 11:
					role = "普通会员";
					break;
				case 12:
					role = "超级会员";
					break;
				case 0:
					role = "小黑屋";
					break;
			}
		}
		return role;
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
				init();
			}
		}, 500);
		super.onResume();
	}
	
	private String getString(String s)
	{
		if (Util.isNullOrEmpty(s))
			return "";
		else
			return s;
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
	
	public void shDialog()
	{
		final boolean isEmptyOdp = getView();
		AlertDialog.Builder builder = new AlertDialog.Builder(UserCentral.this);
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
				Common.mUser.psd = MD5Util.getMD5(querpass.getText().toString());
				Common.updateUser();
				Util.toastLongMessage(getApplicationContext(), "操作成功");
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
				int color = MyThemes.getColorAccent(UserCentral.this);
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

	@Override
	protected void onFinish()
	{
		if (topLayout.getVisibility() == View.GONE)
		{
			topLayout.startAnimation(appears);
			topLayout.setVisibility(View.VISIBLE);
		}
		super.onFinish();
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
