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

    public WaterType returnWaterType(String waterType) {
        if(waterType.equalsIgnoreCase("SWEET")) {
            return WaterType.SWEET;
        } else if(waterType.equalsIgnoreCase("SALT")) {
            return WaterType.SALT;
        }
        return WaterType.NONE;
    }
}
