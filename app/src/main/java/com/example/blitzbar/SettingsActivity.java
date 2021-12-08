package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sp;
    SwitchCompat swDarkMode;
    SwitchCompat swSounds;
    SwitchCompat swNotifications;
    SwitchCompat swLocationPublic;
    TextView userName;
    ImageView profileImage;

    public void onClick(View v) {
        if(v.getId() == R.id.backButton || v.getId() == R.id.backButtonDark) {
            goToLastActivity();
        }
    }

    public void onSwitch(View v) {
        if(v.getId() == R.id.DarkMode || v.getId() == R.id.DarkModeDark) {
            toggleDarkMode();
            restartActivity();
        } else if (v.getId() == R.id.Sounds || v.getId() == R.id.SoundsDark) {
            toggleMute();
        } else if (v.getId() == R.id.Notifications || v.getId() == R.id.NotificationsDark) {
            toggleNotifications();
        } else if (v.getId() == R.id.LocationPublic || v.getId() == R.id.LocationPublicDark) {
            toggleLocationPublic();
        }
    }

    //TODO return to the previous default screen
    private void goToLastActivity() {
        super.onBackPressed();
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
        boolean mute = sp.getBoolean("sounds", true);
        mute = !mute;
        sp.edit().putBoolean("sounds", mute).apply();
    }

    //TODO BLOCK NOTIFICATIONS WHEN notifications is false
    private void toggleNotifications() {
        boolean notifications = sp.getBoolean("notifications", true);
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

        if (sp.getBoolean("darkMode", false)) {
            setContentView(R.layout.activity_settings_dark);
            userName = (TextView) findViewById(R.id.userNameDark);
            profileImage = (ImageView) findViewById(R.id.profileImageDark);
            switchStateDark();
        } else {
            setContentView(R.layout.activity_settings);
            userName = (TextView) findViewById(R.id.userName);
            profileImage = (ImageView) findViewById(R.id.profileImage);
            switchState();
        }

        String userEmail = sp.getString("userEmail", "");
        //String image = sp.getString("image", "");

        if (userEmail != "") {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            User user = dbHelper.getUser(userEmail);

            sqLiteDatabase.close();

            userName.setText(user.getFirst_name() + " " + user.getLast_name());

            //if (image == ""){
                // set to default image
                //profileImage.setImageResource();
            //}
        }else{
            System.out.println("No user");
        }
    }
}