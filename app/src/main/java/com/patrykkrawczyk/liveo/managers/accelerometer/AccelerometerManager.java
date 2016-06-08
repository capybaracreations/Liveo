package com.patrykkrawczyk.liveo.managers.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.patrykkrawczyk.liveo.AlertEvent;
import com.patrykkrawczyk.liveo.MonitorService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 15.05.2016.
 */
public class AccelerometerManager implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 5000;
    private final int UPDATE_THRESHOLD = 100;
    private long lastUpdateTime;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean enabled = false;
    private EventBus eventBus;
    private static SensorEvent lastSensorEvent;
    private long lastUpdate;
    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;
    public boolean canInvokeAlert = true;

    public AccelerometerManager(MonitorService service) {
        eventBus = EventBus.getDefault();

        sensorManager = (SensorManager) service.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor == null) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

    public void setEnabled(boolean state) {
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
                lastSensorEvent = event;
                eventBus.post(new AccelerometerEvent(event.values[0], event.values[2]));
            }

            //sensor
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long differTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / differTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    if (canInvokeAlert) {
                        Log.d("patryczek", "INVOKE");
                        eventBus.post(new AlertEvent());
                        canInvokeAlert = false;
                    }
                    //Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public static SensorEvent getLastSensorEvent() {
        return lastSensorEvent;
    }
}
