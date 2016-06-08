package com.patrykkrawczyk.liveo.managers.sap;

import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.INetwork;
import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.location.MyLocationManager;

import java.util.Random;


/**
 * Created by Patryk Krawczyk on 26.05.2016.
 */
public class SapBroadcaster{

    private static final int updateInterval = 5000;
    private Handler handler;
    private MonitorService service;
    private Runnable mStatusChecker;


    public SapBroadcaster(final MonitorService service) {
        this.service = service;
        handler = new Handler();

        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    sendData();
                } finally {
                    handler.postDelayed(mStatusChecker, updateInterval);
                }
            }
        };

        mStatusChecker.run();
    }

    private void sendData() {
        service.sendData("HR");
        Log.d("PATRYCZEK", "HR");
    }

    public void kill() {
        handler.removeCallbacks(mStatusChecker);
    }

}
