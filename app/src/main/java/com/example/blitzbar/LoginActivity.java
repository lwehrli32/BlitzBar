package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;

public class LoginActivity extends AppCompatActivity {
    public static User loggedInUser = null;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private String TAG = "LoginActivity";
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

        emailTextView=findViewById(R.id.loginEmail);
        passwordTextView=findViewById(R.id.loginPassword);
        Button btn_login = findViewById(R.id.loginButton);
        Button btn_sign = findViewById(R.id.loginCreateAccount);
        mAuth=FirebaseAuth.getInstance();
        btn_login.setOnClickListener(v -> {
            String email= emailTextView.getText().toString().trim();
            String password= passwordTextView.getText().toString().trim();
            if(email.isEmpty())
            {
                emailTextView.setError("Email is empty");
                emailTextView.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                emailTextView.setError("Enter the valid email");
                emailTextView.requestFocus();
                return;
            }
            if(password.isEmpty())
            {
                passwordTextView.setError("Password is empty");
                passwordTextView.requestFocus();
                return;
            }
            if(password.length()<6)
            {
                passwordTextView.setError("Length of password is more than 6");
                passwordTextView.requestFocus();
                return;
            }
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(this, MapsActivity.class));
                }
                else
                {
                    Toast.makeText(this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(this,CreateAccountActivity.class )));
    }

//        loggedIn = sp.getInt("loggedIn", 0);
//        userEmail = sp.getString("userEmail", "");
//        emailTextView = (EditText) findViewById (R.id.loginEmail);
//        passwordTextView = (EditText) findViewById (R.id.loginPassword);
//        feedback = (TextView) findViewById(R.id.loginfeedback);
//        feedback.setText("");
//        mAuth = FirebaseAuth.getInstance();
//
//        if (loggedIn == 1 && userEmail != ""){
//            Context context = getApplicationContext();
//
//            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE,null);
//            DBHelper dbHelper = new DBHelper(sqLiteDatabase);
//            User user = dbHelper.getUser(userEmail);
//
//            sqLiteDatabase.close();
//            loggedInUser = user;
//
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//        }



    //}

    public void gotoCreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

//    public void userLogin(View v){
//        setFeedback("");
//        String userEmail = emailTextView.getText().toString();
//        String password = passwordTextView.getText().toString();
//        mAuth.signInWithEmailAndPassword(userEmail, password)
//                .addOnCompleteListener(this, new onCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Log.d(TAG, "signIn:success");
//                            Intent intent = new Intent(this, MapsActivity.class);
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//        });


        /*FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
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


        Context context = getApplicationContext();

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
    //  }

    public void setFeedback(String msg){
        feedback.setText(msg);
    }

}