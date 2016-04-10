package com.patrykkrawczyk.liveo.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class IceSettings extends AnimatedFragment {

    public IceSettings() {
        super(R.layout.fragment_ice_settings);
    }


    @OnClick(R.id.confirmTextView)
    public void onClickConfirm(View view) {
        switchPage(Page.MENU);
    }
}
