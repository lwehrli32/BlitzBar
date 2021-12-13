package com.example.blitzbar;

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
        User user = new User(first_name, last_name, email, birthday, blitz_score, longitude, latitude, password);
        userDatabase.child(String.valueOf(email.hashCode())).setValue(user);
    }

    private boolean addFriend(String user_email, String friend_email){
        // TODO add friend to friends arraylist in the user object
        return true;
    }

    public void updateBlitzScore(String email, long blitz_score) {
        userDatabase.child(String.valueOf(email.hashCode())).child("blitz_score").setValue(blitz_score);
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
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(String.valueOf(checkEmail.hashCode())).exists()) {
                    userExists = true;
                } else {
                    userExists = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userExists;
    }

    public User getUser(String email) {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstname = userDatabase.child(String.valueOf(email.hashCode())).child("first_name").toString();
                String lastname = userDatabase.child(String.valueOf(email.hashCode())).child("last_name").toString();
                String userEmail = userDatabase.child(String.valueOf(email.hashCode())).child("email").toString();
                String birthday = userDatabase.child(String.valueOf(email.hashCode())).child("birthday").toString();
                long blitzScore = Long.parseLong(userDatabase.child(String.valueOf(email.hashCode())).child("blitz_score").toString());
                long longitude = Long.parseLong(userDatabase.child(String.valueOf(email.hashCode())).child("longitude").toString());
                long latitude = Long.parseLong(userDatabase.child(String.valueOf(email.hashCode())).child("latitude").toString());
                String password = userDatabase.child(String.valueOf(email.hashCode())).child("password").toString();
                user = new User(firstname, lastname, userEmail, birthday, blitzScore, longitude, latitude, password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }
}
