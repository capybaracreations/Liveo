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

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SplashScreenActivity extends AppCompatActivity
{
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Bind(R.id.splashText)    ShimmerTextView splashText;
    //@Bind(R.id.welcomeText)   TextView welcomeText;

    private Shimmer shimmer;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        shimmer = new Shimmer().setDuration(SPLASH_DISPLAY_LENGTH);

        YoYo.with(Techniques.FadeIn)
                .withListener(setupShowAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);

    //    GuideManager.resetGuide(this); // TODO DELETE THIS
        GuideManager.loadGuideState(this);
    }

    private void afterLogoShow() {
        shimmer.start(splashText);

        driver = Driver.getLocalDriver(this);
        hideLogo();
    }

    private void hideLogo() {
        YoYo.with(Techniques.FadeOut)
                .withListener(setupHideAnimator())
                .delay(SPLASH_DISPLAY_LENGTH/2)
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);
    }

    private void afterLogoHide() {
        Intent intent;
        if (driver == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, HelloActivity.class);
        }
        startActivity(intent);
    }

    private AnimatorListener setupShowAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterLogoShow();
            }
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }
    private AnimatorListener setupHideAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterLogoHide();
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
