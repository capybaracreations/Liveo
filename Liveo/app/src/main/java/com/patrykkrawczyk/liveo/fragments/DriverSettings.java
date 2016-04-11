package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverSettings extends AnimatedFragment {

    @Bind(R.id.driverLayout)               TableLayout driverLayout;
    @Bind(R.id.firstNameEditText)          EditText firstNameEditText;
    @Bind(R.id.lastNameEditText)           EditText lastNameEditText;
    @Bind(R.id.registrationNumberEditText) EditText registrationNumberEditText;
    @Bind(R.id.maleSelection)              TextView maleSelection;
    @Bind(R.id.femaleSelection)            TextView femaleSelection;
    @Bind(R.id.teenSelection)              TextView teenSelection;
    @Bind(R.id.adultSelection)             TextView adultSelection;
    @Bind(R.id.seniorSelection)            TextView seniorSelection;
    @Bind(R.id.confirmTextView)            TextView confirmTextView;

    SharedPreferences sharedPreferences;
    int activeColor;
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
        activeColor = getResources().getColor(R.color.colorDefault);
        //SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
    }


    @OnClick(R.id.confirmTextView)
    public void onClickConfirm(View view) {
        switchPage(Page.MENU);
    }

    @OnClick(R.id.maleSelection)
    public void onClickMale(View view) {
        gender = Gender.MALE;
        maleSelection.setBackgroundColor(activeColor);
        femaleSelection.setBackgroundColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.femaleSelection)
    public void onClickFemale(View view) {
        gender = Gender.FEMALE;
        femaleSelection.setBackgroundColor(activeColor);
        maleSelection.setBackgroundColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.teenSelection)
    public void onClickTeen(View view) {
        ageGroup = AgeGroup.TEEN;
        teenSelection.setBackgroundColor(activeColor);
        adultSelection.setBackgroundColor(Color.TRANSPARENT);
        seniorSelection.setBackgroundColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.adultSelection)
    public void onClickAdult(View view) {
        ageGroup = AgeGroup.ADULT;
        adultSelection.setBackgroundColor(activeColor);
        teenSelection.setBackgroundColor(Color.TRANSPARENT);
        seniorSelection.setBackgroundColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.seniorSelection)
    public void onClickSenior(View view) {
        ageGroup = AgeGroup.SENIOR;
        seniorSelection.setBackgroundColor(activeColor);
        teenSelection.setBackgroundColor(Color.TRANSPARENT);
        adultSelection.setBackgroundColor(Color.TRANSPARENT);
    }


}
