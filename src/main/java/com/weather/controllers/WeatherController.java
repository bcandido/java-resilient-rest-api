package com.weather.controllers;

import com.weather.services.WeatherStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
    private static final String SERVICE_UNAVAILABLE = "Service unavailable";
    private WeatherStatusService weatherStatusService;

    @Autowired
    public WeatherController(WeatherStatusService weatherStatusService) {
        this.weatherStatusService = weatherStatusService;
    }

//    @RequestMapping(path = "/weather", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> getWeather(@RequestParam(value = "city") String city) {
//        ResponseEntity response;
//        Optional<Weather> weather = weatherStatusService.getCityWeatherData(city);
//        if (weather.isPresent()) {
//            log.info(weather.toString());
//            response = new ResponseEntity<>(weather.get(), HttpStatus.OK);
//        } else {
//            log.warn(SERVICE_UNAVAILABLE);
//            response = new ResponseEntity<>(SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
//        }
//        return response;
//    }


    @RequestMapping(path = "/weather", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DeferredResult<ResponseEntity<?>> getCityWeather(@RequestParam(value = "city") String city) {
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        weatherStatusService.getCityWeatherData(city)
                .handle((cityWeather, throwable) -> {
                    if (cityWeather != null) {
                        deferredResult.setResult(new ResponseEntity<>(cityWeather, HttpStatus.OK));
                    } else {
                        log.error("No fallback response for city [{}].", city);
                        deferredResult.setResult(
                                new ResponseEntity<>(SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE));
                    }
                    return null;
                });
        return deferredResult;
    }

}
