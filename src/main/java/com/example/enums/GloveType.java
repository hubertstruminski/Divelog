package com.example.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GloveType {

    @JsonProperty("WET")
    WET("WET"),
    @JsonProperty("DRY")
    DRY("DRY"),
    @JsonProperty("NONE")
    NONE("NONE");

    private String gloveType;

    GloveType(String gloveType) {
        this.gloveType = gloveType;
    }

    public String getGloveType() {
        return gloveType;
    }

    public void setGloveType(String gloveType) {
        this.gloveType = gloveType;
    }
}
