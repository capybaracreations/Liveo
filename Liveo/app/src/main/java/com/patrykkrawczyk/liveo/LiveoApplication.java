package com.patrykkrawczyk.liveo;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.patrykkrawczyk.liveo.managers.IceContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 20.05.2016.
 */
public class LiveoApplication extends Application {
    private Tracker mTracker;
    public static List<IceContact> iceContactList = new ArrayList<>(3);

    public LiveoApplication() {
        iceContactList.add(null);
        iceContactList.add(null);
        iceContactList.add(null);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
