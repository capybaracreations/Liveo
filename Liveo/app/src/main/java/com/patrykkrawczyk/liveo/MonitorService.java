package com.patrykkrawczyk.liveo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.location.MyLocationManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 15.05.2016.
 */
public class MonitorService extends Service {

    private EventBus eventBus;
    private LocalBinder binder = new LocalBinder();

    private AccelerometerManager accelerometerManager;
    private MyLocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        eventBus = EventBus.getDefault();
        accelerometerManager = new AccelerometerManager(this);
        locationManager = new MyLocationManager(this);

    }

    public void kill() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class LocalBinder extends Binder {
        public MonitorService getService() {
            return MonitorService.this;
        }
    }
}
