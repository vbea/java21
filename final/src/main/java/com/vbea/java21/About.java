package com.vbea.java21;

import android.view.View;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.LinearLayout;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;

public class About extends BaseActivity
{
	@Override
	public void before()
	{
		setContentView(R.layout.about);
	}

	@Override
	public void after()
	{
		enableBackButton();
		bind(R.id.about_new).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MyAlertDialog dia = new MyAlertDialog(About.this);
				dia.setTitle("新版特性");
				dia.setMessage(R.string.abt_ver);
				dia.setPositiveButton("知道了", null);
				dia.setNeutralButton("复制到剪贴板", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int p)
					{
						Util.addClipboard(About.this, getString(R.string.abt_ver));
						toastShortMessage("已复制到剪贴板");
					}
				});
				dia.show();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
}
