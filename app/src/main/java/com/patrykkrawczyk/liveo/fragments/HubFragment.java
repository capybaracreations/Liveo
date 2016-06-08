package com.patrykkrawczyk.liveo.fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.charts.ScatterChart;
import com.patrykkrawczyk.liveo.HubFragmentsEnum;
import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.activities.HubActivity;
import com.patrykkrawczyk.liveo.activities.MainActivity;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerEvent;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;
import com.patrykkrawczyk.liveo.managers.heartrate.HeartRateViewManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.location.LocationViewManager;
import com.patrykkrawczyk.liveo.managers.sap.SapEvent;
import com.skyfishjy.library.RippleBackground;
import com.github.clans.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubFragment extends Fragment {

    @Bind(R.id.heartText)           TextView heartText;
    @Bind(R.id.heartRipple)         RippleBackground heartRipple;
    @Bind(R.id.accelerometerGraph)  ScatterChart accelerometerGraph;
    @Bind(R.id.locationButton)      FloatingActionButton locationButton;

    private HeartRateViewManager heartRateViewManager;
    private AccelerometerViewManager accelerometerViewManager;
    private EventBus eventBus;
    private LocationViewManager locationManager;
    private HubActivity activity;


    @Override
    public void onStart() {
        super.onStart();

        activity = (HubActivity) getActivity();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

        heartRateViewManager = new HeartRateViewManager(heartRipple, heartText);
        accelerometerViewManager = new AccelerometerViewManager(activity, accelerometerGraph);
        if (activity.monitorService != null) {
            heartRateViewManager.setEnabled(activity.monitorService.heartRateEnabled);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hub, container, false);
        ButterKnife.bind(this, view);
        locationManager = new LocationViewManager((AppCompatActivity) getActivity(), savedInstanceState);
        return view;
    }

    @Subscribe
    public void onAccelerometerEvent(AccelerometerEvent event) {
        accelerometerViewManager.setChartValue(event.x, event.y);
    }

    @Subscribe
    public void onSapEvent(SapEvent event) {
        final String action = event.action;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int value = -10;
                try {
                    value = Integer.valueOf(action);
                } catch (NumberFormatException e) {
                    if (action.equals("CONNECTED")) {
                        activity.monitorService.heartRateEnabled = true;
                        heartRateViewManager.setEnabled(true);
                    } else if (action.equals("DISCONNECTED")){
                        activity.monitorService.heartRateEnabled = false;
                        heartRateViewManager.setEnabled(false);
                    }
                    return;
                }

                heartRateViewManager.set(value);
            }
        });
    }

    @OnTouch(R.id.stopButton)
    public boolean onCloseButton(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animateViewTouch(view);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            activity.goToMenu();
        }
        return true;
    }

    protected void animateViewTouch(View view) {
        YoYo.with(Techniques.Pulse)
                .interpolate(new AccelerateInterpolator())
                .duration(500)
                .playOn(view);
    }

    @OnClick(R.id.locationButton)
    public void onLocationButtonClick() {
        //activity.changeFragment(HubFragmentsEnum.ALERT);
        //monitorService.connect();
        //monitorService.sendData("ALERT");
        locationManager.centerView();
        //Intent intent = new Intent(activity, AlertFragment.class);
        //startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (eventBus.isRegistered(this)) eventBus.unregister(this);
    }
}
