package com.example.enums;

public enum DivingType {

    RECREATIONAL("RECREATIONAL"),
    TECHNICAL("TECHNICAL"),
    CAVE("CAVE"),
    WRECK("WRECK"),
    NONE("NONE");

    private String divingType;

    DivingType(String divingType) {
        this.divingType = divingType;
    }

    public String getDivingType() {
        return divingType;
    }

    public void setDivingType(String divingType) {
        this.divingType = divingType;
    }
}
