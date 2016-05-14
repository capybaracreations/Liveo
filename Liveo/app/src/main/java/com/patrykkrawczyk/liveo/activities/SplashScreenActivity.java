package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.INetwork;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.wang.avi.AVLoadingIndicatorView;

import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SplashScreenActivity extends AppCompatActivity implements Callback<ResponseBody> {
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Bind(R.id.usernameEditText)    MaterialEditText usernameEditText;
    @Bind(R.id.pinEditText)         MaterialEditText pinEditText;
    @Bind(R.id.submitButton)        MaterialIconView submitButton;
    @Bind(R.id.splashText)          ShimmerTextView splashText;
    @Bind(R.id.helloText)           TextView helloText;
    @Bind(R.id.loginForm)           LinearLayout loginForm;
    @Bind(R.id.loadingView)         AVLoadingIndicatorView loadingView;

    private Shimmer shimmer;
    private Driver driver;
    protected final int RIPPLE_SPEED = 400;
    protected MaterialRippleLayout ripple;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.LIVEO_API_URL))
                .build();

        shimmer = new Shimmer().setDuration(SPLASH_DISPLAY_LENGTH);

        YoYo.with(Techniques.FadeIn)
                .withListener(setupShowAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);

    //    GuideManager.resetGuide(this); // TODO DELETE THIS
        GuideManager.loadGuideState(this);


        usernameEditText.setEnabled(false);
        pinEditText.setEnabled(false);
        submitButton.setEnabled(false);

        ripple = MaterialRippleLayout.on(findViewById(R.id.rippleView))
                .rippleOverlay(true)
                .rippleColor(getResources().getColor(R.color.colorRipple))
                .rippleAlpha((float)0.20)
                .ripplePersistent(false)
                .rippleDuration(RIPPLE_SPEED)
                .rippleDelayClick(false)
                .rippleFadeDuration(100)
                .create();
        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
        ripple.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { return true; }
        });
    }

    private void afterLogoShow() {
        shimmer.start(splashText);

        driver = Driver.getLocalDriver(this);
        if (driver != null) {
            helloText.setText("Hello\n" + driver.getFirstName() + " " + driver.getLastName());
            YoYo.with(Techniques.FadeIn)
                    .withListener(setupHelloAnimator())
                    .interpolate(new AccelerateInterpolator())
                    .duration(SPLASH_DISPLAY_LENGTH)
                    .playOn(helloText);
        } else {
            usernameEditText.setEnabled(true);
            pinEditText.setEnabled(true);
            submitButton.setEnabled(true);

            YoYo.with(Techniques.FadeIn)
                    .interpolate(new AccelerateInterpolator())
                    .duration(SPLASH_DISPLAY_LENGTH)
                    .playOn(loginForm);
        }

    }

    @OnTouch(R.id.submitButton)
    public boolean onSubmitButton(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && submitButton.isEnabled()) {
            Point point = new Point((int)event.getRawX(), (int)event.getRawY());
            ripple.performRipple(point);
            validate();
        }
        return true;
    }

    private void hideLogo() {
        YoYo.with(Techniques.FadeOut)
                .withListener(setupHideAnimator())
                .delay(SPLASH_DISPLAY_LENGTH/2)
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);

        if (helloText.getAlpha() > 0)
            YoYo.with(Techniques.FadeOut)
                    .delay(SPLASH_DISPLAY_LENGTH/2)
                    .interpolate(new AccelerateInterpolator())
                    .duration(SPLASH_DISPLAY_LENGTH)
                    .playOn(helloText);
    }

    private void afterLogoHide() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void afterFormHide() {
        helloText.setText("Hello\n" + driver.getFirstName() + " " + driver.getLastName());

        YoYo.with(Techniques.FadeIn)
                .withListener(setupHelloAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(helloText);
    }

    private void validate() {
        List<View> empties = new ArrayList<>();

        if (usernameEditText.getText().toString().isEmpty()) {
            empties.add(usernameEditText);
        }
        if (pinEditText.getText().toString().isEmpty() || pinEditText.getText().toString().length() < 4) {
            empties.add(pinEditText);
        }

        if (empties.size() > 0) {
            for (View view : empties) {
                YoYo.with(Techniques.Shake)
                        .interpolate(new AccelerateInterpolator())
                        .duration(1000)
                        .playOn(view);
            }
        } else {
            submitButton.setColor(getResources().getColor(R.color.colorAccent));
            usernameEditText.setEnabled(false);
            pinEditText.setEnabled(false);
            submitButton.setEnabled(false);

            INetwork login = retrofit.create(INetwork.class);
            Call<ResponseBody> call = login.login(usernameEditText.getText().toString(), pinEditText.getText().toString());

            call.enqueue(this);
            loadingView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 400) {
            onFailure(null, null);
            submitButton.setColor(getResources().getColor(R.color.RED));
        } else {
            try {
                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                driver = mapper.readValue(body, Driver.class);

                if (driver != null) {
                    if (Driver.setCurrentDriver(this, driver)) {
                            YoYo.with(Techniques.FadeOut)
                                    .withListener(setupFormAnimator())
                                    .delay(SPLASH_DISPLAY_LENGTH/2)
                                    .interpolate(new AccelerateInterpolator())
                                    .duration(SPLASH_DISPLAY_LENGTH)
                                    .playOn(loginForm);

                    } else {
                        onFailure(null, null);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                onFailure(null, null);
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        submitButton.setColor(getResources().getColor(R.color.WHITE));
        usernameEditText.setEnabled(true);
        pinEditText.setEnabled(true);
        submitButton.setEnabled(true);

        YoYo.with(Techniques.Shake)
                .interpolate(new AccelerateInterpolator())
                .duration(1000)
                .playOn(submitButton);

        loadingView.setVisibility(View.INVISIBLE);
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
    private AnimatorListener setupHelloAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hideLogo();
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
    private AnimatorListener setupFormAnimator() {
        return new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterFormHide();
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
