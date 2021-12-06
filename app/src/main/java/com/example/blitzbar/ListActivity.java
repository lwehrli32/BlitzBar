package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class ListActivity extends AppCompatActivity {

    private NavigationBarView bottomNavigationBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bottomNavigationBarView = findViewById(R.id.bottomnav);
        bottomNavigationBarView.setOnItemSelectedListener(bottomnavFunction);
    }

    private NavigationBarView.OnItemSelectedListener bottomnavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent menu_intent = new Intent();
            switch (item.getItemId()) {
                case R.id.settings:
                    menu_intent = new Intent(ListActivity.this, SettingsActivity.class);
                    break;
                case R.id.friends:
                    menu_intent = new Intent(ListActivity.this, FriendsActivity.class);
                    break;
                case R.id.mapView:
                    menu_intent = new Intent(ListActivity.this, MapsActivity.class);
                    break;
                default:
                    return false;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            startActivity(menu_intent);
            return true;
        }
    };


}