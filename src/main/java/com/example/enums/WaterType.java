package com.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WaterType {

    @JsonProperty("SWEET")
    SWEET("SWEET"),
    @JsonProperty("SALT")
    SALT("SALT"),
    @JsonProperty("NONE")
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
