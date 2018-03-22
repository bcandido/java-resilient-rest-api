package com.weather.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

import static com.weather.configuration.CacheConfiguration.SHORT_LIVED_CITY_WEATHER;

@Controller
public class AboutController {

    private static final Logger log = LoggerFactory.getLogger(AboutController.class);

    @RequestMapping(path = "/about", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, String> getAbout() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "health");
        log.info(response.toString());
        return response;
    }

}
