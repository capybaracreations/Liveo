package com.patrykkrawczyk.liveo.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.patrykkrawczyk.liveo.ChangeHubFragmentEvent;
import com.patrykkrawczyk.liveo.HubFragmentsEnum;
import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.fragments.AlertFragment;
import com.patrykkrawczyk.liveo.fragments.HubFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

;


public class HubActivity extends AppCompatActivity implements ServiceConnection {

    @Bind(R.id.hubFragment)
    RelativeLayout hubFragment;
    private FragmentManager fragmentManager;
    private EventBus eventBus;
    private long lastPress;
    private MonitorService monitorService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        ButterKnife.bind(this);
        fragmentManager = getFragmentManager();
        eventBus = EventBus.getDefault();

        changeFragment(HubFragmentsEnum.HUB);
    }

    public void changeFragment(HubFragmentsEnum page) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment;
        if (page == HubFragmentsEnum.ALERT) fragment = new AlertFragment();
        else fragment = new HubFragment();

        fragmentTransaction.add(R.id.hubFragment, fragment);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onChangeHubFragmentEvent(ChangeHubFragmentEvent event) {
        changeFragment(event.page);
    }

    public void send(String message) {
        monitorService.sendData(message);
    }

    @Override
    protected void onResume() {
        eventBus.register(this);
        super.onResume();
        if (!isBound) {
            Intent bindIntent = new Intent(this, MonitorService.class);
            bindService(bindIntent, this, BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (monitorService != null && isBound) {
            unbindService(this);
            isBound = false;
        }
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
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

    public void goToMenu() {
        //if (monitorService != null) unbindService(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToInformation() {
        //if (monitorService != null) unbindService(this);
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
        finish();
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

}
