 package com.BatteryAlarm.android.batteryalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.shadow.ShadowViewDelegate;

public class AlertMeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_me);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}