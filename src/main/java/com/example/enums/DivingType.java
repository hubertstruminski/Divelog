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

    public DivingType returnDivingType(String divingType) {
        if(divingType.equalsIgnoreCase("RECREATIONAL")) {
            return DivingType.RECREATIONAL;
        } else if(divingType.equalsIgnoreCase("TECHNICAL")) {
            return DivingType.TECHNICAL;
        } else if(divingType.equalsIgnoreCase("CAVE")) {
            return DivingType.CAVE;
        } else if(divingType.equalsIgnoreCase("WRECK")) {
            return DivingType.WRECK;
        }
        return DivingType.NONE;
    }
}
