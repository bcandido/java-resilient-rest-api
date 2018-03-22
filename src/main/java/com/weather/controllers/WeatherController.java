package com.weather.controllers;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ArrayMap;
import com.weather.weather.Weather;
import com.weather.weather.WeatherDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.weather.configuration.CacheConfiguration.SHORT_LIVED_CITY_WEATHER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
    private static final String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();

    @Cacheable(SHORT_LIVED_CITY_WEATHER)
    @RequestMapping(path = "/weather", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GenericJson getWeather(@RequestParam(value = "city") String city,
                                  HttpServletResponse response) {
        Weather weather;
        String query = String.format(WEATHER_API + "?q=%s&units=metric&APPID=%s", city, TOKEN);

        try {
            GenericUrl url = new GenericUrl(query);
            HttpRequest request = getHttpRequest(url);
            GenericJson json = request.execute().parseAs(GenericJson.class);
            WeatherDetails details = buildWeatherDetailsFromJson(json);
            weather = buildWeatherFromJson(json, details);
            log.info(weather.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(404);
            GenericJson notFoundJsonResponse = new GenericJson();
            notFoundJsonResponse.put("status", "city not found");
            return notFoundJsonResponse;
        }
        return weather;
    }

    @Value("${com.weather.api.token}")
    private String TOKEN;

    /**
     * Get Http Request Object
     *
     * @param url URL
     * @return
     * @throws IOException
     */
    private HttpRequest getHttpRequest(GenericUrl url) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                });
        return requestFactory.buildGetRequest(url);
    }

    /**
     * Builds WeatherDetails Object based on OpenWeatherMap API
     * https://openweathermap.org/current
     *
     * @param json
     * @return WeatherDetails
     */
    private WeatherDetails buildWeatherDetailsFromJson(GenericJson json) {
        Map<String, Object> mainInfos = (Map<String, Object>) json.get("main");
        String temperature = mainInfos.get("temp").toString();
        String pressure = mainInfos.get("pressure").toString();
        String humidity = mainInfos.get("humidity").toString();

        Map wind = (Map) json.get("wind");
        String windSpeed = wind.get("speed").toString();

        return new WeatherDetails(pressure, humidity, temperature, windSpeed);
    }

    /**
     * Builds Weather Object based on OpenWeatherMap API
     * https://openweathermap.org/current
     *
     * @param json
     * @param details
     * @return Weather
     */
    private Weather buildWeatherFromJson(GenericJson json, WeatherDetails details) {
        List<ArrayMap> weatherList = (List<ArrayMap>) json.get("weather");
        String main = weatherList.get(0).get("main").toString();
        String description = weatherList.get(0).get("description").toString();
        String city = json.get("name").toString();
        return new Weather(city, main, description, details);
    }

}
