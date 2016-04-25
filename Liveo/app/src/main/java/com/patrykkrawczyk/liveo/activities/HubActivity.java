package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.managers.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.CpuManager;
import com.patrykkrawczyk.liveo.managers.HeartRateManager;
import com.patrykkrawczyk.liveo.managers.NotificationManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.StateManager;
import com.txusballesteros.SnakeView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubActivity extends AppCompatActivity {

    private EventBus eventBus;
    private SharedPreferences preferences;
    private MaterialRippleLayout ripple;
    @Bind(R.id.closeButton) LinearLayout closeButton;
    @Bind(R.id.closeButtonIcon) MaterialIconView closeButtonIcon;
    @Bind(R.id.closeButtonText) TextView closeButtonText;
    @Bind(R.id.heartText) TextView heartText;
    @Bind(R.id.heartGraph) LineChart heartGraph;
    @Bind(R.id.accelerometerGraph) ScatterChart accelerometerGraph;;

    private CpuManager cpuManager;
    private AccelerometerManager accelerometerManager;
    private NotificationManager notificationManager;
    private StateManager stateManager;
    private HeartRateManager heartRateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        eventBus = EventBus.getDefault();
        ButterKnife.bind(this);
        preferences = getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        cpuManager = CpuManager.getInstance(this);
        notificationManager = NotificationManager.getInstance(this);
        stateManager = StateManager.getInstance(this);

        Intent incomingIntent = getIntent();
        String action = incomingIntent.getAction();
        if (action != null && action.equals(getString(R.string.LIVEO_ACTION_CLOSE)) == true)
            closeHub();

        accelerometerManager = new AccelerometerManager(this, accelerometerGraph);
        heartRateManager = new HeartRateManager(this, heartGraph, heartText);

        initialize();
    }

    private void initialize() {
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

        stateManager.setMonitorState(true);
        cpuManager.acquireCpu();
        notificationManager.showNotification(this);
    }

    @OnTouch(R.id.closeButton)
    public boolean onCloseButton(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            ripple.setEnabled(true);
            ripple.performRipple();
            closeButtonIcon.setColor(getResources().getColor(R.color.colorAccent));
            closeButtonText.setTextColor(getResources().getColor(R.color.colorAccent));

            stateManager.setMonitorState(false);
            notificationManager.hideNotification();
            cpuManager.releaseCpu();

            onBackPressed();
            finish();
        }
        return true;
    }



    @OnTouch(R.id.heartGraph)
    public boolean onHeart(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
                heartRateManager.feedMultiple(this);
        }
        return true;
    }

    @OnTouch(R.id.accelerometerGraph)
    public boolean onAccelerometer(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
                accelerometerManager.feedMultiple(this);
        }
        return true;
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
        stateManager.setMonitorState(false);
        notificationManager.hideNotification();
        cpuManager.releaseCpu();
        finish();
        System.exit(0);
    }
}
