package com.example.blitzbar;

public class User {

    private final String first_name;
    private final String last_name;
    private final String email;
    private final String birthday;
    private long blitz_score;
    private String fav_bar;
    private String fav_drink;

    public User(String first_name, String last_name, String email, String birthday, String blitz_score, String fav_bar, String fav_drink, int dark_mode, int search_radius){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.birthday = birthday;
        this.blitz_score = Long.parseLong(blitz_score);
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

}
