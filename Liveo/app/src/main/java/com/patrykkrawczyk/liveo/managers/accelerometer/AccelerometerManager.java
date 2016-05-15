package com.patrykkrawczyk.liveo.managers.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.patrykkrawczyk.liveo.activities.CalibrateActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 15.05.2016.
 */
public class AccelerometerManager implements SensorEventListener {

    private final int UPDATE_THRESHOLD = 100;
    private long lastUpdateTime;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean enabled = false;
    private EventBus eventBus;

    public AccelerometerManager(Activity activity) {
        eventBus = EventBus.getDefault();
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor == null) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

    private void setEnabled(boolean state) {
        enabled = state;

        if (isEnabled()) {
            sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            sensorManager.unregisterListener(this);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            long diffTime = (currentTime - lastUpdateTime);
            if (diffTime > UPDATE_THRESHOLD) {
                lastUpdateTime = currentTime;
                eventBus.post(new AccelerometerEvent(event.values[0], event.values[2]));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
