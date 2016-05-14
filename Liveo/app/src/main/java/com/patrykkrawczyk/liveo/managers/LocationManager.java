package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.patrykkrawczyk.liveo.R;

import butterknife.OnClick;

/**
 * Created by Patryk Krawczyk on 07.05.2016.
 */
public class LocationManager implements OnMapReadyCallback, MapboxMap.OnMyLocationChangeListener {

    private MapboxMap mapboxMap;
    private SupportMapFragment mapFragment;
    private StateManager stateManager;


    public LocationManager(FragmentActivity activity, Bundle savedInstanceState) {
        stateManager = StateManager.getInstance();

        if (savedInstanceState == null) {
            MapboxMapOptions options = new MapboxMapOptions();
            options.accessToken(activity.getString(R.string.LIVEO_MAPBOX_TOKEN));
            //options.styleUrl(Style.DARK);
            options.styleUrl(activity.getString(R.string.LIVEO_MAPBOX_STYLE));
            options.attributionEnabled(false);
            options.rotateGesturesEnabled(false);
            options.compassEnabled(false);
            options.logoEnabled(false);
            mapFragment = SupportMapFragment.newInstance(options);

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mapContainer, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(this);
    }

    public void enable(boolean state) {
        if (state) {
            // TODO: TURN ON
        } else {
            // TODO: TURN OFF
        }

        stateManager.setLocationState(state);
    }

    public void centerView() {
        animateCamera(mapboxMap.getMyLocation());
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMyLocationEnabled(true);
        mapboxMap.setOnMyLocationChangeListener(this);

        Location location = mapboxMap.getMyLocation();
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .tilt(45)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position));
    }

    private void animateCamera(Location location) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .tilt(45)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position));
    }


    @Override
    public void onMyLocationChange(@Nullable Location location) {
        if (location != null) animateCamera(location);
    }


}
