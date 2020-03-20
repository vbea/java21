package com.vbea.java21;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.Button; 
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.graphics.Bitmap;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MD5Util;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.SettingUtil;
import com.vbea.java21.data.Users;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.exception.BmobException;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.UiError;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import org.json.JSONObject;

public class QQLoginReg extends BaseActivity
{
	private boolean isNameExist, isEmailExist, isRecheck, td1, td2, td3;
	private Users mUser;
	private UserInfo mInfo;
	private ImageView qqIcon;
	private TextView qqNick;
	private CheckBox useQQinfo;
	private Button btnOK;
	private RadioButton radioNew, radioOld;
	private EditText edtUsername, edtEmail, edtUserbind, edtPassword;
	private LinearLayout layoutOld, layoutNew;
	private String iconUrl;
	private Bitmap btIcon;

	@Override
	protected void before()
	{
		setContentView(R.layout.qqlogin_reg);
	}

	@Override
	public void after()
	{
		enableBackButton();
		btnOK = bind(R.id.btn_qqComplete);
		qqIcon = bind(R.id.img_qqIcon);
		qqNick = bind(R.id.txt_qqNick);
		useQQinfo = bind(R.id.chk_useQqIcon);
		radioNew = bind(R.id.rdbNewUser);
		radioOld = bind(R.id.rdbOldUser);
		edtUsername = bind(R.id.edt_qqUsername);
		edtUserbind = bind(R.id.edt_qqUsernameBinding);
		edtEmail = bind(R.id.edt_qqEmail);
		edtPassword = bind(R.id.edt_qqPassword);
		layoutNew = bind(R.id.qqlayout_newuser);
		layoutOld = bind(R.id.qqlayout_olduser);
		mInfo = new UserInfo(QQLoginReg.this, SocialShare.mTencent.getQQToken());
		mHandler.sendEmptyMessage(0);
		
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
		
		radioNew.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View p1)
			{
				changeView(true);
			}
		});
		
		radioOld.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View p1)
			{
				changeView(false);
			}
		});
			
		btnOK.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (radioNew.isChecked())
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
					if (!isEmail(edtEmail.getText().toString()))
					{
						edtEmail.requestFocus();
						edtEmail.setError("邮箱格式不正确");
						return;
					}
				}
				else
				{
					if (isEmpty(edtUserbind, true, "请输入用户名"))
						return;
					if (isEmpty(edtPassword, false, "请输入密码"))
						return;
				}
				if (!Common.isNet(QQLoginReg.this))
				{
					Util.toastShortMessage(getApplicationContext(), "请检查你的网络连接");
					return;
				}
				btnOK.setEnabled(false);
				if (radioNew.isChecked())
				{
					btnOK.setText("注册中...");
					new SignThread().start();
				}
				else
				{
					btnOK.setText("登录中...");
					new BindThread().start();
				}
			}
		});
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
	
	private void init()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					btIcon = Util.getNetBitmap(iconUrl);
					mHandler.sendEmptyMessage(1);
				}
				catch (Exception e)
				{
					ExceptionHandler.log("setIcon", e.toString());
				}
			}
		}).start();
	}
	
	private void changeView(boolean s)
	{
		if (s)
		{
			layoutNew.setVisibility(View.VISIBLE);
			layoutOld.setVisibility(View.GONE);
		}
		else
		{
			layoutOld.setVisibility(View.VISIBLE);
			layoutNew.setVisibility(View.GONE);
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
					mInfo.getUserInfo(new MyIListener());
					break;
				case 1:
					if (btIcon != null)
						qqIcon.setImageBitmap(btIcon);
					break;
				case 2:
					Common.USERID = "";
					Util.toastShortMessage(getApplicationContext(), "登录成功");
					supportFinishAfterTransition();
					break;
				case 3:
					Util.toastShortMessage(getApplicationContext(), "登录失败");
					btnOK.setText("重试");
					btnOK.setEnabled(true);
					break;
				case 10:
					edtUsername.requestFocus();
					edtUsername.setError("该用户名已被使用");
					btnOK.setText(R.string.complete);
					btnOK.setEnabled(true);
					break;
				case 11:
					edtEmail.requestFocus();
					edtEmail.setError("该邮箱地址已存在");
					btnOK.setText(R.string.complete);
					btnOK.setEnabled(true);
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	class MyIListener implements IUiListener
	{
		@Override
		public void onComplete(Object p)
		{
			JSONObject json = (JSONObject)p;
			try
			{
				//ExceptionHandler.log("json", p.toString());
				if (mInfo != null)
			 	{
					if (json.has("nickname"))
					{
						mUser = new Users();
						String nickname = json.getString("nickname").trim();
						qqNick.setText(nickname);
						mUser.nickname = nickname;
						if (json.has("gender"))
							mUser.gender = json.getString("gender").equals("男");
						else
							mUser.gender = true;
						if (json.has("province"))
						{
							StringBuilder _add = new StringBuilder();
							_add.append(json.getString("province"));
							if (json.has("city"))
							{
								_add.append("-");
								_add.append(json.getString("city"));
							}
							mUser.address = _add.toString();
						}
						if (json.has("figureurl_qq_2"))
							iconUrl = json.getString("figureurl_qq_2");
						else
							iconUrl = "";
						mUser.qq = nickname;
						mUser.qqId = SocialShare.mTencent.getOpenId();
						//btIcon = Util.getNetBitmap(iconUrl);
						//mHandler.sendEmptyMessage(1);
						if (!Util.isNullOrEmpty(iconUrl))
							init();
						else
							btIcon = null;
					}
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("uiListener", e.toString());
			}
		}
		@Override
		public void onError(UiError p1)
		{
			Util.toastShortMessage(getApplicationContext(), p1.errorMessage);
		}
		@Override
		public void onCancel()
		{
		}
	}
	
	public File getIconFile() throws Exception
	{
		//String filename = MD5Util.getMD5(btIcon, mUser.name) + ".png";
		File dir = new File(Common.getIconPath());
		if (!dir.exists())
			dir.mkdirs();
		File file = new File(Common.getAvatarPath());
		saveFile(btIcon, file);
		return file;
	}
	
	public void saveFile(Bitmap bm, File file) throws IOException
	{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

	public void uploadIcon(File file)
	{
		final BmobFile icon = new BmobFile(file);
		icon.upload(new UploadFileListener()
		{
			@Override
			public void done(BmobException p1)
			{
				if (p1 == null)
				{
					mUser.icon = icon;
					td3 = false;
				}
			}
		});
	}
	
	class SignThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				//注册新用户
				if (isRecheck)
				{
					td1 = td2 = true;
					nameExist(edtUsername.getText().toString());
					mailExist(edtEmail.getText().toString());
					while (td1 || td2) { Thread.sleep(500);}
					if (isNameExist)
						mHandler.sendEmptyMessage(10);
					if (isEmailExist)
						mHandler.sendEmptyMessage(11);
					if (!isNameExist && !isEmailExist)
						isRecheck = false;
				}
				if (!isRecheck)
				{
					td3 = true;
					mUser.name = edtUsername.getText().toString().trim();
					mUser.email = edtEmail.getText().toString();
					if (btIcon != null)
					{
						File file = getIconFile();
						if (file.exists())
						{
							uploadIcon(file);
							while (td3) {sleep(500);}
						}
					}
					if (mUser.gender == null)
						mUser.gender = false;
					mUser.psd = "";
					mUser.valid = true;
					mUser.role = 2;
					mUser.mark = "他很懒，什么也没留下";
					//add on 20180405 -start
					mUser.dated = 0;
					mUser.lastLogin = new BmobDate(new Date());
					mUser.settings = Common.getSettingJson(new SettingUtil());
					//mUser.Set_Music = Common.MUSIC;
					//-end
					if (Common.IS_ACTIVE)
						mUser.key = Common.KEY;
					Users _m = mUser;
					_m.save(new SaveListener<String>()
					{
						@Override
						public void done(String s, BmobException e)
						{
							if (e == null)
							{
								Common.mUser = mUser;
								Common.IsChangeICON = true;
								mHandler.sendEmptyMessage(2);
							}
							else
							{
								ExceptionHandler.log("QQregThread", e.toString());
								mHandler.sendEmptyMessage(3);
							}
						}
					});
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.log("qqregThread", e.toString());
			}
		}
	}
	
	class BindThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				//绑定老用户
				Common.Login(QQLoginReg.this, edtUserbind.getText().toString(), MD5Util.getMD5(edtPassword.getText().toString()), false, new Common.LoginListener()
				{
					@Override
					public void onLogin(int code)
					{
						if (Common.mUser != null && code == 1)
						{
							if (useQQinfo.isChecked())
							{
								mUser.name = Common.mUser.name;
								try
								{
									File file = getIconFile();
									if (file.exists())
										uploadIcon(file);//上传头像
								}
								catch (Exception e) {}
								mUser.setObjectId(Common.mUser.getObjectId());
								mUser.update(new UpdateListener()
								{
									public void done(BmobException e)
									{
										if (e != null)
											ExceptionHandler.log("QQreg_qqBind_update", e.toString());
									}
								});
								//同步用户资料
								Common.mUser.nickname = mUser.nickname;
								Common.mUser.qq = mUser.qq;
								Common.mUser.qqId = mUser.qqId;
								Common.mUser.icon = mUser.icon;
								if (Common.mUser.address == null || Common.mUser.address.equals(""))
									Common.mUser.address = mUser.address;
							}
							else
							{
								Users _user = new Users();
								_user.setObjectId(Common.mUser.getObjectId());
								_user.qq = mUser.qq;
								_user.qqId = mUser.qqId;
								if (Common.mUser.address == null || Common.mUser.address.equals(""))
									_user.address = mUser.address;
								_user.update(new UpdateListener()
								{
									public void done(BmobException e)
									{	
										if (e != null)
											ExceptionHandler.log("QQreg_qqBind_update2", e.toString());
									}
								});
								//同步用户资料
								Common.mUser.qq = _user.qq;
								Common.mUser.qqId = _user.qqId;
							}
							mHandler.sendEmptyMessage(2);
						}
						else
							mHandler.sendEmptyMessage(3);
					}

					@Override
					public void onError(String error)
					{
						mHandler.sendEmptyMessage(3);
					}
				});
			}
			catch (Exception e)
			{
				ExceptionHandler.log("qqregThread", e.toString());
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		SocialShare.mTencent.logout(this);
		super.onDestroy();
	}
}
