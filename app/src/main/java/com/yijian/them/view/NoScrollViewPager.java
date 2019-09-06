package com.yijian.them.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yqjr.utils.view.NoPreloadViewPager;

public class NoScrollViewPager extends NoPreloadViewPager {
    private Boolean isScroll = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLRScroll(Boolean isScroll) {
        this.isScroll = isScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

}
