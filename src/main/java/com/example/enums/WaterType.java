package com.example.enums;

public enum WaterType {

    SWEET("SWEET"),
    SALT("SALT"),
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
