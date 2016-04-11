package com.patrykkrawczyk.liveo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.fragments.DriverSettings;
import com.patrykkrawczyk.liveo.fragments.IceSettings;
import com.patrykkrawczyk.liveo.fragments.MenuFragment;
import com.patrykkrawczyk.liveo.fragments.PassengerSelection;

import java.lang.reflect.Field;

public class MyViewPager extends ViewPager {

    MyScroller myScroller;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            myScroller = new MyScroller(getContext());
            scroller.set(this, myScroller);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScrollSpeed(int destination) {
        int pageCount = Math.abs(getCurrentItem() - destination);
        myScroller.speed = myScroller.BASE_SPEED * pageCount;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    public class MyScroller extends Scroller {
        static final int BASE_SPEED = 350;
        int speed = 500;

        public MyScroller(Context context) {
            super(context, new AccelerateDecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, speed);
        }
    }
}