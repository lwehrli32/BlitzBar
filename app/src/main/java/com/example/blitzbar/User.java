package com.example.blitzbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private final String first_name;
    private final String last_name;
    private final String email;
    private final String birthday;
    private long blitz_score;
    private String fav_bar;
    private String fav_drink;
    private int longitude;
    private int latitude;

    public User(String first_name, String last_name, String email, String birthday, Long blitz_score, String fav_bar, String fav_drink){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birthday = birthday;
        this.blitz_score = blitz_score;
        this.fav_bar = fav_bar;
        this.fav_drink = fav_drink;
    }

    public String getFirst_name(){return this.first_name;}

    public String getLast_name(){return this.last_name;}

    public String getEmail(){return this.email;}

    public String getBirthday(){return this.birthday;}

    public String getFav_bar(){return this.fav_bar;}

    public String getFav_drink(){return this.fav_drink;}

    public Long getBlitz_score(){return this.blitz_score;}

    public void FirebaseRead() {
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(first_name, last_name, email, birthday, blitz_score, fav_bar, fav_drink);
        //DatabaseReference userData = userDatabase.child("users").child(email).setValue(user);
    }

}
