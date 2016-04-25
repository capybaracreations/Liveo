package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.HubActivity;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class NotificationManager {


    private static NotificationManager instance;

    private android.app.NotificationManager mNotificationManager;
    private Builder mBuilder;
    private StateManager stateManager;
    private static int id = 1;


    public static NotificationManager getInstance(Activity activity) {
        if (instance == null) instance = new NotificationManager(activity);
        return instance;
    }

    public NotificationManager(Activity activity) {
        stateManager = StateManager.getInstance(activity);

        mNotificationManager =
                (android.app.NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new Builder(activity)
                .setOngoing(true)
                .addAction(R.drawable.stop, "Stop", createPendingIntent(activity, true))
                .setSmallIcon(R.drawable.heart_pulse)
                .setContentTitle("Liveo")
                .setContentText(activity.getString(R.string.LIVEO_SLOGAN))
                .setPriority(Notification.PRIORITY_MAX);
    }

    private PendingIntent createPendingIntent(Activity activity, boolean shouldClose) {
        Intent intent = new Intent(activity, HubActivity.class);
        if (shouldClose) intent.setAction(activity.getString(R.string.LIVEO_ACTION_CLOSE));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        activity,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        return pendingIntent;
    }

    public void showNotification(Activity activity) {

        mBuilder.setContentIntent(createPendingIntent(activity, false));

        mNotificationManager.notify(id, mBuilder.build());

        stateManager.setNotificationState(true);
    }

    public void hideNotification() {
        mNotificationManager.cancel(id);
        stateManager.setNotificationState(false);
    }
}
