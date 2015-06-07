package com.ghiyats.fish.temannelayan.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Ghiyats on 5/13/2015.
 */
public class SessionManager {

    private String  TAG = SessionManager.class.getSimpleName();

    //SharedPreferences
    SharedPreferences preferences;
    Editor editor;
    Context context;
    int sa;

    //sharedPref Mode
    int PRIVATE_MODE =0;

    private static final String PREF_NAME = "NelayanLoginInfo";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    public SessionManager(Context context) {

        this.context=context;
        preferences= context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setLogin(boolean isLogin){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLogin);
        editor.commit();

        Log.d(TAG,"user is Logged in");
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_IS_LOGGEDIN,false);
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, "");
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME,"");
    }
    public String getPassword() {
        return preferences.getString(KEY_PASSWORD,"");
    }

    public void setUserId(String id) {
        editor.putString(KEY_USER_ID,id);
        editor.commit();
    }

    public void setUsername(String name) {
        editor.putString(KEY_USERNAME,name);
        editor.commit();
    }

    public void setPassword(String pass) {
        editor.putString(KEY_PASSWORD,pass);
        editor.commit();
    }

    public void createSession(String id,String name,String pass){
        setLogin(true);
        setUsername(name);
        setUserId(id);
        setPassword(pass);
    }

    public void destroySession(){
        editor.clear();
        editor.commit();
        setLogin(false);
    }

}
