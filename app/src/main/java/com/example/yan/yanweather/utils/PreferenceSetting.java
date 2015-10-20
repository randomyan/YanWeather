package com.example.yan.yanweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yan on 10/17/2015.
 */
public class PreferenceSetting {
    public static final String PREFS_SETTING = "WEATHER_FORMAT"; //file name
    public static final String PREFS_DAYS = "PREFS_DAYS";
    public static final String PREFS_UNIT = "PREFS_UNIT";

    public PreferenceSetting(){
        super();
    }
    public void save(Context c,String pref, String text){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = c.getSharedPreferences(PREFS_SETTING, Context.MODE_PRIVATE);
        editor =settings.edit();
        editor.putString(pref, text);
        editor.commit();
    }

    public String getValue(Context c,String ref){
        SharedPreferences settings;
        String text;
        settings=c.getSharedPreferences(PREFS_SETTING, Context.MODE_PRIVATE);
        text =settings.getString(ref,null);
        return text;
    }

    public  void removeValue(Context c, String ref){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = c.getSharedPreferences(PREFS_SETTING, Context.MODE_PRIVATE);
        editor =settings.edit();
        editor.remove(ref);
        editor.commit();
    }

    public void clearSetting(Context c){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = c.getSharedPreferences(PREFS_SETTING, Context.MODE_PRIVATE);
        editor =settings.edit();
        editor.clear();
        editor.commit();
        editor.commit();
    }
    public void defautSetting(Context c){
        save(c,PREFS_DAYS,"3" );
        save(c,PREFS_UNIT,"Celsius" );
    }
}
