package com.vbea.java21;

import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;

import com.vbea.java21.view.MyAlertDialog;
import com.vbea.java21.classes.Util;

public class TextReplace extends BaseActivity
{
	private EditText edit;
	private View baseView;
	private String oldValue="", newValue="";

	@Override
	protected void before()
	{
		setContentView(R.layout.txtreplace);
	}

	
	@Override
	protected void after()
	{
		enableBackButton();
		edit = bind(R.id.edt_replace);
		Button btnPlace = bind(R.id.btn_replace);
		baseView = bind(R.id.txtreplaceRelativeLayout);
		
		btnPlace.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (edit.getText().toString().trim().length() <= 0)
				{
					Util.toastShortMessage(getApplicationContext(), "请输入要替换的文本");
					return;
				}
				shDialog();
			}
		});
	}
	
	public void shDialog()
	{
		LayoutInflater inflat = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflat.inflate(R.layout.replacelog, null);
		final EditText edt1 = (EditText) view.findViewById(R.id.replaceResText);
		final EditText edt2 = (EditText) view.findViewById(R.id.replaceAimText);
		final CheckBox cased = (CheckBox) view.findViewById(R.id.chkIgnoeCase);
		edt1.setText(oldValue);
		edt2.setText(newValue);
		MyAlertDialog builder = new MyAlertDialog(TextReplace.this);
		builder.setTitle("替换选项");
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int d)
			{
				oldValue = edt1.getText().toString();
				newValue = edt2.getText().toString();
				replace(oldValue, newValue, cased.isChecked());
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int d)
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	public void replace(String res, String dim, boolean ignore)
	{
		if (res.length() == 0)
		{
			Snackbar.make(baseView, "操作未完成", Snackbar.LENGTH_SHORT).show();
			return;
		}
		final String old = edit.getText().toString();
		if (!ignore && !old.contains(res))
		{
			Snackbar.make(baseView, "未能找到源文本", Snackbar.LENGTH_SHORT).show();
			return;
		}
		String exp = res.replace("(", "\\(").replace(")", "\\)").replace("?", "\\?").replace("[", "\\[").replace("]", "\\]");
		if (ignore)
			exp = ("(?i)" + exp);
		edit.setText(old.replaceAll(exp, dim));
		Snackbar.make(baseView, "成功替换！", Snackbar.LENGTH_LONG)
		.setAction("撤销", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				edit.setText(old);
				Util.toastShortMessage(getApplicationContext(), "已撤销本次操作");
			}
		}).show();
	}
}
