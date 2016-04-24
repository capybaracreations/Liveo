package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.patrykkrawczyk.liveo.managers.CpuManager;
import com.patrykkrawczyk.liveo.managers.NotificationManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.StateManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HubActivity extends AppCompatActivity {

    private EventBus eventBus;
    private SharedPreferences preferences;
    private CpuManager cpuManager;
    private NotificationManager notificationManager;
    private StateManager stateManager;

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
        
        initialize();
    }

    private void initialize() {
        stateManager.setMonitorState(true);

        cpuManager.acquireCpu();
        notificationManager.showNotification(this);
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
