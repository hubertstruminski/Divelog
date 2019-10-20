package com.example.model;

import java.math.BigInteger;

public class LoginRequest {

    private String accessToken;
    private String email;
    private String name;
    private BigInteger userID;
    private BigInteger twitterUserId;
    private String pictureUrl;

    public LoginRequest() {
    }

    public LoginRequest(String accessToken, String email, String name, BigInteger userID, String pictureUrl) {
        this.accessToken = accessToken;
        this.email = email;
        this.name = name;
        this.userID = userID;
        this.pictureUrl = pictureUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getUserID() {
        return userID;
    }

    public void setUserID(BigInteger userID) {
        this.userID = userID;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public BigInteger getTwitterUserId() {
        return twitterUserId;
    }

    public void setTwitterUserId(BigInteger twitterUserId) {
        this.twitterUserId = twitterUserId;
    }
}