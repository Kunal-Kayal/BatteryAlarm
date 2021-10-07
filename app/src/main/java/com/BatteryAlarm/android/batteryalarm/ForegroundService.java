package com.BatteryAlarm.android.batteryalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.Timer;
import java.util.TimerTask;

/*
This class is for showing Notification & keep tracking the battery in
   every changes
 Set the timer according to battery level
 :-
 */
public class ForegroundService extends Service {

    private static final int NOTIFICATION_ID = 999;
    boolean isAlarmSet;
    int alarm_max;

    Uri alarmSound;
    MediaPlayer mp;
    Utils mUtils;
    Timer timer = new Timer();
    NotificationCompat.Builder builder;
    NotificationManager managerCompat;
    Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(this, alarmSound);

        mUtils = Utils.getInstance(getApplicationContext());

        isAlarmSet = mUtils.isAlarmOn();
        alarm_max = mUtils.getAlarm_max();

        managerCompat = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, "Battery Alert");
        builder.setOngoing(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Intent batteryStatus = getApplication()
                        .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                if (batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                        == BatteryManager.BATTERY_STATUS_CHARGING) {

                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryPct = level * 100 / (float) scale;

                    if (isAlarmSet) {
                        if (alarm_max <= (int) batteryPct)
                            showAlert("Battery Reached at " + alarm_max+"%");
                    }
                }
            }
        }, 0, 1000);

        /*
        Creating the notification for foreground services:
         */
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent homePagePendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0);

        Intent closeButtonIntent = new Intent(this, CloseButtonReceiver.class);
        PendingIntent closeButtonPendingIntent = PendingIntent.getBroadcast(this, 0, closeButtonIntent,0);

        //Creating Notification channel:

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Battery Alert";
            CharSequence name = "Foreground Service";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            managerCompat.createNotificationChannel(mChannel);
        }

        //Set all things up and apply notification:

        notification = builder
                .setContentTitle("Battery Alert")
                .setSmallIcon(R.drawable.ic_device_is_charging)
                .setContentText("Battery Alert on " + alarm_max + "%")
                .setContentIntent(homePagePendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Battery Level Alert on " + alarm_max + "%"))
                .addAction(R.drawable.ic_launcher_foreground, "stop", closeButtonPendingIntent)
                .build();

        managerCompat.notify(NOTIFICATION_ID, notification);

        startForeground(NOTIFICATION_ID, notification);

    }

    @Override
    public void onDestroy() {
        timer.cancel();
        mp.stop();
        managerCompat.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }

    void showAlert(String text) {

        timer.cancel();
        mp.start();

        builder.setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_ALARM);
        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }

}

