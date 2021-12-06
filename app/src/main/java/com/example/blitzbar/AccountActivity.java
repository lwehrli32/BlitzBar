package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    ImageView profileImage;
    TextView userName;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String userEmail = sp.getString("userEmail", "");

        if (userEmail != "") {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            User user = dbHelper.getUser(userEmail);

            userName.setText(user.getFirst_name() + " " + user.getLast_name());

            sqLiteDatabase.close();
        }else{
            System.out.println("No user");
        }
    }

    public void goBack(View view){

    }
}