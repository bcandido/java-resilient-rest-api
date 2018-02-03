package com.weather.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class About {

    @RequestMapping(path = "/about", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, String> getAbout() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "health");
        return response;
    }

}
