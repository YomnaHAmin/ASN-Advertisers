package com.example.android.advertisers;

public class AdvertismentClass {

    private String title;
    private String description;
    private String imgURL;


    AdvertismentClass(String title, String description, String imgPath){
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
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


    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getImgURL() {
        return imgURL;
    }
}
