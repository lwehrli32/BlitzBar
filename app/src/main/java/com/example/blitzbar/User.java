package com.example.blitzbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User {

    private String first_name;
    private String last_name;
    private String email;
    private String birthday;
    private long blitz_score;
    private long longitude;
    private long latitude;
    private ArrayList<String> friends;
    private String password;

    public User(String first_name, String last_name, String email, String birthday, Long blitz_score, long longitude, long latitude, String password){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birthday = birthday;
        this.blitz_score = blitz_score;
        this.longitude = longitude;
        this.latitude = latitude;
        this.password = password;
    }

    public String getFirst_name(){return this.first_name;}

    public void setFirst_name(String firstname) {
        this.first_name = firstname;
    }

    public String getLast_name(){return this.last_name;}

    public void setLast_name(String lastname) {
        this.last_name = lastname;
    }

    public String getEmail(){return this.email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday(){return this.birthday;}

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getBlitz_score(){return this.blitz_score;}

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void incrementBlitzBar(){
        this.blitz_score++;
    }

    public ArrayList<String> getFriends() {return this.friends;}

    public void addFriend(String new_friend){
        this.friends.add(new_friend);
    }

    public long getLongitude() {return this.longitude;}

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {return this.latitude;}

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
