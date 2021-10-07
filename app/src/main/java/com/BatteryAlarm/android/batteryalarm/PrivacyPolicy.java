package com.BatteryAlarm.android.batteryalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
/*
This class contains only privacy policy of the app:-
N.B: You can Ignore this:
 */

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView web =(WebView)findViewById(R.id.webView);
        web.loadUrl("file:///android_asset/privacypolicy.html");
    }
}