package com.vbea.java21.widget;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.view.View;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.support.v7.widget.RecyclerView;

public class SlidingView extends HorizontalScrollView
{
	private SlidingViewListener mListener;
	//private static final float leftMenu = 0.4f;//菜单占屏幕宽度比
	//private static final float rightMenu = 0.3f;
    public final int mScreenWidth;
    public int leftMenuWidth, rightMenuWidth;
    private boolean once = true;
	private boolean isOpen = false;
	private long downTime = 0;
	//private View content;

    public SlidingView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mScreenWidth = getScreenWidth(context);
		leftMenuWidth = 0;//(int) (mScreenWidth * leftMenu);
		rightMenuWidth = 0;//(int) (mScreenWidth * rightMenu);
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setHorizontalScrollBarEnabled(false);
    }
	
	public void setMenuListener(SlidingViewListener lis)
	{
		mListener = lis;
	}

	public void closeMenu()
	{
		if (isOpen)
		{
			this.smoothScrollTo(leftMenuWidth, 0);
			isOpen = false;
			mListener.onCloseMenu();
		}
	}

	public boolean isOpen()
	{
		return isOpen;
	}
	
	public static int getScreenWidth(Context context)
	{
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (once)
		{
			final LinearLayout wrapp = (LinearLayout) getChildAt(0);
			//content = wrapp.getChildAt(1);
			wrapp.getChildAt(1).getLayoutParams().width = mScreenWidth;
			wrapp.post(new Runnable()
			{
				@Override
				public void run()
				{
					leftMenuWidth = wrapp.getChildAt(0).getMeasuredWidth();
					rightMenuWidth = wrapp.getChildAt(2).getMeasuredWidth();
					scrollTo(leftMenuWidth, 0);
				}
			});
			once = false;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev)
	{
		/*获取正在滑动的menu
        if (getScrollingMenu() != null && getScrollingMenu() != this) {
            return false;
        }*/
		if (mListener.onStart(this))
			return true;
        switch (ev.getAction())
		{
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
				//closeOpenMenu();关闭上一次打开的item
				//setScrollingMenu(this);设置本item为正在滑动
                break;
            case MotionEvent.ACTION_UP:
				//setScrollingMenu(null);
                int scrollX = getScrollX();
                if (System.currentTimeMillis() - downTime <= 100 && scrollX == leftMenuWidth)
				{
					if (isOpen)
					{
						isOpen = false;
						this.smoothScrollTo(leftMenuWidth, 0);
						mListener.onCloseMenu();
					}
					//else
						//mListener.onClick();
                    return false;
                }
                if (Math.abs(scrollX) < leftMenuWidth / 2)
				{
					isOpen = true;
                    this.smoothScrollTo(0, 0);
					mListener.onOpenMenu();//记录此打开的view，方便下次关闭
                }
				else if (Math.abs(scrollX) > (leftMenuWidth + (rightMenuWidth / 2)))
				{
					isOpen = true;
                    this.smoothScrollTo(leftMenuWidth + rightMenuWidth, 0);
					mListener.onOpenMenu();
                }
				else
				{
					isOpen = false;
					this.smoothScrollTo(leftMenuWidth, 0);
					mListener.onCloseMenu();
				}
                return false;
        }
        return super.onTouchEvent(ev);
    }
	
	public interface SlidingViewListener
	{
		//void onClick();
		void onOpenMenu();
		void onCloseMenu();
		boolean onStart(SlidingView v);
	}
}
