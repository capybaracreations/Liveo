package com.patrykkrawczyk.liveo.managers.data;

import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.INetwork;
import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerManager;
import com.patrykkrawczyk.liveo.managers.location.MyLocationManager;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;

/**
 * Created by Patryk Krawczyk on 26.05.2016.
 */
public class DataBroadcaster {


    private static final int updateInterval = 5000;
    private Handler handler;
    private Runnable updateSender;
    private Retrofit retrofit;
    private MonitorService service;


    public void run() {
        updateSender  = new Runnable() {
            @Override
            public void run() {
                try {
                    sendData();
                } finally {
                    handler.postDelayed(updateSender, updateInterval);
                }
            }
        };
        updateSender.run();
    }

    public DataBroadcaster(final MonitorService service) {
        this.service = service;

        retrofit = new Retrofit.Builder()
                .baseUrl(service.getString(R.string.LIVEO_API_URL))
                .build();

        handler = new Handler();
    }

    private void sendData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Driver driver = Driver.getLocalDriver(service);
                Location location = MyLocationManager.getLastLocation(service);
                SensorEvent sensorEvent = AccelerometerManager.getLastSensorEvent();

                if (driver != null && location != null && sensorEvent != null) {
                    INetwork data = retrofit.create(INetwork.class);
                    String id, latitude, longitude, heartRate, accX, accY, accZ, trip;

                    id = driver.getId();

                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    heartRate = String.valueOf(new Random().nextInt(100));

                    accX = String.valueOf(sensorEvent.values[0]);
                    accY = String.valueOf(sensorEvent.values[1]);
                    accZ = String.valueOf(sensorEvent.values[2]);

                    try {
                        trip = String.valueOf(Integer.parseInt(driver.getCurrentTrip()) + 1);

                        Call<ResponseBody> call = data.data(id, latitude, longitude, heartRate, accX, accY, accZ, trip);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Log.d("PATRYCZEK", "data broadcast success");
                                } else {
                                    Log.d("PATRYCZEK", "broadcast fail");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("PATRYCZEK", "broadcast fail");
                            }
                        });
                    } catch(Exception e) {

                    }
                }
            }
        });
        thread.start();

    }

    public void kill() {
        handler.removeCallbacks(updateSender);
    }

}
