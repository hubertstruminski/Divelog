package com.example.enums;

public enum DivingSuit {

    DRY("DRY"),
    SEMIARID("SEMIARID"),
    WET("WET");

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
}
