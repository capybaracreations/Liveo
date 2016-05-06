package com.patrykkrawczyk.liveo.managers;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.patrykkrawczyk.liveo.R;

/**
 * Created by Patryk Krawczyk on 06.05.2016.
 */
public class LocationManager implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public LocationManager(SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
