package com.example.blitzbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    SQLiteDatabase sqLiteDatabase;

    public DBHelper(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void createUsersTable(){
        sqLiteDatabase.execSQL("Create table if not exists users " + "(user_id Integer primary key, username text, first_name text, last_name text, email text, birthday text, blitz_score Integer, fav_drink text, fav_bar text, dark_mode Integer, search_radius Integer)");
    }

    @SuppressLint("DefaultLocale")
    public boolean insertUser(String username, String first_name, String last_name, String email, String birthday, long blitz_score, String fav_drink, String fav_bar){
        createUsersTable();

        // check if the user already exists
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where username like '%s'", username), null);
        int numUsers = 0;

        while(!c.isAfterLast()){
            numUsers++;
            c.moveToNext();
        }

        c.close();
        sqLiteDatabase.close();

        if(numUsers > 0) return false;

        // user does not exist, insert the user
        sqLiteDatabase.execSQL(String.format("Insert into users (username, first_name, last_name, email, birthday, blitz_score, fav_drink, fav_bar, dark_mode, search_radius) Values ('%s', '%s', '%s', '%s', '%s', '%d', '%s', '%s', 0, 5", username, first_name, last_name, email, birthday, blitz_score, fav_drink, fav_bar));
        return true;
    }

    public void updateUser(){
        createUsersTable();
        // TODO update users table
    }

    public User getUser(String username){
        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where username like '%s'", username), null);
        //User user = new User();
        return null;
    }
}
