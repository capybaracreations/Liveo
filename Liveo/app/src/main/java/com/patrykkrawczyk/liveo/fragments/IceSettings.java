package com.patrykkrawczyk.liveo.fragments;

import android.view.MotionEvent;
import android.view.View;

import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;

import butterknife.OnTouch;

public class IceSettings extends AnimatedFragment {

    public IceSettings() {
        super(R.layout.fragment_ice_settings);
    }

    @OnTouch(R.id.contact0Button)
    public boolean onContact0Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (GuideManager.tutorialStage == 1) GuideManager.tutorialStage++;
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact1Button)
    public boolean onContact1Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (GuideManager.tutorialStage == 1) GuideManager.tutorialStage++;
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact2Button)
    public boolean onContact2Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (GuideManager.tutorialStage == 1) GuideManager.tutorialStage++;
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact3Button)
    public boolean onContact3Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (GuideManager.tutorialStage == 1) GuideManager.tutorialStage++;
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }

    @OnTouch(R.id.contact4Button)
    public boolean onContact4Touch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (GuideManager.tutorialStage == 1) GuideManager.tutorialStage++;
            setIconColor(view);
            rippleChangePage(event, Page.MENU);
        }
        return true;
    }


}
