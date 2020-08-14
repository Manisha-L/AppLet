package com.W9221214.AppLet.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import com.W9221214.AppLet.utils.Constants.NotificationsChannels;

public class ApplicationClass extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    NotificationsChannels.DEFAULT_CHANNEL_ID,
                    NotificationsChannels.DEFAULT_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(NotificationsChannels.DEFAULT_CHANNEL_ID);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
