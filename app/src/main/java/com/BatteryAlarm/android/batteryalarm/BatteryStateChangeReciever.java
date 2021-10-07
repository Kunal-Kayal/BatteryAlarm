package com.BatteryAlarm.android.batteryalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BatteryStateChangeReciever extends BroadcastReceiver {
    Utils mUtils;
    boolean isAlarmOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        mUtils = Utils.getInstance(context);
        isAlarmOn = mUtils.isAlarmOn();

        if(isAlarmOn){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, ForegroundService.class));
            }
            else
                context.startService(new Intent(context, ForegroundService.class));
        }else{
            context.stopService(new Intent(context, ForegroundService.class));
        }
    }
}
