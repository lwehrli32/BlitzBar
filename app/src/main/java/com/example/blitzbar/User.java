package com.example.blitzbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User {

    private final String first_name;
    private final String last_name;
    private final String email;
    private final String birthday;
    private long blitz_score;
    private long longitude;
    private long latitude;
    private ArrayList<String> friends;

    public User(String first_name, String last_name, String email, String birthday, Long blitz_score, long longitude, long latitude){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birthday = birthday;
        this.blitz_score = blitz_score;
        this.friends = friends;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getFirst_name(){return this.first_name;}

    public String getLast_name(){return this.last_name;}

    public String getEmail(){return this.email;}

    public String getBirthday(){return this.birthday;}

    public Long getBlitz_score(){return this.blitz_score;}

    public void incrementBlitzBar(){
        this.blitz_score++;
    }

    public ArrayList<String> getFriends() {return this.friends;}

    public void addFriend(String new_friend){
        this.friends.add(new_friend);
    }

    public long getLongitude() {return this.longitude;}

    public long getLatitude() {return this.latitude;}


}
