package com.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import java.util.List;


@Immutable
@JsonSerialize(as = com.weather.models.ImmutableCityWeather.class)
@JsonDeserialize(as = com.weather.models.ImmutableCityWeather.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Style(init = "with*", jdkOnly = true)
public abstract class CityWeather {

    public static com.weather.models.ImmutableCityWeather.Builder builder() {
        return com.weather.models.ImmutableCityWeather.builder();
    }

    @Default
    public WeatherStatus serviceStatus() {
        return WeatherStatus.OK;
    }

    @JsonProperty("name")
    public abstract String getCityName();

    @JsonProperty("weather")
    public abstract List<WeatherDetails> getWeatherDetails();

    @JsonProperty("main")
    public abstract Weather getWeather();
}
