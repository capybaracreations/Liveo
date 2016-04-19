package com.patrykkrawczyk.liveo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_driver_settings);

        ButterKnife.bind(this);
        Logger.init("PATRYK");
        View view = findViewById(R.id.lastNameEditText);

        GuideManager.setGuideOnView(this, view, "Driver Profile", "Please fill out your Last Name");

    }

    @OnClick(R.id.maleSelection)
    public void click() {
        Logger.d("upsik");
    }
}
