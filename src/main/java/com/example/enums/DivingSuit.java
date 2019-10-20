package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DivingSuit {

    @JsonProperty("DRY")
    DRY("DRY"),
    @JsonProperty("SEMIARID")
    SEMIARID("SEMIARID"),
    @JsonProperty("WET")
    WET("WET"),
    @JsonProperty("NONE")
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
}
