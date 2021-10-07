package com.BatteryAlarm.android.batteryalarm;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;


public class AlertMeFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
          addPreferencesFromResource(R.xml.pref_alert_me);
    }
}
