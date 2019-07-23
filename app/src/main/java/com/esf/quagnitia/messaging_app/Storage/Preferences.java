package com.esf.quagnitia.messaging_app.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Preferences {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context mContext;
    private String LOGIN="LOGIN";
    private String BadgeCount="BadgeCount";
    private  String REGISTERED = "REGISTERED";
    public static final String KEY_AGENT_ID = "agentId";
    public static final String SOUND = "SOUND";
    public static final String RINGTONE = "RINGTONE";
    public static final String VIBRATE = "VIBRATE";
    private static final String PREFS_STORE_PROFILE = "STORE_PROFILE";
  //  String username = getString(PrefConstants.USER_NAME);

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure

     */
    public void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }

    public int getBadgeCount() {
        return preferences.getInt(BadgeCount, 0);
    }

    public void setBadgeCount(int someVariablebb) {
        int someVariable = getBadgeCount();

        if (someVariablebb > 0) {
            someVariable = someVariable + someVariablebb;
        } else {
            someVariable = 0;
        }

        editor.putInt(BadgeCount, someVariable);
        editor.commit();
    }

    public Preferences() {

    }

    public Preferences(Context context) {
        // TODO Auto-generated constructor stub\
        mContext = context;
        preferences = context.getSharedPreferences("ZapFinPref", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public boolean isLogin() {
        return preferences.getBoolean(LOGIN, false);
    }

    public void setLogin(boolean firstTimeCall) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(this.LOGIN, firstTimeCall);
        editor.commit();
    }

    public boolean isSOUND() {
        return preferences.getBoolean(SOUND, true);
    }

    public void setSOUND(boolean firstTimeCall) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(this.SOUND, firstTimeCall);
        editor.commit();
    }

    public boolean isVIBRATE() {
        return preferences.getBoolean(VIBRATE, true);
    }

    public void setVIBRATE(boolean firstTimeCall) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(this.VIBRATE, firstTimeCall);
        editor.commit();
    }

    public String getRINGTONE(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(RINGTONE,null);
    }

    public void setRINGTONE(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RINGTONE,userId);
        editor.commit();
    }

    public  Boolean getREGISTERED() {
        return preferences.getBoolean(REGISTERED, false);
    }

    public  void setREGISTERED(Boolean REGISTERED) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(this.REGISTERED, REGISTERED);
        editor.commit();
    }

    public String getAgentId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AGENT_ID,null);
    }

    public void saveAgentId(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AGENT_ID,userId);
        editor.commit();
    }

    public String getAgentName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Agent_name", null);
    }

    public void saveAgentName(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Agent_name", username);
        editor.commit();
    }

    public String getSchool(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("School", null);
    }

    public void saveSchool(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_STORE_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("School", username);
        editor.commit();
    }
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public Preferences getInstance(Context ctx) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        mContext = ctx;
        return this;
    }

    public void clearPreferences() {
        editor.clear();
        editor.commit();
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

}
