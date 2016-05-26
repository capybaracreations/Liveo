package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.GeocodingFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.location.MyLocationManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.itangqi.waveloadingview.WaveLoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AlertActivity extends AppCompatActivity implements Callback<GeocodingResponse> {

    private final long FULL_TIME = 5000;

    @Bind(R.id.progressBar)
    WaveLoadingView progressBar;
    private CountDownTimer timer;
    private Vibrator vibrator;
    private boolean vibratorEnabled = true;
    private SmsManager smsManager;
    private String message = "";
    private List<String> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        ButterKnife.bind(this);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibratorEnabled = true;

        smsManager = SmsManager.getDefault();

        timer = new CountDownTimer(FULL_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                timerProgress(millisUntilFinished);
            }

            public void onFinish() {
                timerEnd();
            }
        };

        timer.start();
    }

    private void timerEnd() {
        progressBar.setProgressValue(0);
        progressBar.setCenterTitle("...");
        vibratorEnabled = false;
        progressBar.invalidate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareSms();
            }
        }).start();
    }

    private void prepareSms() {
        if (Looper.myLooper() == Looper.getMainLooper()) Log.d("PATRYCZEK", "MAIN");
        else Log.d("PATRYCZEK", "SEP");

        Driver driver = Driver.getLocalDriver(this);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String icePhoneNumber1 = sharedPref.getString(getString(R.string.LIVEO_ICE_1_PHONE), "");
        String icePhoneNumber2 = sharedPref.getString(getString(R.string.LIVEO_ICE_2_PHONE), "");
        String icePhoneNumber3 = sharedPref.getString(getString(R.string.LIVEO_ICE_3_PHONE), "");

        if (!icePhoneNumber1.isEmpty()) numbers.add(icePhoneNumber1);
        if (!icePhoneNumber2.isEmpty()) numbers.add(icePhoneNumber2);
        if (!icePhoneNumber3.isEmpty()) numbers.add(icePhoneNumber3);

        message = "I had an accident. Please send help to my location: ";
        Location location = MyLocationManager.getLastLocation(this);
        message += "(" + String.valueOf(location.getLatitude()) + "; " + String.valueOf(location.getLongitude()) + ")";
        message += ".\nFirst name: " + driver.getFirstName();
        message += ".\nLast name: " + driver.getLastName();
        message += ".\nGender: " + driver.getGender().toUpperCase();
        message += ".\nAge group: " + driver.getAgeGroup().toUpperCase();
        message += ".\nRegistration: " + driver.getRegisterNumber().toUpperCase();
        message += ".\nPassengers: " + sharedPref.getString(getString(R.string.LIVEO_PASSENGERS_COUNT),  "0") + ".";

        try {
            final Position position = Position.fromCoordinates(-73.989, 40.733);

            MapboxGeocoding client = new MapboxGeocoding.Builder()
                    .setAccessToken(getString(R.string.LIVEO_MAPBOX_TOKEN))
                    .setCoordinates(position)
                    .setType(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            client.enqueueCall(this);
        } catch (ServicesException e) {
            e.printStackTrace();
        }
    }

    private void timerProgress(long secondsLeft) {
        int progress = (int)Math.round(((double)secondsLeft)/FULL_TIME*100);
        progressBar.setProgressValue(progress-1);
        progressBar.setCenterTitle(String.valueOf((int)(secondsLeft/1000))+"s");

        if (vibratorEnabled) vibrator.vibrate(250);
    }

    @OnClick(R.id.safeButton)
    public void safeButtonClick(View view) {
        timer.cancel();
        vibratorEnabled = false;
        progressBar.setProgressValue(0);
        progressBar.setCenterTitle("...");

        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendSms(String message, List<String> numbers) {
        if (smsManager != null) {
            ArrayList<String> parts = smsManager.divideMessage(message);

            for (String number : numbers) {
                smsManager.sendMultipartTextMessage(number, null, parts, null, null);
                    //smsManager.sendTextMessage(number, null, message, null, null);
            }
        }

        moveToInformationScreen();
    }

    private void moveToInformationScreen() {
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
        GeocodingResponse geocoding = response.body();

        List<GeocodingFeature> features = geocoding.getFeatures();
        if (features != null) {
            GeocodingFeature feature = features.get(0);
            message += "\nMy approx. location is: " + feature.getPlaceName() + ".";
        }

        sendSms(message, numbers);
    }

    @Override
    public void onFailure(Call<GeocodingResponse> call, Throwable t) {
        Log.e("PAI", "Error: " + t.getMessage());
    }
}
