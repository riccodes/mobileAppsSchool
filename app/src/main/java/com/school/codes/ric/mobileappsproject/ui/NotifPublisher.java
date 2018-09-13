package com.school.codes.ric.mobileappsproject.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_ID;

public class NotifPublisher extends BroadcastReceiver {

    public static final String TAG = NotifPublisher.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);


        manager.notify(intent.getIntExtra(NOTIFICATION_ID, 0), notification);

        Log.d(TAG, "onReceive()");
    }


}


