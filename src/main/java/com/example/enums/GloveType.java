package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GloveType {

    @JsonProperty("WET")
    WET("WET"),
    @JsonProperty("DRY")
    DRY("DRY"),
    @JsonProperty("NONE")
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
}
