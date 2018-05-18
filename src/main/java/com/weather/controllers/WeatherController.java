package com.weather.controllers;

import com.google.api.client.json.GenericJson;
import com.weather.pojo.Weather;
import com.weather.services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import static com.weather.configuration.CacheConfiguration.SHORT_LIVED_CITY_WEATHER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @ResponseBody
    @Cacheable(SHORT_LIVED_CITY_WEATHER)
    @RequestMapping(path = "/weather", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWeather(@RequestParam(value = "city") String city) {
        ResponseEntity<?> response;
        Optional<Weather> weather = weatherService.getWeather(city);
        if (weather.isPresent()) {
            response = new ResponseEntity<>(weather.get(), HttpStatus.OK);
        } else {
            GenericJson notFoundJsonResponse = new GenericJson();
            notFoundJsonResponse.put("status", "city not found");
            response = new ResponseEntity<>(notFoundJsonResponse, HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
