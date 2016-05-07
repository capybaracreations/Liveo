package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat.Builder;

import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.HubActivity;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class StateManager {


    private static StateManager instance;
    private boolean accelerometerState = false;
    private boolean notificationState = false;
    private boolean monitorState = false;
    private boolean heartRateState = false;
    private boolean locationState;


    public static StateManager getInstance() {
        if (instance == null) instance = new StateManager();
        return instance;
    }

    public StateManager() {

    }

    public boolean getAccelerometerState() {
        return instance.accelerometerState;
    }

    public void setAccelerometerState(boolean state) {
        instance.accelerometerState = state;
    }

    public boolean getNotificationState() {
        return instance.notificationState;
    }

    public void setNotificationState(boolean state) {
        instance.notificationState = state;
    }

    public boolean getMonitorState() {
        return instance.monitorState;
    }

    public void setMonitorState(boolean state) {
        instance.monitorState = state;
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(getString(R.string.LIVEO_IS_RUNNING), state);
//        editor.apply();
    }

    public boolean getHeartRateState() {
        return instance.heartRateState;
    }

    public void setHeartRateState(boolean state) {
        instance.heartRateState = state;
    }

    public boolean getLocationState() {
        return instance.locationState;
    }

    public void setLocationState(boolean state) {
        instance.locationState = state;
    }
}
