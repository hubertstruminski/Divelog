package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WaterType {

    @JsonProperty("SWEET")
    SWEET("SWEET"),
    @JsonProperty("SALT")
    SALT("SALT"),
    @JsonProperty("NONE")
    NONE("NONE");

    private String waterType;

    WaterType(String waterType) {
        this.waterType = waterType;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }
}
