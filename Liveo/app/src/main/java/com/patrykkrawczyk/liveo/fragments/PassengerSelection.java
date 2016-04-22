package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnTouch;

public class PassengerSelection extends AnimatedFragment {


    public PassengerSelection() { super(R.layout.fragment_passenger_selection); }


    private EventBus eventBus;


    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
    }

    @OnTouch(R.id.passenger0Button)
    public boolean onPassenger0Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger1Button)
    public boolean onPassenger1Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }

    @OnTouch(R.id.passenger2Button)
    public boolean onPassenger2Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger3Button)
    public boolean onPassenger3Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }

    @OnTouch(R.id.passenger4Button)
    public boolean onPassenger4Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }
    @OnTouch(R.id.passenger5Button)
    public boolean onPassenger5Button(View view, MotionEvent event) {
        setIconColor(view);
        savePassengerCount(view, event);
        return true;
    }


    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | DRIVER");
        touchEnabled = true;
    }

    private void savePassengerCount(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 2) GuideManager.incrementStage();
            String count                    = view.getTag().toString();
            SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getString(R.string.PASSENGERS_COUNT), count);
            editor.apply();
            rippleChangePage(event, Page.MENU);
        }
    }



}