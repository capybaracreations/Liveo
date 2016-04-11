package com.patrykkrawczyk.liveo.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.net.Inet4Address;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

public class MenuFragment extends AnimatedFragment {

    //@Bind(R.id.menuLayout)       TableLayout menuLayout;
    @Bind(R.id.driverButton)     TableRow driverButton;
    @Bind(R.id.iceButton)        TableRow iceButton;
    @Bind(R.id.passengersButton) TableRow passengersButton;
    @Bind(R.id.triggerButton)    TableRow triggerButton;
    //@Bind(R.id.animator)       ViewRevealAnimator mViewAnimator;

    Page nextPage;

    public MenuFragment() { super(R.layout.fragment_menu); }


    @OnClick(R.id.rippleView)
    public void onRippleClick(View view) {
        Logger.d("tekst");
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver (View v, MotionEvent event) {
        nextPage = Page.DRIVER;
        rippleChangePage(event);
        return true;
    }

    @OnTouch(R.id.iceButton)
    public boolean onTouchIce (View v, MotionEvent event) {
        nextPage = Page.ICE;
        rippleChangePage(event);
        return true;
    }

    @OnTouch(R.id.passengersButton)
    public boolean onTouchPassengers (View v, MotionEvent event) {
        nextPage = Page.PASSENGERS;
        rippleChangePage(event);
        return true;
    }

    public void rippleChangePage(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = new Point((int) event.getRawX(), (int) event.getRawY());
            ripple.performRipple(point);
            switchPage(nextPage);
        }
    }

}
