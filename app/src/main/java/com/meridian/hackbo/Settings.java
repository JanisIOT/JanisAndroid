package com.meridian.hackbo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;


public class Settings {
    private static final boolean DEBUG = Config.DEBUG;
    private static final String TAG = Settings.class.getSimpleName();

//	private static final String TAG = Settings.class.getSimpleName();
//  private static final boolean DEBUG = Config.DEBUG;
	
	public static boolean isFirstIntro(Context ctx){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
		return preferences.getBoolean(SettingsFields.help_intro, false);
	}
	

    //*************************************************************
    // POIS STORAGE
    //*************************************************************



    public static void setSSID(Context ctx,String ssid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SettingsFields.SSID,ssid);
        editor.commit();


    }
    public static String getSSID(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ssid = preferences.getString(SettingsFields.SSID, "");
        return ssid;

    }

    public static void setIP(Context ctx,String IP) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SettingsFields.IP,IP);
        editor.commit();

    }
    public static String getIP(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ip = preferences.getString(SettingsFields.IP, "192.168.4.1");
     return ip;

    }

    public static void addUserid(Context ctx,String uid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SettingsFields.uid,uid);
        editor.commit();


    }
    public static String getUserId(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String uid = preferences.getString(SettingsFields.uid, "");
        if(uid.equals("")){

            return null;
        }
        else return uid;

    }








	
	
	
}