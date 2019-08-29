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

    public WaterEntryType returnWaterEntryType(String waterEntryType) {
        if(waterEntryType.equalsIgnoreCase("COAST")) {
            return WaterEntryType.COAST;
        } else if(waterEntryType.equalsIgnoreCase("BOAT")) {
            return WaterEntryType.BOAT;
        }
        return WaterEntryType.NONE;
    }
}
