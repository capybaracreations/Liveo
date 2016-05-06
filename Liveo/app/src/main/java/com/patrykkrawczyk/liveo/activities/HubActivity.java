package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.mikephil.charting.charts.ScatterChart;
import com.google.android.gms.maps.SupportMapFragment;
import com.patrykkrawczyk.liveo.managers.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.CpuManager;
import com.patrykkrawczyk.liveo.managers.HeartRateManager;
import com.patrykkrawczyk.liveo.managers.LocationManager;
import com.patrykkrawczyk.liveo.managers.NotificationManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.StateManager;
import com.skyfishjy.library.RippleBackground;

import net.steamcrafted.materialiconlib.MaterialIconView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubActivity extends AppCompatActivity {

    private MaterialRippleLayout ripple;
    @Bind(R.id.closeButtonIcon) MaterialIconView closeButtonIcon;
    @Bind(R.id.closeButtonText) TextView closeButtonText;
    @Bind(R.id.heartText) TextView heartText;
    @Bind(R.id.heartRipple) RippleBackground heartRipple;
    @Bind(R.id.accelerometerGraph) ScatterChart accelerometerGraph;

    private CpuManager cpuManager;
    private AccelerometerManager accelerometerManager;
    private NotificationManager notificationManager;
    private StateManager stateManager;
    private HeartRateManager heartRateManager;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        ButterKnife.bind(this);

        cpuManager = CpuManager.getInstance(this);
        notificationManager = NotificationManager.getInstance(this);
        stateManager = StateManager.getInstance();

        Intent incomingIntent = getIntent();
        String action = incomingIntent.getAction();
        if (action != null && action.equals(getString(R.string.LIVEO_ACTION_CLOSE)) == true) closeHub();

        accelerometerManager = new AccelerometerManager(this, accelerometerGraph);
        heartRateManager = new HeartRateManager(this, heartRipple, heartText);
        locationManager = new LocationManager((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        initializeRipple();
    }

    @OnTouch(R.id.heartRipple)
    public boolean onHeart(View v, MotionEvent event) {
        heartRateManager.add(100);

        return true;
    }

    @OnTouch(R.id.closeButton)
    public boolean onCloseButton(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ripple.setEnabled(true);
            ripple.performRipple();
            closeButtonIcon.setColor(getResources().getColor(R.color.colorAccent));
            closeButtonText.setTextColor(getResources().getColor(R.color.colorAccent));

            onBackPressed();
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableMonitor(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableMonitor(false);
    }

    private void enableMonitor(boolean state) {
        stateManager.setMonitorState(state);
        accelerometerManager.enable(state);
        if (state) {
            cpuManager.acquireCpu();
            notificationManager.showNotification(this);
        } else {
            cpuManager.releaseCpu();
            notificationManager.hideNotification();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void closeHub() {
        finish();
        System.exit(0);
    }

    private void initializeRipple() {
        ripple = MaterialRippleLayout.on(findViewById(R.id.rippleView))
                .rippleOverlay(true)
                .rippleColor(getResources().getColor(R.color.colorRipple))
                .rippleAlpha((float)0.20)
                .ripplePersistent(true)
                .rippleDuration(375)
                .rippleDelayClick(false)
                .rippleFadeDuration(100)
                .create();
        ripple.setEnabled(false);
    }

}
