package com.vbea.java21.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
 
public class MyWebView extends WebView implements NestedScrollingChild {
 
    public static final String TAG = MyWebView.class.getSimpleName();
 
    private int mLastMotionY;
 
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
 
    private int mNestedYOffset;
    private boolean canNestedScroll = true;
 
    private NestedScrollingChildHelper mChildHelper;
 
    public MyWebView(Context context) {
        super(context);
        init();
    }
 
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
 
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
 
    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canNestedScroll) {
            return false;
        }
        boolean result = false;
 
        MotionEvent trackedEvent = MotionEvent.obtain(event);
 
        final int action = MotionEventCompat.getActionMasked(event);
 
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
 
        int y = (int) event.getY();
 
        event.offsetLocation(0, mNestedYOffset);
 
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                result = super.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastMotionY - y;
 
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
 
                int oldY = getScrollY();
                mLastMotionY = y - mScrollOffset[1];
                if (deltaY < 0) {
                    int newScrollY = Math.max(0, oldY + deltaY);
                    deltaY -= newScrollY - oldY;
                    if (dispatchNestedScroll(0, newScrollY - deltaY, 0, deltaY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        trackedEvent.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    }
                }
 
                trackedEvent.recycle();
                result = super.onTouchEvent(trackedEvent);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                result = super.onTouchEvent(event);
                break;
        }
        return result;
    }

    public void setCanNestedScroll(boolean enabled) {
        canNestedScroll = enabled;
    }

    // NestedScrollingChild
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }
 
    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }
 
    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }
 
    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }
 
    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }
 
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled())
            return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        else
            return false;
    }
 
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (isNestedScrollingEnabled())
            return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        else
            return false;
    }
 
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        if (isNestedScrollingEnabled())
            return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
        else
            return false;
    }
 
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        if (isNestedScrollingEnabled())
            return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
        else
            return false;
    }
}