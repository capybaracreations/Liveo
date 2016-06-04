package com.patrykkrawczyk.liveo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Patryk Krawczyk on 02.06.2016.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         activeNetInfo       = connectivityManager.getActiveNetworkInfo();

        EventBus.getDefault().post(new NetworkStateEvent(activeNetInfo != null));
    }
}
