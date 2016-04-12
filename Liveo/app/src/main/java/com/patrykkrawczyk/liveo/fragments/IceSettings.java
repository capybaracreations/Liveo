package com.patrykkrawczyk.liveo.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class IceSettings extends AnimatedFragment {

    public IceSettings() {
        super(R.layout.fragment_ice_settings);
    }

    @OnTouch(R.id.contact0Button)
    public boolean onContact0Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact1Button)
    public boolean onContact1Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact2Button)
    public boolean onContact2Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact3Button)
    public boolean onContact3Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact4Button)
    public boolean onContact4Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }


}
