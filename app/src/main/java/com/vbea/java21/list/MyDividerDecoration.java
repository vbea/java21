package com.vbea.java21.list;

import android.view.View;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;

import com.vbea.java21.R;

public class MyDividerDecoration extends ItemDecoration
{
	private Drawable drawable;
	private final Rect mBounds = new Rect();

	public MyDividerDecoration(Context context)
	{
		drawable = ContextCompat.getDrawable(context, R.drawable.ic_divider);
		/*TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.divider});
		drawable = a.getDrawable(0);
		a.recycle();*/
	}
	
	@Override
	public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state)
	{
		canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
							parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - drawable.getIntrinsicHeight();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }
        canvas.restore();
	}
	
	@Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
    }
}
