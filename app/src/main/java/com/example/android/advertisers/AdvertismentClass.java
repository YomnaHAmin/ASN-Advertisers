package com.example.android.advertisers;

import java.util.Date;

public class AdvertismentClass {

    private String title;
    private String description;
    private String imgURL;
    private Date expirationDate = null;
    private int ID;


    AdvertismentClass(int ID, String title, String description, String imgPath){
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
    }
    AdvertismentClass(int ID, String title, String description, String imgPath, Date expirationDate){
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
        this.expirationDate = expirationDate;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }
    public void setExpirationDate(Date expirationDate){ this.expirationDate = expirationDate; }

    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getImgURL() {
        return imgURL;
    }
    public Date getExpirationDate() { return expirationDate; }
}
