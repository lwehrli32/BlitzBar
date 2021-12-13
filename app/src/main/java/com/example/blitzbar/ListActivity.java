package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationBarView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    SharedPreferences sp;
    private NavigationBarView bottomNavigationBarView;
    private GoogleMap mMap;
    LocationRequest request;
    GoogleApi client;
    LatLng latLngCurrent;
    ScrollView scrollView;
    TextView textView;
    public ListActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);
        boolean isDarkMode = sp.getInt("isDarkMode", 0) == 1;
        if (isDarkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        bottomNavigationBarView = findViewById(R.id.bottomnav);
        bottomNavigationBarView.setOnItemSelectedListener(bottomnavFunction);

        ScrollView scroll = (ScrollView) this.findViewById(R.id.scrollView);

//        TextView bar1 = new TextView(this);
//        bar1.setText("Sconniebar");

//        scroll.addView(bar1);
        addBarList(scroll);
       // listBars(scroll);
    }

//    public void listBars(View v) {
//        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        stringBuilder.append("location=43.0676,-89.4037");
//        stringBuilder.append("&radius="+1000);
//        stringBuilder.append("&keyword="+"bar");
//        stringBuilder.append("&key="+getResources().getString(R.string.google_places_key));
//
//        String url = stringBuilder.toString();
//
//        Object dataTransfer[] = new Object[3];
//        dataTransfer[0] = scrollView;
//        dataTransfer[1] = url;
//        dataTransfer[2] = textView;
//
//        GetNearbyBars getNearbyBars = new GetNearbyBars();
//        getNearbyBars.execute(dataTransfer);
//    }

    public void onAttach(Activity activity) {
        this.activity = (ListActivity) activity;
    }

    public void addBarList(View v) {
        ScrollView scroll = (ScrollView) this.findViewById(R.id.scrollView);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout);

        TextView bar1 = new TextView(this);
        bar1.setText("Sconniebar");

        TextView bar2 = new TextView(this);
        bar2.setText("Red Shed");

        TextView bar3 = new TextView(this);
        bar3.setText("Whiskey Jack's");

        TextView bar4 = new TextView(this);
        bar4.setText("Monday's");

        TextView bar5 = new TextView(this);
        bar5.setText("Lucky's");

        TextView bar6 = new TextView(this);
        bar6.setText("State Street Brats");

        TextView bar7 = new TextView(this);
        bar7.setText("Jordan's Big 10");

        TextView bar8 = new TextView(this);
        bar8.setText("The Library");

        TextView bar9 = new TextView(this);
        bar9.setText("Wando's");

        TextView bar10 = new TextView(this);
        bar10.setText("The Redzone");

        TextView bar11 = new TextView(this);
        bar11.setText("Buckingham's");

        TextView bar12 = new TextView(this);
        bar12.setText("Double U");

        TextView bar13 = new TextView(this);
        bar13.setText("Church Key");

        TextView bar14 = new TextView(this);
        bar14.setText("City Bar");

        TextView bar15 = new TextView(this);
        bar15.setText("The Kollege Klub");

        TextView bar16 = new TextView(this);
        bar16.setText("Nitty Gritty");

        TextView bar17 = new TextView(this);
        bar17.setText("Chasers 2.0");

        linearLayout.addView(bar1);
        linearLayout.addView(bar2);
        linearLayout.addView(bar3);
        linearLayout.addView(bar4);
        linearLayout.addView(bar5);
        linearLayout.addView(bar6);
        linearLayout.addView(bar7);
        linearLayout.addView(bar8);
        linearLayout.addView(bar9);
        linearLayout.addView(bar10);
        linearLayout.addView(bar11);
        linearLayout.addView(bar12);
        linearLayout.addView(bar13);
        linearLayout.addView(bar14);
        linearLayout.addView(bar15);
        linearLayout.addView(bar16);
        linearLayout.addView(bar17);

      //  scroll.addView(linearLayout);


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

            startActivity(menu_intent);
            return true;
        }
    };


}