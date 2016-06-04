package com.patrykkrawczyk.liveo.managers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

import com.patrykkrawczyk.liveo.MonitorService;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.activities.HubActivity;

/**
 * Created by Patryk Krawczyk on 24.04.2016.
 */
public class NotificationManager {

    private static int id = 1;
    private static Notification notification;

    public NotificationManager(MonitorService service) {
        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notification == null)
            notification = new Builder(service)
                .setOngoing(true)
                .setSmallIcon(R.drawable.heart_pulse)
                .setContentTitle("Liveo")
                .setContentText(service.getString(R.string.LIVEO_SLOGAN))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(createPendingIntent(service)).build();

        service.startForeground(id, notification);
        //notificationManager.notify(id, notification);
    }

    private PendingIntent createPendingIntent(MonitorService service) {
        Intent intent = new Intent(service, HubActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        service,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        return pendingIntent;
    }
}
