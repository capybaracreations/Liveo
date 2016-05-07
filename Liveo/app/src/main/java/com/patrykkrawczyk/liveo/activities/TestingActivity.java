package com.patrykkrawczyk.liveo.activities;

    import android.location.Location;
    import android.support.annotation.Nullable;
    import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

    import com.mapbox.mapboxsdk.camera.CameraPosition;
    import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
    import com.mapbox.mapboxsdk.geometry.LatLng;
    import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
    import com.mapbox.mapboxsdk.maps.UiSettings;
    import com.patrykkrawczyk.liveo.R;


public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

    }

}
