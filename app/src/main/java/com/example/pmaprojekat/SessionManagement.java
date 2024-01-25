package com.example.pmaprojekat;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_id";

    public SessionManagement(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void saveSession(int userID)
    {
        editor.putInt(SESSION_KEY,userID).commit();
    }

    public int getSession()
    {
        return sharedPreferences.getInt(SESSION_KEY,-1);

    }
    public void removeSession()
    {
        editor.putInt(SESSION_KEY,-1).commit();
    }


}
