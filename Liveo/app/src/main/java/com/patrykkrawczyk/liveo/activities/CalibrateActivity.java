package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalibrateActivity extends AppCompatActivity implements SensorEventListener {

    @Bind(R.id.submitButton)
    MaterialIconView submitButton;
    protected MaterialRippleLayout ripple;
    protected final int RIPPLE_SPEED = 400;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEvent calibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        ButterKnife.bind(this);

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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnTouch(R.id.submitButton)
    public boolean onSubmitButton(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            submitButton.setColor(getResources().getColor(R.color.colorAccent));
            Point point = new Point((int)event.getRawX(), (int)event.getRawY());
            ripple.performRipple(point);

            AccelerometerViewManager.setCalibration(calibration);
            sensorManager.unregisterListener(this);

            Intent intent = new Intent(this, HubActivity.class);
            startActivity(intent);
            finish();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
