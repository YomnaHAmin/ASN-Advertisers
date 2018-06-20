package com.example.android.advertisers;

/**
 * Created by a on 08/03/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Belal on 26/11/16.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "advSharedPref";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_SERVICE_TYPE = "userServiceType";
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_ADRS = "userAdrs";
    private static final String KEY_USER_WORKING_TIME = "userWorkingTime";
    private static final String KEY_USER_LICENCE = "userLicence";
    private static final String KEY_USER_iconURL = "userIconURL";
    private static final String KEY_USER_LAT = "userLat";
    private static final String KEY_USER_LNG = "userLng";
    private static final String KEY_USER_ATIT = "userAtit";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public  boolean setUserInfo(
            int ID,
            String name,
            String slogan,
            String serviceType,
            String adrs,
            String workingTime,
            String phone,
            String email,
            String licence,
            String iconURL,
            double lng,
            double lat,
            double atit){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, ID);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, name);

        editor.apply();

        __Info.ID = ID;
        __Info.name = name;
        __Info.email = email;
        __Info.serviceType = serviceType;
        __Info.slogan = slogan;
        __Info.adrs = adrs;
        __Info.phone = phone;
        __Info.workTime = workingTime;
        __Info.iconURL = iconURL;
        __Info.licence = licence;
        __Info.lat = lat;
        __Info.lng = lng;
        __Info.atit = atit;

        return true;

    }
    public  boolean editUserInfo(
            String name,
            String slogan,
            String serviceType,
            String adrs,
            String workingTime,
            String phone,
            String email,
            String licence,
            String iconURL){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, __Info.ID);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, name);

        editor.apply();

        __Info.name = name;
        __Info.email = email;
        __Info.serviceType = serviceType;
        __Info.slogan = slogan;
        __Info.adrs = adrs;
        __Info.phone = phone;
        __Info.workTime = workingTime;
        __Info.iconURL = iconURL;
        __Info.licence = licence;

        return true;

    }

//    public boolean userLogin(String username, String email){
//
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        //editor.putInt(KEY_USER_ID, id);
//        editor.putString(KEY_USER_EMAIL, email);
//        editor.putString(KEY_USERNAME, username);
//
//        editor.apply();
//
//        return true;
//    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME,null);
        editor.apply();

        __Info.logout();
        return true;
    }


//    public String getUsername(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_USERNAME, null);
//    }
//    public String getToken(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(NOTIFICATION_TOKEN
//                , null);
//    }
//
//    public String getUserEmail(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(KEY_USER_EMAIL, null);
//    }
//
//
//    public int getUserId(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(KEY_USER_ID,0);
//    }
//
//    public int getCarId(){
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getInt(KEY_CAR_ID,0);
//    }
}
