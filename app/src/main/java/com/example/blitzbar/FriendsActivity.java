package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        NavigationBarView bottomNavigationBarView = findViewById(R.id.bottomnav);
        bottomNavigationBarView.setOnItemSelectedListener(bottomnavFunction);
    }

    private final NavigationBarView.OnItemSelectedListener bottomnavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent menu_intent = new Intent();
            switch (item.getItemId()) {
                case R.id.settings:
                    menu_intent = new Intent(FriendsActivity.this, SettingsActivity.class);
                    break;
                case R.id.listView:
                    menu_intent = new Intent(FriendsActivity.this, ListActivity.class);
                    break;
                case R.id.mapView:
                    menu_intent = new Intent(FriendsActivity.this, MapsActivity.class);
                    break;
                default:
                    return false;
            }


            startActivity(menu_intent);
            return true;
        }
    };
}