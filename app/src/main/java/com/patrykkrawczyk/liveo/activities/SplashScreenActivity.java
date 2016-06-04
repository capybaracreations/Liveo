package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.INetwork;
import com.patrykkrawczyk.liveo.LiveoApplication;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.IceContact;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.wang.avi.AVLoadingIndicatorView;

import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
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
    @Bind(R.id.registerText)        TextView registerText;
    @Bind(R.id.loginForm)           CardView loginForm;
    @Bind(R.id.loadingView)         AVLoadingIndicatorView loadingView;

    private Shimmer shimmer;
    private Driver driver;
    private Retrofit retrofit;
    private String[] credentials;
    private boolean immediateLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.LIVEO_API_URL))
                .build();

        shimmer = new Shimmer().setDuration(SPLASH_DISPLAY_LENGTH/5*4);

        YoYo.with(Techniques.FadeIn)
                .withListener(setupShowAnimator())
                .interpolate(new AccelerateInterpolator())
                .duration(SPLASH_DISPLAY_LENGTH)
                .playOn(splashText);

    //    GuideManager.resetGuide(this); // TODO DELETE THIS
        GuideManager.loadGuideState(this);

        loadIceContacts();

        registerText.setMovementMethod(LinkMovementMethod.getInstance());

        usernameEditText.setEnabled(false);
        pinEditText.setEnabled(false);
        submitButton.setEnabled(false);

    }

    private String[] loadLoginState() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String username = sharedPref.getString(getString(R.string.LIVEO_LOGIN_USERNAME),    "");
        String pin = sharedPref.getString(getString(R.string.LIVEO_LOGIN_PIN),    "");
        String[] credentials = {username, pin};

        return credentials;
    }

    private void loadIceContacts() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String s1i = sharedPref.getString(getString(R.string.LIVEO_ICE_1_ID),    "");
        String s1u = sharedPref.getString(getString(R.string.LIVEO_ICE_1_URI),   "");
        String s1n = sharedPref.getString(getString(R.string.LIVEO_ICE_1_NAME),  "");
        String s1p = sharedPref.getString(getString(R.string.LIVEO_ICE_1_PHONE),  "");

        String s2i = sharedPref.getString(getString(R.string.LIVEO_ICE_2_ID),    "");
        String s2u = sharedPref.getString(getString(R.string.LIVEO_ICE_2_URI),   "");
        String s2n = sharedPref.getString(getString(R.string.LIVEO_ICE_2_NAME),  "");
        String s2p = sharedPref.getString(getString(R.string.LIVEO_ICE_2_PHONE),  "");

        String s3i = sharedPref.getString(getString(R.string.LIVEO_ICE_3_ID),    "");
        String s3u = sharedPref.getString(getString(R.string.LIVEO_ICE_3_URI),   "");
        String s3n = sharedPref.getString(getString(R.string.LIVEO_ICE_3_NAME),  "");
        String s3p = sharedPref.getString(getString(R.string.LIVEO_ICE_3_PHONE),  "");

        IceContact ice1 = new IceContact(s1i, s1n, s1p, s1u);
        IceContact ice2 = new IceContact(s2i, s2n, s2p, s2u);
        IceContact ice3 = new IceContact(s3i, s3n, s3p, s3u);

        if (ice1.validate()) LiveoApplication.iceContactList.set(0, ice1);
        if (ice2.validate()) LiveoApplication.iceContactList.set(1, ice2);
        if (ice3.validate()) LiveoApplication.iceContactList.set(2, ice3);
    }

    private void afterLogoShow() {
        shimmer.start(splashText);

        credentials = loadLoginState();
        if (credentials[0].isEmpty() || credentials[1].isEmpty()) {
            usernameEditText.setEnabled(true);
            pinEditText.setEnabled(true);
            submitButton.setEnabled(true);

            YoYo.with(Techniques.FadeIn)
                    .interpolate(new AccelerateInterpolator())
                    .duration(SPLASH_DISPLAY_LENGTH)
                    .playOn(loginForm);
        } else {
            immediateLogin = true;
            //loadingView.setVisibility(View.VISIBLE);
            INetwork login = retrofit.create(INetwork.class);
            Call<ResponseBody> call = login.login(credentials[0], credentials[1]);

            call.enqueue(this);
        }

        /*driver = Driver.getLocalDriver(this);
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
        }*/

    }

    @OnTouch(R.id.submitButton)
    public boolean onSubmitButton(View v, MotionEvent event) {
        if (submitButton.isEnabled()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                validate();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animateViewTouch(v);
            }
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
            submitButton.setColor(getResources().getColor(R.color.newAccent));
            usernameEditText.setEnabled(false);
            pinEditText.setEnabled(false);
            submitButton.setEnabled(false);

            credentials[0] = usernameEditText.getText().toString();
            credentials[1] = pinEditText.getText().toString();

            INetwork login = retrofit.create(INetwork.class);
            Call<ResponseBody> call = login.login(credentials[0], credentials[1]);

            call.enqueue(this);
            loadingView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 400) {
            onFailure(null, null);
            submitButton.setColor(getResources().getColor(R.color.alert));
        } else {
            //loadingView.setVisibility(View.INVISIBLE);
            try {
                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                driver = mapper.readValue(body, Driver.class);

                if (driver != null) {
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(getString(R.string.LIVEO_LOGIN_USERNAME), credentials[0]);
                    editor.putString(getString(R.string.LIVEO_LOGIN_PIN), credentials[1]);
                    editor.apply();

                    if (Driver.setCurrentDriver(this, driver)) {
                        if (!immediateLogin) {
                            YoYo.with(Techniques.FadeOut)
                                    .withListener(setupFormAnimator())
                                    .delay(SPLASH_DISPLAY_LENGTH / 2)
                                    .interpolate(new AccelerateInterpolator())
                                    .duration(SPLASH_DISPLAY_LENGTH)
                                    .playOn(loginForm);
                        } else {
                            afterFormHide();
                        }

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

    private void animateViewTouch(View view) {
        YoYo.with(Techniques.Pulse)
                .interpolate(new AccelerateInterpolator())
                .duration(500)
                .playOn(view);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
