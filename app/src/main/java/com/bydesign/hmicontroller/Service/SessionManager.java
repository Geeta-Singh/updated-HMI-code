package com.bydesign.hmicontroller.Service;

/**
 * Created by PARIKSHIT on 1/19/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bydesign.hmicontroller.activity.LoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "HmiController";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERLEVEL = "userlevel";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_MAINTOKEN = "maintoken";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        // commit changes
        editor.commit();
    }

    public void setMainToken(String token) {
        editor.putString(KEY_MAINTOKEN, token);
        // commit changes
        editor.commit();
    }

    //Creating login session
    public void createLoginSession(int userlevel, String username) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing vlaues  in pref
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_USERLEVEL, userlevel);

        // commit changes
        editor.commit();
    }


    // Clearing session details
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity

        _context.startActivity(i);

    }

}


