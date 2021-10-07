package com.BatteryAlarm.android.batteryalarm;

import android.content.Context;

import androidx.preference.PreferenceManager;
/*
This is a simple helper pojo class:-
*/

public class Utils {

    private int alarm_max;
    private boolean isAlarmOn;
    private static Utils mUtils;

    public Utils(Context context){
        setAlarmOn(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_set_alarm), false));
        setAlarm_max(PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getString(R.string.key_Set_Alarm_max),80));
    }

    public static Utils getInstance(Context context){
        if(mUtils == null){
            mUtils = new Utils(context);
        }
        return mUtils;
    }


    public int getAlarm_max() {
        return alarm_max;
    }

    public void setAlarm_max(int alarm_max) {
        this.alarm_max = alarm_max;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        isAlarmOn = alarmOn;
    }
}
