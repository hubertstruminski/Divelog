package com.example.dto;

public class ContactDirectMessage {

    private String name;
    private String screenName;
    private boolean isDMAccessible;
    private String pictureUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public boolean isDMAccessible() {
        return isDMAccessible;
    }

    public void setDMAccessible(boolean DMAccessible) {
        isDMAccessible = DMAccessible;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
