package com.example.todoplaceholder.utils.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoplaceholder.R;

public class NotificationSchedule extends BroadcastReceiver {

    private static final String CHANNEL_ID = "myTODOAPP";


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(intent.getStringExtra("TITLE"))
                .setContentText(intent.getStringExtra("DESCRIPTION"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(intent.getIntExtra("NOTIFICATION_ID", 200), builder.build());
    }
}