package com.example.blitzbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FireBaseHelper {
    DatabaseReference userDatabase;

    public FireBaseHelper(DatabaseReference databaseReference) {
        this.userDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private long getUserId(String email) {
        long user_Id = -1;
        return user_Id;
    }

    private String getUserEmail(int user_id) {
        String user_email = "";
        return user_email;
    }

    private ArrayList<String> listFriends(String email) {
        ArrayList<String> friends = new ArrayList<>();
        return friends;
    }

    public boolean insertUser(String first_name, String last_name, String email, String birthday, String blitz_score, String fav_drink, String fav_bar){
        return true;
    }

    public User getUser(String email) {
        String first_name = null;
        String last_name = null;
        String birthday = null;
        long blitz_score = 0;
        String fav_bar = null;
        String fav_drink = null;
        return new User(first_name, last_name, email, birthday, blitz_score, fav_bar, fav_drink);
    }
}
