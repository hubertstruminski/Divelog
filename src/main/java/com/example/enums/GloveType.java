package com.example.enums;

public enum GloveType {

    WET("WET"),
    DRY("DRY"),
    NONE("NONE");

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

    public GloveType returnGloveType(String gloveType) {
        if(gloveType.equalsIgnoreCase("WET")) {
            return GloveType.WET;
        } else if(gloveType.equalsIgnoreCase("DRY")) {
            return GloveType.DRY;
        }
        return GloveType.NONE;
    }
}
