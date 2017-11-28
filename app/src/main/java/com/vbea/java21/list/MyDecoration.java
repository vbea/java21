package com.vbea.java21.list;

import android.view.View;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.*;

import com.vbea.java21.R;

public class MyDecoration extends ItemDecoration
{
	private Drawable drawable;

	public MyDecoration(Context context)
	{
		drawable = context.getResources().getDrawable(R.drawable.ic_divider);
	}
	
	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
	{
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();
		int count = parent.getChildCount();
		for (int i=0; i < count; i++)
		{
			View child = parent.getChildAt(i);
			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + drawable.getIntrinsicHeight();
			drawable.setBounds(left, top, right, bottom);
			drawable.draw(c);
		}
		super.onDraw(c, parent, state);
	}
}
