package com.patrykkrawczyk.liveo.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class IceSettings extends AnimatedFragment {

    public IceSettings() {
        super(R.layout.fragment_ice_settings);
    }

    @OnTouch(R.id.confirmButton)
    public boolean onTouchConfirm(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }
}
