package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;
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
    public void goToLastActivity() {

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
        boolean mute = sp.getBoolean("sounds", false);
        mute = !mute;
        sp.edit().putBoolean("sounds", mute).apply();
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(mute) {
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        } else {
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        }
    }

    private void toggleNotifications() {
        boolean notifications = sp.getBoolean("notifications", false);
        notifications = !notifications;
        sp.edit().putBoolean("notifications", notifications).apply();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.areNotificationsEnabled(notifications);
    }
    //TODO CREATE LOCATIONPUBLIC TOGGLE
    private void toggleLocationPublic() {

    }

    private void switchState() {
        swDarkMode = findViewById(R.id.DarkMode);
        swSounds = findViewById(R.id.Sounds);
        swNotifications = findViewById(R.id.Notifications);
        swLocationPublic = findViewById(R.id.LocationPublic);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", false));
        swNotifications.setChecked(sp.getBoolean("notifications", false));
    }

    private void switchStateDark() {
        swDarkMode = findViewById(R.id.DarkModeDark);
        swSounds = findViewById(R.id.SoundsDark);
        swNotifications = findViewById(R.id.NotificationsDark);
        swLocationPublic = findViewById(R.id.LocationPublicDark);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", false));
        swNotifications.setChecked(sp.getBoolean("notifications", false));
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