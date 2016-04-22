package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnTouch;

public class IceSettings extends AnimatedFragment {

    private EventBus eventBus;

    public IceSettings() {
        super(R.layout.fragment_ice_settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | DRIVER");
        touchEnabled = true;
    }

    @OnTouch(R.id.contact0Button)
    public boolean onContact0Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact1Button)
    public boolean onContact1Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact2Button)
    public boolean onContact2Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact3Button)
    public boolean onContact3Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact4Button)
    public boolean onContact4Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }


}
