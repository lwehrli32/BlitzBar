package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

public class ListActivity extends AppCompatActivity {

    private NavigationBarView bottomNavigationBarView;
    private GoogleMap mMap;
    LocationRequest request;
    GoogleApi client;
    LatLng latLngCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bottomNavigationBarView = findViewById(R.id.bottomnav);
        bottomNavigationBarView.setOnItemSelectedListener(bottomnavFunction);

        ScrollView scroll = (ScrollView) this.findViewById(R.id.scrollView1);
    }

    public void listBars(View v) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location="+latLngCurrent.latitude + "," + latLngCurrent.longitude);
        stringBuilder.append("&radius="+1000);
        stringBuilder.append("&keyword="+"bar");
        stringBuilder.append("&key="+getResources().getString(R.string.google_places_key));

        String url = stringBuilder.toString();

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyBars getNearbyBars = new GetNearbyBars();
        getNearbyBars.execute(dataTransfer);
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