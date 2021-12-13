package com.example.blitzbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    public static User loggedInUser = null;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();

    SharedPreferences sp;
    int loggedIn = 0;
    EditText emailTextView;
    EditText passwordTextView;
    TextView feedback;
    String userEmail;

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
        setContentView(R.layout.activity_login);

        loggedIn = sp.getInt("loggedIn", 0);
        userEmail = sp.getString("userEmail", "");
        emailTextView = (EditText) findViewById (R.id.loginEmail);
        passwordTextView = (EditText) findViewById (R.id.loginPassword);
        feedback = (TextView) findViewById(R.id.loginfeedback);
        feedback.setText("");

        if (loggedIn == 1 && userEmail != ""){
            Context context = getApplicationContext();

            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            User user = dbHelper.getUser(userEmail);

            sqLiteDatabase.close();
            loggedInUser = user;

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void gotoCreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void userLogin(View v){
        setFeedback("");
        String userEmail = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
        User user = fireBaseHelper.getUser(userEmail);
        if (!fireBaseHelper.checkUser(userEmail) && !user.getPassword().equals(password)) {
            setFeedback("User not found");
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("loggedIn", 1).apply();
            editor.putString("userEmail", userEmail).apply();
            loggedInUser = user;
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }


        /*Context context = getApplicationContext();

        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        User user = dbHelper.getUser(userEmail);

        sqLiteDatabase.close();
        if (user != null) {
            String pwd = null;

            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("loggedIn", 1).apply();
            editor.putString("userEmail", userEmail).apply();
            loggedInUser = user;

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }else{
            setFeedback("User not found");
        }
         */
    }

    public void setFeedback(String msg){
        feedback.setText(msg);
    }

}