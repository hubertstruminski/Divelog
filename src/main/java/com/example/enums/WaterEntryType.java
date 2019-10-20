package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WaterEntryType {

    @JsonProperty("COAST")
    COAST("COAST"),
    @JsonProperty("BOAT")
    BOAT("BOAT"),
    @JsonProperty("NONE")
    NONE("NONE");

    private String waterEntryType;

    WaterEntryType(String waterEntryType) {
        this.waterEntryType = waterEntryType;
    }

    public String getWaterEntryType() {
        return waterEntryType;
    }

    public void setWaterEntryType(String waterEntryType) {
        this.waterEntryType = waterEntryType;
    }
}
