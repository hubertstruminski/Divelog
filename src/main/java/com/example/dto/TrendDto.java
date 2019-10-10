package com.example.dto;

public class TrendDto {

    private String name;
    private String countryName;
    private int tweetVolume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getTweetVolume() {
        return tweetVolume;
    }

    public void setTweetVolume(int tweetVolume) {
        this.tweetVolume = tweetVolume;
    }
}
