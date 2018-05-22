package com.example.chetanmuliya.entrancetest.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by chetanmuliya on 12/14/2017.
 */

public class SessionManager {

    private static String TAG=SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context ctx;

    int PRIVATE_MODE=0;
    private static final String PREF_NAME="TestuneLogin";
    private static final String KEY_IS_LOGGEDIN="isLoggedIn";

    public SessionManager(Context ctx) {
        this.ctx = ctx;
        pref=ctx.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void setLogin(boolean loggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,loggedIn);
        editor.commit();
        Log.d(TAG, "login session modified");
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN,false);
    }
}
