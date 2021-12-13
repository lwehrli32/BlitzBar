package com.example.blitzbar;

public class Users {
    private String first_name, last_name, blitz_score, profile_image, fullName;

    public Users() {

    }

    public Users (String first_name, String last_name, String blitz_score) {
        this.blitz_score = blitz_score;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_image = profile_image;

        this.fullName = first_name + " " + last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBlitz_score() {
        return blitz_score;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getFullName() {
        return fullName;
    }
}
