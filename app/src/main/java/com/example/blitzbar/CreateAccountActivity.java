package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    boolean isDrinkingAge = false;
    Calendar cal = Calendar.getInstance();
    int cyear = cal.get(Calendar.YEAR);
    int cmonth = cal.get(Calendar.MONTH);
    int cday = cal.get(Calendar.DAY_OF_MONTH);
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "CreateAccountActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int dialogTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);

        boolean isDarkMode = sp.getInt("isDarkMode", 0) == 1;
        if (isDarkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            int dialogTheme = AppCompatDelegate.MODE_NIGHT_YES;
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            int dialogTheme = AppCompatDelegate.MODE_NIGHT_NO;
        }

        if(savedInstanceState != null) {
            Log.i("STATE", savedInstanceState.toString());
        }

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

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatePickerDialog dialog = new DatePickerDialog(
                        CreateAccountActivity.this,
                        dialogTheme,
                        mDateSetListener,
                        cyear, cmonth, cday);
                if (isDarkMode) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                } else {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int uyear, int umonth, int uday) {
                umonth++;
                cmonth++;
                String udateStr = umonth + "/" + uday + "/" + uyear;
                String cdateStr = cmonth + "/" + cday + "/" + cyear;
                birthdayEditText.setText(udateStr);
                Log.i("Birthday Entered:", umonth + "/ " + uday + "/ " + uyear);
                Log.i("Current Date:", cmonth + "/ " + cday + "/ " + cyear);
                if (uyear + 22 <= cyear) {
                    isDrinkingAge = true;
                } else if (uyear + 21 == cyear) {
                    if (umonth < cmonth) {
                        isDrinkingAge = true;
                    } else if (umonth == cmonth) {
                        if (uday <= cday) {
                            isDrinkingAge = true;
                        }
                    }
                }
            }
        };


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
        long blitzScore = 0;
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
        } else if(!isDrinkingAge) {
            setFeedback("BlitzBar is only available to those age 21 years old or older.");
            goodInput = false;
        }

        if(goodInput) {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            // TODO firebase helper returns user when inserted and then set loggedinuser to the user returned
            boolean userCreated = dbHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, fav_drink, fav_bar);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            long longitude = 0;
            long latitude = 0;
            FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
            if (!fireBaseHelper.checkUser(userEmail)) {
                fireBaseHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, longitude, latitude);
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            } else {
                setFeedback("Email is already taken");
            }
            sqLiteDatabase.close();

            /*if (userCreated) {
                SharedPreferences.Editor editor = sp.edit();

                editor.putInt("loggedIn", 1).apply();
                editor.putString("userEmail", userEmail).apply();

                // TODO set this to the returned user from creating a new user
                LoginActivity.loggedInUser = null;

                // give blitzbar score for creating an account
                assert LoginActivity.loggedInUser != null;
                LoginActivity.loggedInUser.incrementBlitzBar();

                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            } else {
                setFeedback("Email is already taken");
            }
            */
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