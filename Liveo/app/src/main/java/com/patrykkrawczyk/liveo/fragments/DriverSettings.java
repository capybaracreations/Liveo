package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.SwitchPageEvent;
import com.patrykkrawczyk.liveo.activities.MainActivity;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnTouch;

public class DriverSettings extends AnimatedFragment {


    @Bind(R.id.firstNameEditText)          EditText firstNameEditText;
    @Bind(R.id.maleSelection)              MaterialIconView maleSelection;
    @Bind(R.id.femaleSelection)            MaterialIconView femaleSelection;
    @Bind(R.id.confirmButton)              MaterialIconView confirmButton;
    @Bind(R.id.teenSelection)              TextView teenSelection;
    @Bind(R.id.adultSelection)             TextView adultSelection;
    @Bind(R.id.seniorSelection)            TextView seniorSelection;

    private EventBus eventBus;
    SharedPreferences sharedPreferences;
    enum Gender{
        MALE, FEMALE;
    };
    Gender gender;
    enum AgeGroup{
        TEEN, ADULT, SENIOR;
    };
    AgeGroup ageGroup;

    public DriverSettings() {
        super(R.layout.fragment_driver_settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
    }

    @OnTouch(R.id.confirmButton)
    public boolean onTouchConfirm(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            if (GuideManager.getStage() == 0) GuideManager.incrementStage();
            rippleChangePage(event, Page.MENU);
            //performRippleNoSwitch(event);
            //performCheck();
        }
        return true;
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | DRIVER");
        touchEnabled = true;
    }

    private void performCheck() {
        boolean correct = true;

        if (!correct) {
            confirmButton.setColor(Color.RED);
        } else {
            if (GuideManager.getStage() == 0) GuideManager.incrementStage();
            confirmButton.setColor(getResources().getColor(R.color.colorAccent));
            saveDriverData();
        }
    }

    private void saveDriverData() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("showPassengers", true);
        startActivity(intent);
    }

    @OnTouch(R.id.maleSelection)
    public boolean onTouchMale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gender = Gender.MALE;
            femaleSelection.setColor(Color.WHITE);
            maleSelection.setColor(getResources().getColor(R.color.colorAccent));
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.femaleSelection)
    public boolean onTouchFemale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gender = Gender.FEMALE;
            femaleSelection.setColor(getResources().getColor(R.color.colorAccent));
            maleSelection.setColor(Color.WHITE);
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.teenSelection)
    public boolean onTouchTeen(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ageGroup = AgeGroup.TEEN;
            teenSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            adultSelection.setTextColor(Color.WHITE);
            seniorSelection.setTextColor(Color.WHITE);
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.adultSelection)
    public boolean onTouchAdult(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ageGroup = AgeGroup.ADULT;
            teenSelection.setTextColor(Color.WHITE);
            adultSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            seniorSelection.setTextColor(Color.WHITE);
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.seniorSelection)
    public boolean onTouchSenior(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ageGroup = AgeGroup.SENIOR;
            teenSelection.setTextColor(Color.WHITE);
            adultSelection.setTextColor(Color.WHITE);
            seniorSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }



}
