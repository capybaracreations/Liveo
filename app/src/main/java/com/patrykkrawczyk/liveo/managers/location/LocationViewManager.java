package com.patrykkrawczyk.liveo.managers.location;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.patrykkrawczyk.liveo.R;

/**
 * Created by Patryk Krawczyk on 07.05.2016.
 */
public class LocationViewManager implements OnMapReadyCallback, MapboxMap.OnMapClickListener, MapboxMap.OnScrollListener, MapboxMap.OnMyLocationChangeListener {

    public static Location lastLocation;
    private MapboxMap mapboxMap;
    private boolean enabled = false;
    private boolean locked = true;
    private MapView mapView;
    SupportMapFragment mapFragment;

    public LocationViewManager(AppCompatActivity activity, Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enabled = true;
        }

        if (savedInstanceState == null) {
            // Create fragment
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.accessToken("pk.eyJ1IjoiY2FweWJhcmEiLCJhIjoiY2lueGo5MWgwMDBydncya2x4ZXh2M3I5dyJ9.ArNA_6puuM4UZ1J_3Xq-3Q");
            options.styleUrl("mapbox://styles/mapbox/streets-v8");
            options.logoEnabled(false);
            options.rotateGesturesEnabled(false);
            options.compassEnabled(false);
            options.attributionEnabled(false);

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            FrameLayout frameLayout = (FrameLayout) activity.findViewById(R.id.mapView);
            if (frameLayout != null) {
                ((FrameLayout) frameLayout.getParent()).removeView(frameLayout);
                frameLayout = null;//.removeAllViews();
            }
            // Add map fragment to parent container
            transaction.add(R.id.mapView, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(this);
    }

    public boolean isEnabled() {
        return enabled;
    }


    public void centerView() {
        locked = true;

        if (enabled) {
            animateCamera(mapboxMap.getMyLocation());
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //mapboxMap.setMyLocationEnabled(true);
        //mapboxMap.setOnMyLocationChangeListener(this);
    }

    public void animateCamera(Location location) {
        if (locked && location != null && enabled) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition position = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(17)
                    .tilt(45)
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) { centerView(); locked = false; }

    @Override
    public void onScroll() { locked = false; }

    @Override
    public void onMyLocationChange(@Nullable Location location) {
        if (location != null) {
            lastLocation = location;
            animateCamera(location);
        }
    }

}
