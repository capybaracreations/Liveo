package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PassengerSelection extends AnimatedFragment {


    public PassengerSelection() { super(R.layout.fragment_passenger_selection); }

    @OnTouch(R.id.passenger0Button)
    public boolean onPassenger0Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger1Button)
    public boolean onPassenger1Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }

    @OnTouch(R.id.passenger2Button)
    public boolean onPassenger2Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger3Button)
    public boolean onPassenger3Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }

    @OnTouch(R.id.passenger4Button)
    public boolean onPassenger4Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger5Button)
    public boolean onPassenger5Button(View view, MotionEvent event) {
        setIconColor(view, Color.RED);
        savePassengerCount(view, event);
        return true;
    }

    private void savePassengerCount(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            String count                    = view.getTag().toString();
            SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.PASSENGERS_COUNT), count);
            editor.apply();
            rippleChangePage(event, Page.MENU);
        }
    }

}