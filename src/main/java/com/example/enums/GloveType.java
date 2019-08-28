package com.example.enums;

public enum GloveType {

    WET("WET"),
    DRY("DRY");

    private String gloveType;

    GloveType(String gloveType) {
        this.gloveType = gloveType;
    }

    public String getGloveType() {
        return gloveType;
    }

    public void setGloveType(String gloveType) {
        this.gloveType = gloveType;
    }
}
