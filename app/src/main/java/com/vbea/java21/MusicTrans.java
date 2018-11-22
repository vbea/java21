package com.vbea.java21;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.text.TextWatcher;
import android.text.Editable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

public class MusicTrans extends BaseActivity
{
	private EditText txtMusic, txtCode;
	private Button btnTrans;
	private boolean COMPLETE = false;

	@Override
	protected void before()
	{
		setContentView(R.layout.musictrans);
	}

	@Override
	public void after()
	{
		enableBackButton();
		btnTrans = (Button) findViewById(R.id.btn_musicTrans);
		txtMusic = (EditText) findViewById(R.id.txt_musicNew);
		txtCode = (EditText) findViewById(R.id.txt_musicOld);
		
		btnTrans.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (COMPLETE)
				{
					Intent intent = new Intent(MusicTrans.this, MusicTest.class);
					intent.putExtra("music", txtMusic.getText().toString().trim());
					Common.startActivityOptions(MusicTrans.this, intent);
				}
				else
				{
					if (txtCode.getText().toString().trim().equals(""))
						Util.toastShortMessage(getApplicationContext(), "请输入音节代码");
					else
					{
						try
						{
							trans(txtCode.getText().toString());
						}
						catch (Exception e)
						{
							Util.toastShortMessage(getApplicationContext(), "转换失败");
						}
					}
				}
			}
		});
		
		txtCode.addTextChangedListener(new TextWatcher()
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
				COMPLETE = false;
				getButtonText();
			}
		});
	}
	
	private void getButtonText()
	{
		if (COMPLETE)
			btnTrans.setText("去测试");
		else
			btnTrans.setText("转换");
	}
	
	private void trans(String code)
	{
		code = code.replaceAll("\\s*|\t|\r|\n", "");
		StringBuilder strb = new StringBuilder();
		boolean extend = false, bracket = false, kuot = false;
		String extens = "", brack = "", kuos = "";
		for (int i = 0; i < code.length(); i++)
		{
			String s = code.substring(i, i+1);
			switch (s)
			{
				case "[":
					extend = true;
					break;
				case "]":
					extend = false;
					if (bracket)
						brack += extens + "-";
					else if (kuot)
						kuos += extens + "_";
					else
						strb.append(extens+",");
					extens = "";
					break;
				case "(":
					bracket = true;
					break;
				case ")":
					bracket = false;
					if (kuot)
						kuos += brack.substring(0, brack.length()-1)+"_";
					else
						strb.append(brack.substring(0, brack.length()-1)+",");
					brack = "";
					break;
				case "<":
					kuot = true;
					break;
				case ">":
					kuot = false;
					strb.append(kuos.substring(0, kuos.length()-1)+",");
					kuos = "";
					break;
				default:
					if (extend)
						extens += s;
					else if (bracket)
						brack += s + "-";
					else if (kuot)
						kuos += s + "_";
					else
						strb.append(s+",");
					break;
			}
		}
		txtMusic.setText(strb.toString().substring(0, strb.length()-1));
		COMPLETE = true;
		getButtonText();
	}
}
