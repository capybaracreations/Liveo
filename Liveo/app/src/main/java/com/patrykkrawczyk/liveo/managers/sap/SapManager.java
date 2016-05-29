package com.patrykkrawczyk.liveo.managers.sap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.HubActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 29.05.2016.
 */
public class SapManager {

    private boolean mIsBound = false;
    private ConsumerService mConsumerService = null;
    private MonitorService service;
    private EventBus eventBus;

    public SapManager(MonitorService service) {
        this.service = service;
        eventBus = EventBus.getDefault();
        mIsBound = service.bindService(new Intent(service, HubActivity.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void connect() {
        if (mIsBound == true && mConsumerService != null) {
            mConsumerService.findPeers();
        }
    }

    public void disconnect() {
        if (mIsBound == true) {
            if (mConsumerService != null) mConsumerService.closeConnection();
            service.unbindService(mConnection);
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
            //updateTextView("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mConsumerService = null;
            mIsBound = false;
            //updateTextView("onServiceDisconnected");
        }
    };
}
