package com.example.blitzbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FireBaseHelper {
    DatabaseReference userDatabase;

    public FireBaseHelper(DatabaseReference databaseReference) {
        this.userDatabase = FirebaseDatabase.getInstance().getReference();
    }


    private boolean insertUser(String first_name, String last_name, String email, String birthday, long blitz_score, ArrayList<String> friends, long longitude, long latitude){
        User user = new User(first_name, last_name, email, birthday, blitz_score, friends, longitude, latitude);
        userDatabase.child("users").child(email).setValue(user);
        return true;
    }

    private boolean addFriend(String user_email, String friend_email){
        return true;
    }

    private boolean updateBlitzScore(String email, long blitz_score) {
        return true;
    }

    private boolean locationUpdate(String email) {
        return true;
    }

    private ArrayList<String> listFriends(String email) {
        ArrayList<String> friends = new ArrayList<>();
        return friends;
    }

    private long[] friendLocation(String friend_email) {
        long longitude = 0;
        long latitude = 0;
        return new long[]{longitude, latitude};
    }

    public User getUser(String email) {
        String first_name = null;
        String last_name = null;
        String birthday = null;
        long blitz_score = 0;
        ArrayList<String> friends = null;
        long longitude = 0;
        long latitude = 0;
        return new User(first_name, last_name, email, birthday, blitz_score, friends, longitude, latitude);
    }
}
