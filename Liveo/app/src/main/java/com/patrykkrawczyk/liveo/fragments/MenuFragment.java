package com.patrykkrawczyk.liveo.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends AnimatedFragment implements ViewPager.OnPageChangeListener{

    @Bind(R.id.menuLayout)         GridLayout menuLayout;
    @Bind(R.id.driverTextView)     TextView driverTextView;
    @Bind(R.id.iceTextView)        TextView iceTextView;
    @Bind(R.id.passengersTextView) TextView passengersTextView;
    @Bind(R.id.triggerTextView)    TextView triggerTextView;


    public MenuFragment() { super(R.layout.fragment_menu); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewPager.addOnPageChangeListener(this);
    }


    @OnClick(R.id.driverTextView)
    public void onClickDriver(View view) {
        switchPage(Page.DRIVER);
    }

    @OnClick(R.id.iceTextView)
    public void onClickIce(View view) {
        switchPage(Page.ICE);
    }

    @OnClick(R.id.passengersTextView)
    public void onClickPassengers(View view) {
        switchPage(Page.PASSENGERS);
    }

    @OnClick(R.id.triggerTextView)
    public void onClickTrigger(View view) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mainViewPager.getCurrentItem() == 0 && state == ViewPager.SCROLL_STATE_IDLE) {
            Logger.d("MENU STOP");
            //adapter.cleanFragments();
        }
    }
}
