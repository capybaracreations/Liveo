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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TableLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

public class MenuFragment extends AnimatedFragment{

    @Bind(R.id.menuLayout)       TableLayout menuLayout;
    @Bind(R.id.driverButton)     MaterialIconView driverButton;
    @Bind(R.id.iceButton)        MaterialIconView iceButton;
    @Bind(R.id.passengersButton) MaterialIconView passengersButton;
    @Bind(R.id.triggerButton)    MaterialIconView triggerButton;
    //@Bind(R.id.animator)         ViewRevealAnimator mViewAnimator;

    public MenuFragment() { super(R.layout.fragment_menu); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.driverButton)
    public void onClickDriver(View view) {
        //switchPage(Page.DRIVER);

       // mViewAnimator.showNext();
    }

    @OnClick(R.id.iceButton)
    public void onClickIce(View view) {
        switchPage(Page.ICE);
    }

    @OnClick(R.id.passengersButton)
    public void onClickPassengers(View view) {
        switchPage(Page.PASSENGERS);
    }

    @OnClick(R.id.triggerButton)
    public void onClickTrigger(View view) {

    }

}
