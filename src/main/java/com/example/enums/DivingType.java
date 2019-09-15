package com.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
