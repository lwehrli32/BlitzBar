package com.example.blitzbar;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseHelper {
    DatabaseReference userDatabase;
    boolean userExists;
    User user;

    public FireBaseHelper(DatabaseReference databaseReference) {
        this.userDatabase = FirebaseDatabase.getInstance().getReference();
    }



    public void insertUser(String first_name, String last_name, String email, String birthday, long blitz_score, long longitude, long latitude, String password){
        User insertUser = new User(first_name, last_name, email, birthday, blitz_score, longitude, latitude, password);
        userDatabase.child("users").child(String.valueOf(email.hashCode())).setValue(insertUser);
    }

    private boolean addFriend(String user_email, String friend_email){
        // TODO add friend to friends arraylist in the user object
        return true;
    }

    public void updateBlitzScore(String email, long blitz_score) {
        userDatabase.child("users").child(String.valueOf(email.hashCode())).child("blitz_score").setValue(blitz_score);
    }

    private boolean locationUpdate(String email) {
        return true;
    }

    private ArrayList<String> listFriends(String email) {
        ArrayList<String> friends = new ArrayList<>();
        return friends;
    }

    private boolean checkFriend(String email, String friend_email) {
        return true;
    }

    private long[] friendLocation(String friend_email) {
        long longitude = 0;
        long latitude = 0;
        return new long[]{longitude, latitude};
    }

    public boolean checkUser(String checkEmail) {
        DatabaseReference userData = userDatabase.child("users").child(String.valueOf(checkEmail.hashCode()));
        String inputUserId = String.valueOf(checkEmail.hashCode());
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = snapshot.getKey();
                FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
                fireBaseHelper.userExists = userId.equals(inputUserId);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userExists;
    }

    public User getUser(String email) {
        DatabaseReference userData = userDatabase.child("users").child(String.valueOf(email.hashCode()));
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FireBaseHelper fireBaseHelper = new FireBaseHelper(userDatabase);
                String firstname = snapshot.child("first_name").toString();
                String lastname = snapshot.child("last_name").toString();
                String userEmail = snapshot.child("email").toString();
                String birthday = snapshot.child("birthday").toString();
                long blitzScore = Long.parseLong(snapshot.child("blitz_score").toString());
                long longitude = Long.parseLong(snapshot.child("longitude").toString());
                long latitude = Long.parseLong(snapshot.child("latitude").toString());
                String password = snapshot.child("password").toString();
                fireBaseHelper.user = new User(firstname, lastname, userEmail, birthday, blitzScore, longitude, latitude, password);
                if (fireBaseHelper.user == null) {
                    Log.e("user null check, first", "USER IS NULL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (user == null) {
            Log.e("user null check, second", "USER IS NULL");
        }
        return user;
    }
}
