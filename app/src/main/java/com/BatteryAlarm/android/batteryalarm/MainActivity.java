package com.BatteryAlarm.android.batteryalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
This is the main UI for this app
It update the UI according the data and by the help of Utils Class
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*
     Variables Used in this activity------------->
     */
    private  String current_battery_level;
    private  String battery_health;
    private  String battery_temp;
    private  String battery_volt;
    private  boolean isCharging;
    private  int progress_status;

    /*
     xml instances--->
     */
    TextView mtextview_battary_level;
    ImageView mtextView_img_bat_level;
    TextView mtexView_health;
    TextView mtextView_temp;
    TextView mTextView_volt;
    TextView mTextView_battary_last;
    ProgressBar mBatteryProgress;
    ImageView mHealth_image_view;
    CardView mAlertMe;
    ProgressBar mBatteryCapacity;
    TextView mBatteryCapacityTextView;

    /*
    This is for broadcast reciver--->
     */
    BatteryBroadCast mBatteryBroadCast;
    IntentFilter mIntentFilter;

    /*
    this is for setting alert system---->
     */
    Utils mUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = this.getSharedPreferences("appInfo", 0);
        boolean firstTime = settings.getBoolean("first_time", true);
        if (firstTime) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("first_time", false);
            editor.commit();
            Intent i=new Intent(this,CustomAppIntro.class);
            startActivity(i);
            finish();
        }

        initVariable();
        mUtils = Utils.getInstance(this);

        /*
        Card view Click operation--->
         */
        mAlertMe.setOnClickListener(v -> {
            Intent alert_Intent = new Intent(getApplicationContext(),AlertMeActivity.class);
            startActivity(alert_Intent);
        });


        /*
        setting the preference sreen---->
         */
        setUpPreference();

        /*
        setting the broadcast-->
         */
        mBatteryBroadCast = new BatteryBroadCast();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        if(mUtils.isAlarmOn()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
            }
            else
                startService(new Intent(MainActivity.this, ForegroundService.class));
        }else {
            stopService(new Intent(MainActivity.this, ForegroundService.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

     void  viewBinding() {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null,iFilter);

        /*
        The following is for getting the data from android
        & update the UI according the data
         */

        //Determining the charging status:-
        int stauts = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        isCharging = stauts == BatteryManager.BATTERY_STATUS_CHARGING;
        if(isCharging) {
            //Set UI according to charging via usb or ac:-
            int charge_via = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (charge_via == BatteryManager.BATTERY_PLUGGED_USB) {
                mTextView_battary_last.setText(R.string.charge_mode_USB);
            } else if (charge_via == BatteryManager.BATTERY_PLUGGED_AC) {
                mTextView_battary_last.setText(R.string.charge_mode_AC);
            } else if (charge_via == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                mTextView_battary_last.setText(R.string.charge_mode_wearless);
            } else mTextView_battary_last.setText(R.string.charge_mode_Unknown);
        }else{
            if(stauts == BatteryManager.BATTERY_STATUS_DISCHARGING)
                mTextView_battary_last.setText(R.string.battery_discharging);
            else if(stauts == BatteryManager.BATTERY_STATUS_FULL)
                mTextView_battary_last.setText(R.string.battery_full);
            else if(stauts == BatteryManager.BATTERY_STATUS_NOT_CHARGING)
                mTextView_battary_last.setText(R.string.battery_not_charging);
            else {
                mTextView_battary_last.setText(R.string.on_battery);
            }
        }

        //Determining battery capacity:-
         double capacity = getBatteryCapacity();
        if(capacity != -1) {
            mBatteryCapacity.setProgress((int) capacity);
            mBatteryCapacityTextView.setText(capacity + getString(R.string.battery_unit));
        }else{
            mBatteryCapacity.setProgress(0);
            mBatteryCapacityTextView.setText(R.string.Unknown);
        }

        // Set the battery level scale:-
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        //Calculation the battery level:-
        float batteryPct = level * 100 / (float)scale;
        current_battery_level = (batteryPct)+"%";
        progress_status = (int)batteryPct;


        //Determining battery health:-
        int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
        battery_health = getHealthString(health);

        //Determining battery temperature:-
        float temp = (float)batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)/10;
        battery_temp = (temp)+"\u00b0"+"C";

        //Determining battery voltage:-
        float volt = (float)batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
        if(volt>1000)volt/=1000;
        battery_volt = (volt)+"V";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_main_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.intro){
            Intent intent = new Intent(this, CustomAppIntro.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.Privacy){
            Intent intent = new Intent(this,PrivacyPolicy.class);
            startActivity(intent);
        }
        return true;
    }

    private void initVariable(){
        mTextView_battary_last = findViewById(R.id.estimate_time_for_battery);
        mtextview_battary_level = findViewById(R.id.battery_level);
        mtextView_img_bat_level = findViewById(R.id.image_battery_level);
        mBatteryCapacityTextView = findViewById(R.id.textview_batteryCapacity);


        mtexView_health = findViewById(R.id.textView_health);
        mtextView_temp = findViewById(R.id.textView_temp);
        mTextView_volt = findViewById(R.id.textView_volt);

        mHealth_image_view = findViewById(R.id.imageView_health);

        mBatteryProgress = findViewById(R.id.main_battery_progress_bar);
        mBatteryCapacity = findViewById(R.id.progressBar_batterycapacity);

        mAlertMe = findViewById(R.id.cardView_alertme);


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBatteryBroadCast,mIntentFilter);
        viewBinding();
        UpdateUI();
    }

     void UpdateUI(){
        mtextview_battary_level.setText(current_battery_level);
        mtexView_health.setText(battery_health);
        mtextView_temp.setText(battery_temp);
        mTextView_volt.setText(battery_volt);
        mBatteryProgress.setProgress(progress_status);

        if(isCharging)mtextView_img_bat_level.setImageResource(R.drawable.ic_device_is_charging);
        else mtextView_img_bat_level.setImageResource(R.drawable.ic_battery_discharging);
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";
        mHealth_image_view.setImageResource(R.drawable.ic_healthunknown);
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                mHealth_image_view.setImageResource(R.drawable.ic_healthdead);
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good ";
                mHealth_image_view.setImageResource(R.drawable.ic_healthgood);
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Volt";
                mHealth_image_view.setImageResource(R.drawable.ic_healthovervoltage);
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                mHealth_image_view.setImageResource(R.drawable.ic_healthoverheated);
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                mHealth_image_view.setImageResource(R.drawable.ic_healthfailed);
                break;
        }
        return healthString;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.key_set_alarm))){
            mUtils.setAlarmOn(sharedPreferences.getBoolean(key,false));
        }else if(key.equals(getString(R.string.key_Set_Alarm_max))){
            mUtils.setAlarm_max(sharedPreferences.getInt(key,80));
        }

        if(mUtils.isAlarmOn()){
            stopService(new Intent(MainActivity.this, ForegroundService.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(MainActivity.this, ForegroundService.class));
            }
            else
                startService(new Intent(MainActivity.this, ForegroundService.class));
        }else {
            stopService(new Intent(MainActivity.this, ForegroundService.class));
        }
    }

    private void setUpPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    public  class BatteryBroadCast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            viewBinding();
            UpdateUI();

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
       unregisterReceiver(mBatteryBroadCast);
    }
    public double getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double batteryCapacity = -1;

        try {
            batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity;
    }

}