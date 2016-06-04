package com.patrykkrawczyk.liveo.activities;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.LiveoApplication;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class InformationActivity extends AppCompatActivity {

    @Bind(R.id.informationFirstName) TextView informationFirstName;
    @Bind(R.id.informationLastName)  TextView informationLastName;
    @Bind(R.id.informationAgeGroup)  TextView informationAgeGroup;
    @Bind(R.id.informationGender)    TextView informationGender;
    @Bind(R.id.informationRegNumber) TextView informationRegNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ButterKnife.bind(this);

        loadTextViews();
    }

    private void loadTextViews() {
        Driver driver = Driver.getLocalDriver(this);

        informationFirstName.setText(driver.getFirstName());
        informationLastName.setText(driver.getLastName());
        informationAgeGroup.setText(driver.getAgeGroup());
        informationGender.setText(driver.getGender());
        informationRegNumber.setText(driver.getRegisterNumber());
    }

    @OnTouch(R.id.informationCloseButton)
    public boolean onInformationCloseButton(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final Activity activtiy = this;
            new SweetAlertDialog(activtiy, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("")
                    .setCancelText("CLOSE")
                    .setConfirmText("STAY")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(activtiy, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animateViewTouch(view);
        }
        return true;
    }

    private void animateViewTouch(View view) {
        YoYo.with(Techniques.Pulse)
                .interpolate(new AccelerateInterpolator())
                .duration(500)
                .playOn(view);
    }
}
