package com.patrykkrawczyk.liveo.managers.sap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.patrykkrawczyk.liveo.MonitorService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 29.05.2016.
 */
public class SapManager {

    private boolean mIsBound = false;
    private ConsumerService mConsumerService = null;
    private MonitorService monitorService;
    private EventBus eventBus;

    public SapManager(MonitorService service) {
        this.monitorService = service;
        eventBus = EventBus.getDefault();
        mIsBound = service.bindService(new Intent(service, ConsumerService.class), mConnection, Context.BIND_AUTO_CREATE);
        Log.d("PATRYCZEK", "x");
    }


    public void disconnect() {
        if (mIsBound == true && mConsumerService != null) {
            if (mConsumerService.closeConnection() == false) {
            }
        }
        // Un-bind service
        if (mIsBound) {
            monitorService.unbindService(mConnection);
            mIsBound = false;
        }
    }

    public void send(String action) {
        if (mIsBound == true && mConsumerService != null) {
            mConsumerService.sendData(action);
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mConsumerService = ((ConsumerService.LocalBinder) service).getService();
            monitorService.heartRateEnabled = true;
            //updateTextView("onServiceConnected");
            Log.d("PATRYCZEK", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mConsumerService = null;
            mIsBound = false;
            monitorService.heartRateEnabled = false;
            //updateTextView("onServiceDisconnected");
            Log.d("PATRYCZEK", "disc");
        }
    };
}
