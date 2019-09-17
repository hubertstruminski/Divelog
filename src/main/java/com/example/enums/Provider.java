package com.example.enums;

public enum Provider {

    FACEBOOK("FACEBOOK"),
    TWITTER("TWITTER");

    private String provider;

    Provider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
