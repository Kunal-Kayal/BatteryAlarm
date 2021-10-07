package com.BatteryAlarm.android.batteryalarm;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;

import org.jetbrains.annotations.Nullable;

public class CustomAppIntro extends AppIntro2 {

    Intent mMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorTransitionsEnabled(true);
        setImmersive(true);
        showStatusBar(true);
        mMainActivity = new Intent(this,MainActivity.class);
        addSlide(AppIntroFragment.newInstance(
                "Battery Alarm",
                "Save your battery health with science",
                 R.drawable.ic_img_sliderone,
                 Color.parseColor(getString(R.string.slider_background_color_1)),
                 Color.BLUE,
                 Color.BLACK

        ));
        addSlide(AppIntroFragment.newInstance(
                "Battery Alarm is Something Cool",
                "We only show you actual battery data",
                R.drawable.ic_img_slidertwo,
                Color.parseColor(getString(R.string.slider_background_color_2)),
                Color.BLUE,
                Color.BLACK

        ));

        addSlide(AppIntroFragment.newInstance(
                "Keep your battary safe",
                "Extened your battary life with 80% charging",
                R.drawable.ic_image_sliderthree,
                Color.parseColor("#ffaf49"),
                Color.BLUE,
                Color.BLACK

        ));
        addSlide(AppIntroFragment.newInstance(
                "Try Our Alarm System",
                "Set Alarm for your Battery when charging",
                R.drawable.ic_img_sliderfour,
                Color.parseColor("#df55f2"),
                Color.BLUE,
                Color.BLACK

        ));
        addSlide(AppIntroFragment.newInstance(
                "Don't need to give any permission",
                "This app is completely safe, It doesn't require permission",
                R.drawable.ic_img_sliderfive,
                Color.parseColor("#ffaf49"),
                Color.BLUE,
                Color.BLACK

        ));


    }


    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(mMainActivity);
        finish();

    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(mMainActivity);
        finish();
    }
}