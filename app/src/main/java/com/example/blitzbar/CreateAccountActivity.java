package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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
            Context context = getApplicationContext();

            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE,null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
            User user = dbHelper.getUser(userEmail);

            sqLiteDatabase.close();
            LoginActivity.loggedInUser = user;

            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public void gotoLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean verify_birthday(String birthday){
        String[] date = birthday.split("/");

        if (date.length != 3)
            return false;

        int day;
        int month;
        int year;

        try{
            month = Integer.parseInt(date[0]);
            day = Integer.parseInt(date[1]);
            year = Integer.parseInt(date[2]);
        }catch(Exception e){
            return false;
        }

        if (month <= 0 || month > 12)
            return false;
        if (day > 31 || day < 1)
            return false;
        if (date[2].length() != 4)
            return false;

        // check for leap year day
        if (month == 2 && day == 29 && year % 4 != 0)
            return false;

        if (month == 2 && day > 28){
            return false;
        }

        // check end of months
        if (month == 4 || month == 6 || month == 9 || month == 11){
            if(day > 30){
                return false;
            }
        }

        return true;
    }

    public boolean verify_age(String birthday){
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String localDate = simpleDateFormat.format(new Date());

        String[] currdate = localDate.split("/");
        String[] birdate = birthday.split("/");

        int curryear;
        int currday;
        int currmonth;
        int biryear;
        int birmonth;
        int birday;

        try{
            curryear = Integer.parseInt(currdate[2]);
            currday = Integer.parseInt(currdate[1]);
            currmonth = Integer.parseInt(currdate[0]);
            biryear = Integer.parseInt(birdate[2]);
            birday = Integer.parseInt(birdate[1]);
            birmonth = Integer.parseInt(birdate[0]);
        }catch(Exception e){
            return false;
        }

        if (curryear - biryear < 0){
            return false;
        }else {
            if (currmonth - birmonth < 0) {
                return false;
            } else {
                if (currday - birday < 0) {
                    return false;
                }
                return true;
            }
        }
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
        }else if (!verify_birthday(birthday)){
            goodInput = false;
            setFeedback("Invalid date");
        }else if (!verify_age(birthday)){
            goodInput = false;
            setFeedback("Must be 21 years old");
        }

        if(goodInput) {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            // TODO firebase helper returns user when inserted and then set loggedinuser to the user returned
            boolean userCreated = dbHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, fav_drink, fav_bar);

            sqLiteDatabase.close();

            if (userCreated) {
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("loggedIn", 1).apply();
                editor.putString("userEmail", userEmail).apply();

                // TODO set this to the returned user from creating a new user
                LoginActivity.loggedInUser = null;

                // give blitzbar score for creating an account
                LoginActivity.loggedInUser.incrementBlitzBar();

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