package com.example.android.advertisers;

import java.util.Date;

public class AdvertisementClass {

    private String title;
    private String description;
    private String imgURL;
    private String expirationDate = null;
    private int ID;


    AdvertisementClass(int ID, String title, String description, String imgPath){
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
    }
    AdvertisementClass(int ID, String title, String description, String imgPath, String expirationDate){
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
        this.expirationDate = expirationDate;
    }

    public void setID(int ID) { this.ID = ID; }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }
    public void setExpirationDate(String expirationDate){ this.expirationDate = expirationDate; }

    public int getID() { return ID; }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getImgURL() {
        return imgURL;
    }
    public String getExpirationDate() { return expirationDate; }
}
