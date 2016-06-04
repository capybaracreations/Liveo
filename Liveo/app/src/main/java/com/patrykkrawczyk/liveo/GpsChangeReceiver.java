package com.patrykkrawczyk.liveo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 02.06.2016.
 */
public class GpsChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        boolean state = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        EventBus.getDefault().post(new GpsStatusEvent(state));
    }
}
