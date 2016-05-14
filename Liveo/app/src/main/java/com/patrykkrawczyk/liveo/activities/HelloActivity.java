package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HelloActivity extends AppCompatActivity {

    private static final int WELCOME_DISPLAY_LENGTH = 1000;

    @Bind(R.id.helloText) TextView helloText;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        ButterKnife.bind(this);
        driver = Driver.getLocalDriver(this);

        helloText.setText("Hello\n" + driver.getFirstName() + " " + driver.getLastName());

        YoYo.with(Techniques.FadeIn)
                .withListener(setupShowAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(WELCOME_DISPLAY_LENGTH)
                .playOn(helloText);
    }

    private void afterTextShow() {
        YoYo.with(Techniques.FadeOut)
                .withListener(setupHideAnimator())
                .delay(WELCOME_DISPLAY_LENGTH/2)
                .interpolate(new AccelerateInterpolator())
                .duration(WELCOME_DISPLAY_LENGTH)
                .playOn(helloText);
    }

    private void afterTextHide() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private Animator.AnimatorListener setupShowAnimator() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterTextShow();
            }
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }
    private Animator.AnimatorListener setupHideAnimator() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterTextHide();
            }
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
