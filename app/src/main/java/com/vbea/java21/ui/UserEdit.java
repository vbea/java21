package com.vbea.java21.ui;

import android.app.Dialog;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.view.View;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Users;

public class UserEdit extends BaseActivity {
    private RadioButton rdbMale, rdbFemale;
    private EditText edtNick, edtRemark, edtAddress, edtBirthday;
    //private TextView username, chPassword;
    private Button btnSave;
    private Dialog mDialog;

    //private boolean isNewPass = false;
    //private String NewPass = "";

    @Override
    protected void before() {
        setContentView(R.layout.usercmd);
    }

    @Override
    public void after() {
        enableBackButton(R.id.toolbar);
        btnSave = bind(R.id.btnSave);
        //username = bind(R.id.info_username);
        edtRemark = bind(R.id.udt_mark);
        edtNick = bind(R.id.udt_nick);
        edtBirthday = bind(R.id.udt_birthday);
        rdbMale = bind(R.id.rdbMale);
        rdbFemale = bind(R.id.rdbFemale);
        edtAddress = bind(R.id.udt_address);
        //chPassword = (TextView) findViewById(R.id.udt_password);

        edtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean f) {
                if (!f) return;
                v.clearFocus();
                String[] s = edtBirthday.getText().toString().split("-");
                int year = 2017, month = 1, day = 1;
                if (s != null && s.length == 3) {
                    year = Integer.parseInt(s[0]);
                    month = Integer.parseInt(s[1]);
                    day = Integer.parseInt(s[2]);
                }
                mDialog = new DatePickerDialog(UserEdit.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker v, int _year, int _month, int _day) {
                        edtBirthday.setText(String.format("%d-%02d-%02d", _year, (_month + 1), _day));
                        mDialog.dismiss();
                    }
                }, year, month - 1, day);
                mDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nick = edtNick.getText().toString().trim();
                if (!nick.equals(""))
                    Common.mUser.nickname = nick.trim();
				/*if (isNewPass)
					Common.mUser.psd = NewPass;*/
                Common.mUser.mark = edtRemark.getText().toString();
                Common.mUser.address = edtAddress.getText().toString();
                Common.mUser.gender = rdbMale.isChecked();
                Common.mUser.birthday = edtBirthday.getText().toString();
                Common.updateUser();
                supportFinishAfterTransition();
            }
        });
		
		/*chPassword.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				shDialog();
			}
		});*/
        init(Common.mUser);
        try {
            Common.Login(this, new Common.LoginListener() {
                @Override
                public void onLogin(int code) {
                    if (code == 1) //登录成功
                        init(Common.mUser);
                }

                @Override
                public void onError(String error) {
                    //登录失败
                    ExceptionHandler.log("UserEdit.login(onError)", error);
                }
            });
        } catch (Exception e) {
            ExceptionHandler.log("UserEdit.login()", e);
        }
    }

    private void init(Users user) {
        if (!Common.isLogin())
            return;
        //username.setText(user.name);
        edtNick.setText(user.nickname);
        edtRemark.setText(toStrings(user.mark));
        if (Common.mUser.gender != null) {
            if (user.gender)
                rdbMale.setChecked(true);
            else
                rdbFemale.setChecked(true);
        }
        edtBirthday.setText(toStrings(user.birthday));
        edtAddress.setText(toStrings(user.address));
    }

    private String toStrings(String str) {
        if (str != null)
            return str;
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
