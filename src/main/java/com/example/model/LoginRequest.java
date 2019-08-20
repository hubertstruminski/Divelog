package com.example.model;

public class LoginRequest {

    private String accessToken;
    private String email;
    private String name;
    private Long userID;

    public LoginRequest() {
    }

    public LoginRequest(String accessToken, String email, String name, Long userID) {
        this.accessToken = accessToken;
        this.email = email;
        this.name = name;
        this.userID = userID;
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

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}