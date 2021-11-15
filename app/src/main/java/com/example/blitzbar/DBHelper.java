package com.example.blitzbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createUsersTable(){
        sqLiteDatabase.execSQL("Create table if not exists users " + "(user_id Integer primary key, first_name text, last_name text, email text, birthday text, blitz_score text, fav_drink text, fav_bar text, dark_mode Integer, search_radius Integer)");
    }

    @SuppressLint("DefaultLocale")
    public boolean insertUser(String first_name, String last_name, String email, String birthday, String blitz_score, String fav_drink, String fav_bar){
        createUsersTable();

        // check if the user already exists
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where email like '%s'", email), null);
        int numUsers = 0;

        while(!c.isAfterLast()){
            numUsers++;
            c.moveToNext();
        }

        c.close();

        if(numUsers > 0) return false;

        // TODO find out how to insert passwords
        // user does not exist, insert the user

        // TODO error here
        sqLiteDatabase.execSQL(String.format("Insert into users (first_name, last_name, email, birthday, blitz_score, fav_drink, fav_bar, dark_mode, search_radius) Values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', 0, 5)", first_name, last_name, email, birthday, blitz_score, fav_drink, fav_bar));
        return true;
    }

    public void updateUser(){
        createUsersTable();
        // TODO update users table
    }

    public User getUser(String email){
        createUsersTable();

        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where email like '%s'", email), null);

        int first_name_index = c.getColumnIndex("first_name");
        int last_name_index = c.getColumnIndex("last_name");
        int birthday_index = c.getColumnIndex("birthday");
        int blitz_score_index = c.getColumnIndex("blitz_score");
        int fav_drink_index = c.getColumnIndex("fav_drink");
        int fav_bar_index = c.getColumnIndex("fav_bar");
        int dark_mode_index = c.getColumnIndex("dark_mode");
        int search_radius_index = c.getColumnIndex("search_radius");

        String first_name = null;
        String last_name = null;
        String birthday = null;
        String blitz_score = "0";
        String fav_bar = null;
        String fav_drink = null;
        int dark_mode = 0;
        int search_radius = 5;
        int numUsers = 0;

        c.moveToFirst();

        while(!c.isAfterLast()){
            first_name = c.getString(first_name_index);
            last_name = c.getString(last_name_index);
            birthday = c.getString(birthday_index);
            blitz_score = c.getString(blitz_score_index);
            fav_bar = c.getString(fav_bar_index);
            fav_drink = c.getString(fav_drink_index);
            dark_mode = c.getInt(dark_mode_index);
            search_radius = c.getInt(search_radius_index);
            numUsers++;
            c.moveToNext();
        }

        if (numUsers != 1) {
            return null;
        }

        c.close();

        return new User(first_name, last_name, email, birthday, blitz_score, fav_bar, fav_drink, dark_mode, search_radius);
    }
}
