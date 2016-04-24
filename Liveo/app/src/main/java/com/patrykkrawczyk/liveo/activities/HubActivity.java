package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.mikephil.charting.charts.RadarChart;
import com.patrykkrawczyk.liveo.managers.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.CpuManager;
import com.patrykkrawczyk.liveo.managers.NotificationManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.StateManager;
import com.txusballesteros.SnakeView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubActivity extends AppCompatActivity {

    private EventBus eventBus;
    private SharedPreferences preferences;
    private CpuManager cpuManager;
    private AccelerometerManager accelerometerManager;
    private NotificationManager notificationManager;
    private StateManager stateManager;
    private MaterialRippleLayout ripple;
    @Bind(R.id.closeButton) LinearLayout closeButton;
    @Bind(R.id.closeButtonIcon) MaterialIconView closeButtonIcon;
    @Bind(R.id.closeButtonText) TextView closeButtonText;
    @Bind(R.id.heartGraph) SnakeView heartGraph;
    @Bind(R.id.accelerometerGraph) RadarChart accelerometerGraph;

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
        accelerometerManager = AccelerometerManager.getInstance(this, accelerometerGraph);

        initialize();
    }

    private void initialize() {
        stateManager.setMonitorState(true);

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

        heartGraph.setMinValue(0);
        heartGraph.setMaxValue(250);
        heartGraph.addValue(100);
        heartGraph.addValue(150);
        heartGraph.addValue(50);
        heartGraph.addValue(250);
        heartGraph.addValue(20);

        cpuManager.acquireCpu();
        notificationManager.showNotification(this);
        //accelerometerManager.showNotification(this);
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



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
