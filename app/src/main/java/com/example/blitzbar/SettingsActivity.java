package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;
    private boolean notifications;
    private boolean mute;
    Switch swDarkMode;
    Switch swSounds;
    Switch swNotifications;
    Switch swLocationPublic;

    public void onClick(View v) {
        if(v.getId() == R.id.backButton) {
            goToLastActivity();
        }
    }

    public void onSwitch(View v) {
        switch (v.getId()) {
            case R.id.DarkMode:
            case R.id.DarkModeDark:
                toggleDarkMode();
                restartActivity();
                break;

            case R.id.Sounds:
            case R.id.SoundsDark:
                toggleMute();
                break;

            case R.id.Notifications:
            case R.id.NotificationsDark:
                toggleNotifications();
                break;

            case R.id.LocationPublic:
            case R.id.LocationPublicDark:
                toggleLocationPublic();
                break;
        }
    }

    //TODO CREATE BACK BUTTON FUNCTIONALITY
    private void goToLastActivity() {

    }

    private void restartActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void toggleDarkMode() {
        boolean darkMode = sp.getBoolean("darkMode", false);
        darkMode = !darkMode;
        sp.edit().putBoolean("darkMode", darkMode).apply();
    }

    private void toggleMute() {
        mute = sp.getBoolean("sounds", true);
        mute = !mute;
        sp.edit().putBoolean("sounds", mute).apply();
    }

    //TODO BLOCK NOTIFICATIONS WHEN notifications is false
    private void toggleNotifications() {
        notifications = sp.getBoolean("notifications", true);
        notifications = !notifications;
        sp.edit().putBoolean("notifications", notifications).apply();
    }

    //TODO Figure out what we want LocationPublic to exactly do
    private void toggleLocationPublic() {
        boolean locationPublic = sp.getBoolean("locationPublic", true);
        locationPublic = !locationPublic;
        sp.edit().putBoolean("locationPublic", locationPublic).apply();
    }

    private void switchState() {
        swDarkMode = findViewById(R.id.DarkMode);
        swSounds = findViewById(R.id.Sounds);
        swNotifications = findViewById(R.id.Notifications);
        swLocationPublic = findViewById(R.id.LocationPublic);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", true));
        swNotifications.setChecked(sp.getBoolean("notifications", true));
        swLocationPublic.setChecked(sp.getBoolean("locationPublic", true));
    }

    private void switchStateDark() {
        swDarkMode = findViewById(R.id.DarkModeDark);
        swSounds = findViewById(R.id.SoundsDark);
        swNotifications = findViewById(R.id.NotificationsDark);
        swLocationPublic = findViewById(R.id.LocationPublicDark);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", true));
        swNotifications.setChecked(sp.getBoolean("notifications", true));
        swLocationPublic.setChecked(sp.getBoolean("locationPublic", true));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);
        if(sp.getBoolean("darkMode", false)) {
            setContentView(R.layout.activity_settings_dark);
            switchStateDark();

        } else {
            setContentView(R.layout.activity_settings);
            switchState();
        }

        // TODO REMOVE THIS CODE
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("loggedIn", 0).apply();
    }
}