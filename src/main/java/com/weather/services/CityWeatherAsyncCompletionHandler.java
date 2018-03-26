package com.weather.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.models.CityWeather;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityWeatherAsyncCompletionHandler extends AsyncCompletionHandler<CityWeather> {

    private static final Logger log = LoggerFactory.getLogger(CityWeatherAsyncCompletionHandler.class);
    private static final int OK = 200;
    private static final int NOT_FOUND = 200;

    @Override
    public CityWeather onCompleted(Response response) throws Exception {
        CityWeather cityWeather = null;
        if (response.getStatusCode() == OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            cityWeather = objectMapper.readValue(response.getResponseBody(), CityWeather.class);
        } else if (response.getStatusCode() == NOT_FOUND) {
        }
        return cityWeather;

    }

    @Override
    public void onThrowable(Throwable t) {
        log.error("There was a problem processing the request to OpenWeather", t);
    }
}
