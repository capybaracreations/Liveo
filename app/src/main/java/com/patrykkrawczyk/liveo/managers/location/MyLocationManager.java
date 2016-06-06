package com.patrykkrawczyk.liveo.managers.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.patrykkrawczyk.liveo.MonitorService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Patryk Krawczyk on 18.05.2016.
 */
public class MyLocationManager implements LocationListener
{

    private static boolean enabled = false;
    private static final int UPDATE_THRESHOLD = 100;

    public MyLocationManager(MonitorService service) {
        LocationManager locationManager = (LocationManager) service.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(service, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(service, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enabled = true;
        }

        if (enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_THRESHOLD, 10, this);
        }
    }

    @SuppressWarnings("MissingPermission")
    public static Location getLastLocation(Context context) {
        Location bestLocation = null;

        bestLocation = LocationViewManager.lastLocation;

        if (enabled && bestLocation != null) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);

            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }

        return bestLocation;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onLocationChanged(Location location) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderDisabled(String provider) {}
}
