package com.example.dto;

import com.example.model.Connection;

public class ConnectionDto extends Connection {

    private String tokenSecret;
    private String screenName;

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
