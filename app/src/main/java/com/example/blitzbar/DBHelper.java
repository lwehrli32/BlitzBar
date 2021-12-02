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

    private int getUserId(String email){

        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where email = '%s'", email), null);

        int numUsers = 0;
        int user_id = -1;

        int userIdIndex = c.getColumnIndex("user_id");

        while(!c.isAfterLast()){
            numUsers++;
            user_id = c.getInt(userIdIndex);
            c.moveToNext();
        }

        c.close();

        if (numUsers != 1) return -1;

        return user_id;
    }

    private String getUserEmail(int user_id){

        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from users where user_id = %i", user_id), null);

        int userEmailIndex = c.getColumnIndex("email");
        String user_email = "";

        while(!c.isAfterLast()){
            user_email = c.getString(userEmailIndex);
            c.moveToNext();
        }

        c.close();

        return user_email;
    }

    public void createUsersTable(){
        sqLiteDatabase.execSQL("Create table if not exists users " + "(user_id Integer primary key, first_name text, last_name text, email text, birthday text, blitz_score text, fav_drink text, fav_bar text, dark_mode Integer, search_radius Integer)");
    }

    public void createFriendsTable(){
        sqLiteDatabase.execSQL("Create table if not exists friends " + "(id Integer primary key, user_id Integer, friend_id Integer)");
    }

    public boolean createFriendShip(String user_email, String friend_email){
        createFriendsTable();

        int user_id = getUserId(user_email);
        int friend_id = getUserId(friend_email);

        if(user_id == -1 || friend_id == -1) return false;

        sqLiteDatabase.execSQL(String.format("Insert into friends (user_id, friend_id) Values (%i, %i)", user_id, friend_id));

        return true;
    }

    public boolean isFriend(String user_email, String friend_email){
        createFriendsTable();

        int user_id = getUserId(user_email);
        int friend_id = getUserId(friend_email);
        int numRelationship = 0;

        if (user_id == -1 || friend_id == -1) return false;

        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from friends where user_id = %i and friend_id = %i", user_id, friend_id), null);

        while(!c.isAfterLast()) {
            numRelationship++;
            c.moveToNext();
        }

        c.close();

        if (numRelationship != 1) return false;

        return true;

    }

    public ArrayList<String> listFriends(String email){
        createFriendsTable();
        createUsersTable();

        ArrayList<String> friends = new ArrayList<>();

        int userId = getUserId(email);

        Cursor c = sqLiteDatabase.rawQuery(String.format("Select * from friends where user_id = %i", userId), null);

        int friendIdIndex = c.getColumnIndex("friend_id");

        while(!c.isAfterLast()){
            int friend_id = c.getInt(friendIdIndex);
            String friend_email = getUserEmail(friend_id);
            friends.add(friend_email);
            c.moveToNext();
        }

        c.close();
        return friends;
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
