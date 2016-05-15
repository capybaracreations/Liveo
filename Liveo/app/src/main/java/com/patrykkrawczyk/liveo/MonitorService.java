package com.patrykkrawczyk.liveo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.patrykkrawczyk.liveo.managers.heartrate.HeartRateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 15.05.2016.
 */
public class MonitorService extends Service {

    private EventBus eventBus;
    private LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        eventBus = EventBus.getDefault();
        doLongRunningOperation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void kill() {
        stopSelf();
    }

    public void doLongRunningOperation() {
        new Runnable() {
            @Override
            public void run() {
                Log.d("PATRYCZEK", "kekek");
            }
        };
        eventBus.post(new HeartRateEvent(50));
    }

    public class LocalBinder extends Binder {
        public MonitorService getService() {
            return MonitorService.this;
        }
    }
}
