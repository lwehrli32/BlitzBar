//package com.example.blitzbar;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//
//import android.annotation.SuppressLint;
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.Date;
//import java.util.regex.Pattern;
//
//public class CreateAccountActivity extends AppCompatActivity {
//    SharedPreferences sp;
//    int loggedIn = 0;
//    String userEmail;
//    EditText firstNameEditText;
//    EditText lastNameEditText;
//    EditText birthdayEditText;
//    EditText emailEditText;
//    EditText passwordEditText;
//    Button registerButton;
//    TextView feedback;
//    boolean isDrinkingAge = false;
//    Calendar cal = Calendar.getInstance();
//    int cyear = cal.get(Calendar.YEAR);
//    int cmonth = cal.get(Calendar.MONTH);
//    int cday = cal.get(Calendar.DAY_OF_MONTH);
//    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();
//    FusedLocationProviderClient fusedLocationProviderClient;
//    FirebaseDatabase database;
//    DatabaseReference mDatabase;
//    FirebaseAuth mAuth;
//    static final String USER = "user";
//
//
//    static final String TAG = "CreateAccountActivity";
//    User user;
//
//    private DatePickerDialog.OnDateSetListener mDateSetListener;
//    int dialogTheme;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);
//
//        boolean isDarkMode = sp.getInt("isDarkMode", 0) == 1;
//        if (isDarkMode) {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            int dialogTheme = AppCompatDelegate.MODE_NIGHT_YES;
//        } else {
//            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            int dialogTheme = AppCompatDelegate.MODE_NIGHT_NO;
//        }
//
//        if (savedInstanceState != null) {
//            Log.i("STATE", savedInstanceState.toString());
//        }
//
//        setContentView(R.layout.activity_create_account);
//
//        //setFeedback("");
//
//        registerButton = findViewById(R.id.createAccountButton);
//
//        database = FirebaseDatabase.getInstance();
//        mDatabase = database.getReference(USER);
//        mAuth = FirebaseAuth.getInstance();
//
//        if (loggedIn == 1 && userEmail != "") {
//            Context context = getApplicationContext();
//
//            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
//            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
//            User user = dbHelper.getUser(userEmail);
//
//            sqLiteDatabase.close();
//            LoginActivity.loggedInUser = user;
//
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//        }
//
////        birthdayEditText.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                DatePickerDialog dialog = new DatePickerDialog(
////                        CreateAccountActivity.this,
////                        dialogTheme,
////                        mDateSetListener,
////                        cyear, cmonth, cday);
////                if (isDarkMode) {
////                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
////                } else {
////                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
////                }
////                dialog.show();
////            }
////        });
//
//        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int uyear, int umonth, int uday) {
//                umonth++;
//                cmonth++;
//                String udateStr = umonth + "/" + uday + "/" + uyear;
//                String cdateStr = cmonth + "/" + cday + "/" + cyear;
//                birthdayEditText.setText(udateStr);
//                Log.i("Birthday Entered:", umonth + "/ " + uday + "/ " + uyear);
//                Log.i("Current Date:", cmonth + "/ " + cday + "/ " + cyear);
//                if (uyear + 22 <= cyear) {
//                    isDrinkingAge = true;
//                } else if (uyear + 21 == cyear) {
//                    if (umonth < cmonth) {
//                        isDrinkingAge = true;
//                    } else if (umonth == cmonth) {
//                        if (uday <= cday) {
//                            isDrinkingAge = true;
//                        }
//                    }
//                }
//            }
//        };
//
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String firstName = firstNameEditText.getText().toString();
//                String lastName = lastNameEditText.getText().toString();
//                String birthday = birthdayEditText.getText().toString();
//                String userEmail = emailEditText.getText().toString();
//                String userPassword = passwordEditText.getText().toString();
//                boolean goodInput = true;
//                long blitzScore = 0;
//                String fav_bar = "";
//                String fav_drink = "";
//
//                if (firstName.equals("")){
//                    setFeedback("First name is required");
//                    goodInput = false;
//                }else if(lastName.equals("")){
//                    setFeedback("Last name is required");
//                    goodInput = false;
//                }else if(birthday.equals("")){
//                    setFeedback("Birthday is required");
//                    goodInput = false;
//                }else if(userEmail.equals("")){
//                    setFeedback("Email is required");
//                    goodInput = false;
//                }else if(!isValid(userEmail)){
//                    setFeedback("Email is not valid");
//                    goodInput = false;
//                } else if(userPassword.equals("")){
//                    setFeedback("Password is required");
//                    goodInput = false;
//                } else if(!isDrinkingAge) {
//                    setFeedback("BlitzBar is only available to those age 21 years old or older.");
//                    goodInput = false;
//                }
//
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_LONG).show();
//                    return;
//                }
////                String firstName = firstNameEditText.getText().toString();
////                String lastName = lastNameEditText.getText().toString();
////                String birthday = birthdayEditText.getText().toString();
////                long blitzScore = 0;
//                long longitude = 0;
//                long latitude = 0;
//                User user = new User(firstName, lastName, email, birthday, blitzScore, longitude, latitude, password);
//                registerUser(email, password);
//            }
//        });
//    }
//
//    public void registerUser(String email, String password) {
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete (@NonNull Task< AuthResult > task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                            }
//
//                            // ...
//                        }
//        });
//    }
//
//    public void updateUI(FirebaseUser current) {
//        String keyId = mDatabase.push().getKey();
//        mDatabase.child(keyId).setValue(user);
//        Intent loginIntent = new Intent(this, MapsActivity.class);
//        startActivity(loginIntent);
//    }
//
////    public void gotoLogin(View view){
////        Intent intent = new Intent(this, LoginActivity.class);
////        startActivity(intent);
////    }
//
////    public void userCreateAccount(View v){
////        setFeedback("");
////        String firstName = firstNameEditText.getText().toString();
////        String lastName = lastNameEditText.getText().toString();
////        String birthday = birthdayEditText.getText().toString();
////        String userEmail = emailEditText.getText().toString();
////        String userPassword = passwordEditText.getText().toString();
////        boolean goodInput = true;
////        long blitzScore = 0;
////        String fav_bar = "";
////        String fav_drink = "";
////
////        if (firstName.equals("")){
////            setFeedback("First name is required");
////            goodInput = false;
////        }else if(lastName.equals("")){
////            setFeedback("Last name is required");
////            goodInput = false;
////        }else if(birthday.equals("")){
////            setFeedback("Birthday is required");
////            goodInput = false;
////        }else if(userEmail.equals("")){
////            setFeedback("Email is required");
////            goodInput = false;
////        }else if(!isValid(userEmail)){
////            setFeedback("Email is not valid");
////            goodInput = false;
////        } else if(userPassword.equals("")){
////            setFeedback("Password is required");
////            goodInput = false;
////        } else if(!isDrinkingAge) {
////            setFeedback("BlitzBar is only available to those age 21 years old or older.");
////            goodInput = false;
////        }
////
////        if(goodInput) {
////            /*Context context = getApplicationContext();
////            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
////            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
////
////            // TODO firebase helper returns user when inserted and then set loggedinuser to the user returned
////            boolean userCreated = dbHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, fav_drink, fav_bar);
////
////             */
////            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////            long longitude = 0;
////            long latitude = 0;
////            FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
////
////            boolean userCreated = fireBaseHelper.checkUser(userEmail);
////            if (userCreated) {
////                setFeedback("Email is already taken");
////            } else {
////                fireBaseHelper.insertUser(firstName, lastName, userEmail, birthday, blitzScore, longitude, latitude, userPassword);
////                Intent intent = new Intent(this, MapsActivity.class);
////                startActivity(intent);
////            }
////            //sqLiteDatabase.close();
////
////            /*if (userCreated) {
////                SharedPreferences.Editor editor = sp.edit();
////
////                editor.putInt("loggedIn", 1).apply();
////                editor.putString("userEmail", userEmail).apply();
////
////                // TODO set this to the returned user from creating a new user
////                LoginActivity.loggedInUser = null;
////
////                // give blitzbar score for creating an account
////                LoginActivity.loggedInUser.incrementBlitzBar();
////
////                Intent intent = new Intent(this, MapsActivity.class);
////                startActivity(intent);
////            } else {
////                setFeedback("Email is already taken");
////            }
////             */
////        }
////    }
//
//    public void setFeedback(String msg){
//        feedback.setText(msg);
//    }
//
//    public static boolean isValid(String email){
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
//                "[a-zA-Z0-9_+&*-]+)*@" +
//                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
//                "A-Z]{2,7}$";
//
//        Pattern pat = Pattern.compile(emailRegex);
//        if (email == null)
//            return false;
//        return pat.matcher(email).matches();
//    }
//}


