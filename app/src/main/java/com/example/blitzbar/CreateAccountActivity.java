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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {
    SharedPreferences sp;
    int loggedIn = 0;
    String userEmail;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText birthdayEditText;
    EditText emailEditText;
    EditText passwordEditText;
    TextView feedback;

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
        setContentView(R.layout.activity_create_account);

        loggedIn = sp.getInt("loggedIn", 0);
        userEmail = sp.getString("userEmail", "");
        firstNameEditText = (EditText) findViewById(R.id.createAccountFirstName);
        lastNameEditText = (EditText) findViewById(R.id.createAccountLastName);
        birthdayEditText = (EditText) findViewById(R.id.createAccountBirthday);
        emailEditText = (EditText) findViewById(R.id.createAccountEmail);
        passwordEditText = (EditText) findViewById(R.id.createAccountPassword);
        feedback = (TextView) findViewById(R.id.createAccountFeedback);
        feedback.setText("");

        if(loggedIn == 1 && userEmail != ""){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void gotoLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void userCreateAccount(View v){
        setFeedback("");
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String birthday = birthdayEditText.getText().toString();
        String userEmail = emailEditText.getText().toString();
        String userPassword = passwordEditText.getText().toString();
        boolean goodInput = true;
        String blitzScore = "0";
        String fav_bar = "";
        String fav_drink = "";

        if (firstName.equals("")){
            setFeedback("First name is required");
            goodInput = false;
        }else if(lastName.equals("")){
            setFeedback("Last name is required");
            goodInput = false;
        }else if(birthday.equals("")){
            setFeedback("Birthday is required");
            goodInput = false;
        }else if(userEmail.equals("")){
            setFeedback("Email is required");
            goodInput = false;
        }else if(!isValid(userEmail)){
            setFeedback("Email is not valid");
            goodInput = false;
        } else if(userPassword.equals("")){
            setFeedback("Password is required");
            goodInput = false;
        }

        if(goodInput) {

            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            boolean userCreated = dbHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, fav_drink, fav_bar);

            sqLiteDatabase.close();

            if (userCreated) {
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("loggedIn", 1).apply();
                editor.putString("userEmail", userEmail).apply();

                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            } else {
                setFeedback("Email is already taken");
            }
        }
    }

    public void setFeedback(String msg){
        feedback.setText(msg);
    }

    public static boolean isValid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}