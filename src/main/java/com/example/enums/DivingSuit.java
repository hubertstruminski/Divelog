package com.example.enums;

public enum DivingSuit {

    DRY("DRY"),
    SEMIARID("SEMIARID"),
    WET("WET"),
    NONE("NONE");

    private String divingSuit;

    DivingSuit(String divingSuit) {
        this.divingSuit = divingSuit;
    }

    public String getDivingSuit() {
        return divingSuit;
    }

    public void setDivingSuit(String divingSuit) {
        this.divingSuit = divingSuit;
    }

    public DivingSuit returnDivingSuit(String divingSuit) {
        if(divingSuit.equalsIgnoreCase("DRY")) {
            return DivingSuit.DRY;
        } else if(divingSuit.equalsIgnoreCase("SEMIARID")) {
            return DivingSuit.SEMIARID;
        } else if(divingSuit.equalsIgnoreCase("WET")) {
            return DivingSuit.WET;
        }
        return DivingSuit.NONE;
    }
}
