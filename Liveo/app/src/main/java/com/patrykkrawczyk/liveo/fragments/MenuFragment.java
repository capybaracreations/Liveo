package com.patrykkrawczyk.liveo.fragments;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.patrykkrawczyk.liveo.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.OnTouch;

public class MenuFragment extends AnimatedFragment {

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.DRIVER);
        }
        return true;
    }

    @OnTouch(R.id.iceButton)
    public boolean onTouchIce (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.ICE);
        }
        return true;
    }

    @OnTouch(R.id.passengersButton)
    public boolean onTouchPassengers (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setIconColor(view, Color.RED);
            rippleChangePage(event, Page.PASSENGERS);
        }
        return true;
    }

    @Override
    protected void setIconColor(View view, int color) {
        LinearLayout viewGroup = (LinearLayout)((ViewGroup) view).getChildAt(0);
        MaterialIconView icon = (MaterialIconView) ((TableRow)(viewGroup.getChildAt(0))).getChildAt(0);
        TextView textView = (TextView) ((TableRow)(viewGroup.getChildAt(1))).getChildAt(0);
        icon.setColor(color);
        textView.setTextColor(color);
    }
}
