package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.HubActivity;
import com.patrykkrawczyk.liveo.activities.MainActivity;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalibrateFragment extends AnimatedFragment implements SensorEventListener {

    @Bind(R.id.submitButton)
    MaterialIconView submitButton;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEvent calibration;
    private EventBus eventBus;

    public CalibrateFragment() {
        super(R.layout.activity_calibrate);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
        //setContentView(R.layout.activity_calibrate);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        touchEnabled = true;
    }

    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) {
        if (eventBus.isRegistered(this)) eventBus.unregister(this);
        touchEnabled = false;
        changePage(Page.MENU);
    }

    @OnTouch(R.id.submitButton)
    public boolean onSubmitButton(View v, MotionEvent event) {
        if (touchEnabled) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                submitButton.setColor(getResources().getColor(R.color.newAccent));
                AccelerometerViewManager.setCalibration(calibration);
                sensorManager.unregisterListener(this);

                Intent intent = new Intent(getContext(), HubActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animateViewTouch(v);
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        calibration = event;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
