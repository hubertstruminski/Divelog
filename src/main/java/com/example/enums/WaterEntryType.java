package com.example.enums;

public enum WaterEntryType {

    COAST("COAST"),
    BOAT("BOAT"),
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
