package com.patrykkrawczyk.liveo.fragments;

import android.view.MotionEvent;
import android.view.View;
import com.patrykkrawczyk.liveo.R;
import butterknife.OnTouch;

public class MenuFragment extends AnimatedFragment {

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver (View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rippleChangePage(event, Page.DRIVER);
        }
        return true;
    }

    @OnTouch(R.id.iceButton)
    public boolean onTouchIce (View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rippleChangePage(event, Page.ICE);
        }
        return true;
    }

    @OnTouch(R.id.passengersButton)
    public boolean onTouchPassengers (View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            rippleChangePage(event, Page.PASSENGERS);
        }
        return true;
    }

}
