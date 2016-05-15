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


    //cpuManager = CpuManager.getInstance(this);
    //notificationManager = NotificationManager.getInstance(this);
    //stateManager = StateManager.getInstance();

    //Intent incomingIntent = getIntent();
    //String action = incomingIntent.getAction();
    //if (action != null && action.equals(getString(R.string.LIVEO_ACTION_CLOSE)) == true) closeHub();

    //accelerometerManager = new AccelerometerViewManager(this, accelerometerGraph);
    //heartRateManager = new HeartRateViewManager(this, heartRipple, heartText);
    //locationManager = new LocationViewManager(this, savedInstanceState);




/*
    private void enableMonitor(boolean state) {
        stateManager.setMonitorState(state);
        accelerometerManager.enable(state);
        heartRateManager.enable(state);
        locationManager.enable(state);

        if (state) {
            cpuManager.acquireCpu();
            notificationManager.showNotification(this);
        } else {
            cpuManager.releaseCpu();
            notificationManager.hideNotification();
        }
    }*/


/*
    private void closeHub() {
        finish();
        System.exit(0);
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        //enableMonitor(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //enableMonitor(false);
    }



}
