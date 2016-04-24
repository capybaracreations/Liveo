package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SplashScreenActivity extends AppCompatActivity
{
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Bind(R.id.splashText)    ShimmerTextView splashText;
    @Bind(R.id.welcomeText)   TextView welcomeText;

    private Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        shimmer = new Shimmer().setDuration(SPLASH_DISPLAY_LENGTH);

        YoYo.with(Techniques.FadeIn)
                .withListener(setupSplashAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);


        //GuideManager.resetGuide(this); // TODO DELETE THIS
        GuideManager.loadGuideState(this);

    }


    public void getCurrentDriver() {
        showWelcomeText(Driver.getCurrentDriver(this));
    }


    private void showWelcomeText(Driver driver) {
        if (welcomeText.getAlpha() == 0) {
            String text;

            if (driver == null) text = "Welcome";
            else text = "Hello\n" + driver.getFirstName() + " " + driver.getLastName();

            welcomeText.setText(text);
            YoYo.with(Techniques.FadeIn)
                    .withListener(setupWelcomeAnimator())
                    .interpolate(new AccelerateInterpolator())
                    .duration(SPLASH_DISPLAY_LENGTH)
                    .playOn(welcomeText);
        }
    }

    private void goToMenu() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private AnimatorListener setupSplashAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shimmer.start(splashText);
                getCurrentDriver();
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
    private AnimatorListener setupWelcomeAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                YoYo.with(Techniques.FadeOut)
                        .delay(SPLASH_DISPLAY_LENGTH/2)
                        .interpolate(new AccelerateInterpolator())
                        .duration(SPLASH_DISPLAY_LENGTH)
                        .playOn(welcomeText);
                YoYo.with(Techniques.FadeOut)
                        .withListener(setupHideAnimator())
                        .delay(SPLASH_DISPLAY_LENGTH/2)
                        .interpolate(new AccelerateInterpolator())
                        .duration(SPLASH_DISPLAY_LENGTH)
                        .playOn(splashText);
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
    private AnimatorListener setupHideAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                goToMenu();
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


}