package com.example.blitzbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, workEditText, passwordEditText;
    private EditText phoneEditText, emailEditText;
    private EditText birthdayEditText;
    private ImageView picImageView;
    private CheckBox maleCheckBox, femaleCheckBox;
    private Button registerButton;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "users";
    private String TAG = "RegisterActivity";
    private String firstName, lastName, email, birthday;
    private String password;
    private User user;
    private FirebaseAuth mAuth;
    SharedPreferences sp;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);

        boolean isDarkMode = sp.getInt("isDarkMode", 0) == 1;
        if (isDarkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            int dialogTheme = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            int dialogTheme = AppCompatDelegate.MODE_NIGHT_NO;
        }
        setContentView(R.layout.activity_create_account);

        firstNameEditText = findViewById(R.id.createAccountFirstName);
        lastNameEditText = findViewById(R.id.createAccountLastName);
       // workEditText = findViewById(R.id.workplace_edittext);
      // phoneEditText = findViewById(R.id.phone_edittext);
        passwordEditText = findViewById(R.id.createAccountPassword);
        emailEditText = findViewById(R.id.createAccountEmail);
        birthdayEditText = findViewById(R.id.createAccountBirthday);
       // picImageView = findViewById(R.id.pic_imageview);
       // maleCheckBox = findViewById(R.id.male_checkbox);
       // femaleCheckBox = findViewById(R.id.female_checkbox);
        registerButton = findViewById(R.id.createAccountButton);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USERS);
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginCreateAccount);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //insert data into firebase database
                if(emailEditText.getText().toString() != null && passwordEditText.getText().toString() != null) {
                    firstName = firstNameEditText.getText().toString();
                    lastName = lastNameEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    birthday = birthdayEditText.getText().toString();
                   // phone = phoneEditText.getText().toString();
                   // profession = professionEditText.getText().toString();
                   // workplace = workEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    long num = 0;
                    user = new User(firstName, lastName, email, birthday, num, num, num, password);
                    registerUser();
                }
            }
        });

//        loginButton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                Intent loginIntent = new Intent (CreateAccountActivity.this, LoginActivity.class);
//                startActivity(loginIntent);
//            }
//        });

//           loginButton.setOnClickListener(new View.onClickListener(){
//           public void onClick(View v) {
//
//           }
//                                          });
//            @Override
//            public void onClick(View view) {
//                Intent loginIntent = new Intent(this, LoginActivity.class);
//                startActivity(loginIntent);
//            }
//        });

    }

    public void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * adding user information to database and redirect to login screen
     * @param currentUser
     */
    public void updateUI(FirebaseUser currentUser) {
        SharedPreferences.Editor editor = sp.edit();
        String keyid = mDatabase.push().getKey();
        editor.putInt("loggedIn", 1).apply();
        editor.putString("userEmail", user.getEmail()).apply();
        mDatabase.child(keyid).setValue(user); //adding user info to database
        Intent loginIntent = new Intent(this, MapsActivity.class);
        startActivity(loginIntent);
    }

    public void gotoLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
