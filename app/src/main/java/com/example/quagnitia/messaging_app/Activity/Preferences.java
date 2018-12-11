package com.example.quagnitia.messaging_app.Activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by V@iBh@V on 12/7/2018.
 */

public class Preferences  {
    private String LOGIN="LOGIN";

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context mContext;



    public void setLOGIN(boolean  firstTimeCall) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(this.LOGIN, firstTimeCall);
        editor.commit();

    }





}
