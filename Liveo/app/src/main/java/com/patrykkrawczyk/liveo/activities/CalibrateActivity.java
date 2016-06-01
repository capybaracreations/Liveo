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
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
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

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEvent calibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        ButterKnife.bind(this);

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
            animateViewTouch(v);
            submitButton.setColor(getResources().getColor(R.color.newAccent));
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
