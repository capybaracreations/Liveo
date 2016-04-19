package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.SwitchPageEvent;
import com.patrykkrawczyk.liveo.activities.MainActivity;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class DriverSettings extends AnimatedFragment {


    @Bind(R.id.firstNameEditText)          EditText firstNameEditText;
    @Bind(R.id.maleSelection)              MaterialIconView maleSelection;
    @Bind(R.id.femaleSelection)            MaterialIconView femaleSelection;
    @Bind(R.id.confirmButton)              MaterialIconView confirmButton;
    @Bind(R.id.teenSelection)              TextView teenSelection;
    @Bind(R.id.adultSelection)             TextView adultSelection;
    @Bind(R.id.seniorSelection)            TextView seniorSelection;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
    }

    @OnTouch(R.id.confirmButton)
    public boolean onTouchConfirm(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performRippleNoSwitch(event);
            performCheck();
        }
        return true;
    }

    private void performCheck() {
        boolean correct = true;

        if (!correct) {
            confirmButton.setColor(Color.RED);
        } else {
            confirmButton.setColor(Color.GREEN);
            saveDriverData();
        }
    }

    private void saveDriverData() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("showPassengers", true);
        startActivity(intent);
        //EventBus.getDefault().post(new SwitchPageEvent(Page.MENU));
    }

    @OnTouch(R.id.maleSelection)
    public boolean onTouchMale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gender = Gender.MALE;
            femaleSelection.setColor(Color.WHITE);
            maleSelection.setColor(Color.RED);
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.femaleSelection)
    public boolean onTouchFemale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gender = Gender.FEMALE;
            femaleSelection.setColor(Color.RED);
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
            teenSelection.setTextColor(Color.RED);
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
            adultSelection.setTextColor(Color.RED);
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
            seniorSelection.setTextColor(Color.RED);
            confirmButton.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }



}
