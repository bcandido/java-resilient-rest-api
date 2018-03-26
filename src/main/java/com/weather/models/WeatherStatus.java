package com.weather.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WeatherStatus {
    OK("OK"),
    DOWN("Open Weather API down. Returning last valid result."),
    NOT_FOUND("City not found");

    private String description;

    WeatherStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static WeatherStatus fromName(String name) {
        return WeatherStatus.valueOf(name);
    }
}