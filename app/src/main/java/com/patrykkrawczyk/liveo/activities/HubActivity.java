package com.patrykkrawczyk.liveo.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerEvent;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;
import com.patrykkrawczyk.liveo.managers.heartrate.HeartRateViewManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.location.LocationViewManager;
import com.patrykkrawczyk.liveo.managers.sap.SapEvent;
import com.skyfishjy.library.RippleBackground;
import com.github.clans.fab.FloatingActionButton;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubActivity extends AppCompatActivity implements ServiceConnection {

    @Bind(R.id.heartText)           TextView heartText;
    @Bind(R.id.heartRipple)         RippleBackground heartRipple;
    @Bind(R.id.accelerometerGraph)  ScatterChart accelerometerGraph;
    @Bind(R.id.locationButton)      FloatingActionButton locationButton;

    private long lastPress;
    private HeartRateViewManager heartRateViewManager;
    private AccelerometerViewManager accelerometerViewManager;
    private EventBus eventBus;
    private LocationViewManager locationManager;
    private MonitorService monitorService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        eventBus = EventBus.getDefault();
        ButterKnife.bind(this);

        heartRateViewManager = new HeartRateViewManager(heartRipple, heartText);
        accelerometerViewManager = new AccelerometerViewManager(this, accelerometerGraph);
        locationManager = new LocationViewManager(this, savedInstanceState);
    }

    @Subscribe
    public void onAccelerometerEvent(AccelerometerEvent event) {
        accelerometerViewManager.setChartValue(event.x, event.y);
    }

    @Subscribe
    public void onSapEvent(SapEvent event) {
        final String action = event.action;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int value = -10;
                try {
                    value = Integer.valueOf(action);
                } catch (NumberFormatException e) {
                    if (action.equals("CONNECTED")) {
                        heartRateViewManager.setEnabled(true);
                    } else {
                        heartRateViewManager.setEnabled(false);
                    }
                    return;
                }

                if (value != -10) {
                    heartRateViewManager.set(value);
                }
            }
        });
    }

    @OnTouch(R.id.stopButton)
    public boolean onCloseButton(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            goToMenu();
        }
        return true;
    }

    @OnClick(R.id.locationButton)
    public void onLocationButtonClick() {
        //monitorService.connect();
        //monitorService.sendData("ALERT");
        //locationManager.centerView();
        Intent intent = new Intent(this, AlertActivity.class);
        startActivity(intent);
    }

    private void goToMenu() {
        //if (monitorService != null) unbindService(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 3000) {
            Toast.makeText(this, R.string.LIVEO_PRESS_BACK_AGAIN, Toast.LENGTH_SHORT).show();
            lastPress = currentTime;
        } else {
            goToMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.onResume();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
        if (!isBound) {
            Intent bindIntent = new Intent(this, MonitorService.class);
            bindService(bindIntent, this, BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.onPause();
        if (eventBus.isRegistered(this)) eventBus.unregister(this);

        if (monitorService != null && isBound) {
            unbindService(this);
            isBound = false;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        monitorService = ((MonitorService.LocalBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        monitorService = null;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        locationManager.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        locationManager.onSaveInstanceState(outState);
    }
}
