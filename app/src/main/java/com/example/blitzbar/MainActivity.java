package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    int loggedin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // see if the user is logged in
        sharedPref = getApplicationContext().getSharedPreferences("com.example.blitzbar", Context.MODE_PRIVATE);
        loggedin = sharedPref.getInt("loggedin", 0);

        if (loggedin == 0){
            // if the user is not logged in
            gotoLoginActivity();
        }

        // goto map page
        setContentView(R.layout.activity_main);
    }

    public void gotoLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}