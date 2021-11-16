package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;

    // TODO CREATE METHODS FOR SWITCHES AND BUTTONS

    public void onClick(View v) {
        if(v.getId() == R.id.backButton) {
            goToLastActivity();
        }
    }

    public void onSwitch(View v) {
        switch (v.getId()) {
            case R.id.DarkMode:
                toggleDarkMode();
                break;
            case R.id.Sounds:
                // TODO toggle sounds
                break;
            case R.id.Notifications:
                // TODO toggle notifications
                break;
            case R.id.LocationPublic:
                // TODO toggle publicized location
                break;
        }
    }

    public void goToLastActivity() {

    }

    public void toggleDarkMode() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);

        // TODO REMOVE THIS CODE
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("loggedIn", 0).apply();
    }
}