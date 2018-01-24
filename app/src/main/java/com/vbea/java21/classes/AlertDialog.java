package com.vbea.java21.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import com.vbea.java21.MyThemes;

public class AlertDialog extends AlertDialog.Builder
{
	Context context;
	boolean isButton;
	public AlertDialog(Context _context)
	{
		super(_context);
		context = _context;
		isButton = false;
	}

	@Override
	public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setPositiveButton(textId, listener);
	}

	@Override
	public AlertDialog.Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setPositiveButton(text, listener);
	}

	@Override
	public AlertDialog.Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setNeutralButton(textId, listener);
	}

	@Override
	public AlertDialog.Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setNeutralButton(text, listener);
	}

	@Override
	public AlertDialog.Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setNegativeButton(textId, listener);
	}

	@Override
	public AlertDialog.Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener)
	{
		isButton = true;
		return super.setNegativeButton(text, listener);
	}

	@Override
	public AlertDialog create()
	{
		if (isButton)
		{
			final AlertDialog _this = super.create();
			_this.setOnShowListener(new DialogInterface.OnShowListener()
			{
				public void onShow(DialogInterface dialog)
				{
					int color = MyThemes.getColorAccent(context);
					Button negative = _this.getButton(DialogInterface.BUTTON_NEGATIVE);
					if (negative != null)
						negative.setTextColor(color);
					Button neutral = _this.getButton(DialogInterface.BUTTON_NEUTRAL);
					if (neutral != null)
						neutral.setTextColor(color);
					Button positive = _this.getButton(DialogInterface.BUTTON_POSITIVE);
					if (positive != null)
						positive.setTextColor(color);
				}
			});
			return _this;
		}
		else
			return super.create();
	}
}
