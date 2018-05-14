package com.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = com.weather.models.ImmutableWeather.class)
@JsonDeserialize(as = com.weather.models.ImmutableWeather.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Style(init = "with*", jdkOnly = true)
public abstract class Weather {

    @JsonProperty("temp")
    public abstract double getTemparature();

    public abstract double getPressure();

    public abstract double getHumidity();

    public static com.weather.models.ImmutableWeather.Builder builder(){
        return com.weather.models.ImmutableWeather.builder();
    }
}
