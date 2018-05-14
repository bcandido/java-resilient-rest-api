package com.weather.services;

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
import com.weather.controllers.WeatherController;
import com.weather.pojo.Weather;
import com.weather.pojo.WeatherDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {
    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);
    private static final String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();

    @Value("${com.weather.api.token}")
    private String TOKEN;

    public Optional<Weather> getWeather(String city) {
        Optional<Weather> weather = Optional.empty();
        GenericJson json = makeWeatherRequest(city);
        if (!json.isEmpty()) {
            weather = Optional.of(buildWeatherFromJson(json));
            log.info(String.format("get weather for city %s successfully: %s", city, weather.get()));
        } else {
            log.info(String.format("unable to get weather for city %s", city));
        }
        return weather;
    }

    private GenericJson makeWeatherRequest(String city) {
        try {
            log.info(String.format("making request to %s for city %s", WEATHER_API, city));
            String query = String.format("%s?q=%s&units=metric&APPID=%s", WEATHER_API, city, TOKEN);
            GenericUrl url = new GenericUrl(query);
            HttpRequest request = getHttpRequest(url);
            return request.execute().parseAs(GenericJson.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new GenericJson();
    }

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
     * @return Weather
     */
    private Weather buildWeatherFromJson(GenericJson json) {
        WeatherDetails details = buildWeatherDetailsFromJson(json);
        List<ArrayMap> weatherList = (List<ArrayMap>) json.get("weather");
        String main = weatherList.get(0).get("main").toString();
        String description = weatherList.get(0).get("description").toString();
        String city = json.get("name").toString();
        return new Weather(city, main, description, details);
    }

}
