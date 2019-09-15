package com.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WaterEntryType {

    @JsonProperty("COAST")
    COAST("COAST"),
    @JsonProperty("BOAT")
    BOAT("BOAT"),
    @JsonProperty("NONE")
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
