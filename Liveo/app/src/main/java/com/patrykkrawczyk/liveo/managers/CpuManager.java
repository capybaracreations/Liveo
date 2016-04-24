package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.os.PowerManager;

import com.patrykkrawczyk.liveo.R;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class CpuManager {

    private static CpuManager instance;

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    public static CpuManager getInstance(Activity activity) {
        if (instance == null) instance = new CpuManager(activity);
        return instance;
    }

    public CpuManager(Activity activity) {
        powerManager = (PowerManager) activity.getSystemService(activity.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, activity.getString(R.string.LIVEO_WAKE_LOCK));
    }

    public void acquireCpu() {
        if (instance.wakeLock != null) instance.wakeLock.acquire();
    }

    public void releaseCpu() {
        if (instance.wakeLock != null) instance.wakeLock.release();
    }
}
