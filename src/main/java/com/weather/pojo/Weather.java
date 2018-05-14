package com.weather.pojo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Weather extends GenericJson {

    @Key
    @Getter
    @Setter
    private String city;
    @Key
    @Getter
    @Setter
    private String weather;
    @Key
    @Getter
    @Setter
    private String description;
    @Key
    @Getter
    @Setter
    private WeatherDetails details;

    public Weather(String city, String weather, String description,
                   WeatherDetails details) {
        this.city = city;
        this.weather = weather;
        this.description = description;
        this.details = details;
    }
}
