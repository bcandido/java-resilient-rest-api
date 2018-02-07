package com.weather.weather;

import com.google.api.client.util.Key;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class WeatherDetails {

    public WeatherDetails(String pressure, String humidity, String temperature, String windSpeed) {
        this.pressure = pressure;
        this.humidity = humidity;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    @Key
    @Getter
    @Setter
    private String pressure;

    @Key
    @Getter
    @Setter
    private String humidity;

    @Key
    @Getter
    @Setter
    private String temperature;


    @Key
    @Getter
    @Setter
    private String windSpeed;
}