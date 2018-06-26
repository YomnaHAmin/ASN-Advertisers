package com.example.android.advertisers;

public class __Info {

    // Class for the advertiser info
    // These info are set when the user logs in the app
    // So they are stored somewhere in the app to be used anytime ...

    public static int ID;
    public static String name;
    public static String email;
    public static String serviceType;
    public static String phone;
    public static String adrs;
    public static String slogan;
    public static String workTime;
    public static String iconURL;

    public static String licence;
    public static String creditCard;
    public static double lat;
    public static double lng;
    public static double atit;

    public static void logout(){
        ID = -1;
        name = null;
        email = null;
        serviceType = null;
        slogan = null;
        phone = null;
        adrs = null;
        workTime = null;
        iconURL = null;
        licence = null;
        lat = 0;
        lng = 0;
        atit = 0;
    }

}
