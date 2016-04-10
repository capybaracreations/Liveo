package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassengerSelection extends AnimatedFragment {

    @Bind(R.id.passenger0TextView) TextView passenger0TextView;
    @Bind(R.id.passenger1TextView) TextView passenger1TextView;
    @Bind(R.id.passenger2TextView) TextView passenger2TextView;
    @Bind(R.id.passenger3TextView) TextView passenger3TextView;
    @Bind(R.id.passenger4TextView) TextView passenger4TextView;
    @Bind(R.id.passenger5TextView) TextView passenger5TextView;

    public PassengerSelection() {
        super(R.layout.fragment_passenger_selection);
    }


    @OnClick(R.id.passenger0TextView)
    public void passenger0Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }

    @OnClick(R.id.passenger1TextView)
    public void passenger1Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }

    @OnClick(R.id.passenger2TextView)
    public void passenger2Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }

    @OnClick(R.id.passenger3TextView)
    public void passenger3Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }

    @OnClick(R.id.passenger4TextView)
    public void passenger4Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }

    @OnClick(R.id.passenger5TextView)
    public void passenger5Click(TextView textView) {
        savePassengerCount(textView.getText().toString());
    }


    private void savePassengerCount(String count) {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.PASSENGERS_COUNT), count);
        editor.apply();
        switchPage(Page.MENU);
    }



}