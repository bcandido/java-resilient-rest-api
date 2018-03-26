package com.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Map;

@Value.Immutable
@JsonSerialize(as = ImmutableWeatherDetails.class)
@JsonDeserialize(as = ImmutableWeatherDetails.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*", jdkOnly = true)
public abstract class WeatherDetails {

    public static ImmutableWeatherDetails.Builder builder() {
        return ImmutableWeatherDetails.builder();
    }

    public abstract String getDescription();

    @JsonProperty("main")
    public abstract String getStatus();
}
