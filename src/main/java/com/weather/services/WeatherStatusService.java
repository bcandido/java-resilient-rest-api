package com.weather.services;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.ArrayMap;
import com.weather.models.CityWeather;
import com.weather.models.Weather;
import com.weather.models.WeatherDetails;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.weather.configuration.CacheConfiguration.SHORT_LIVED_CITY_WEATHER;

@Service
public class WeatherStatusService {
    private static final Logger log = LoggerFactory.getLogger(WeatherStatusService.class);

    private static final String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    // private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    // private static JsonFactory JSON_FACTORY = new JacksonFactory();

    private final AsyncHttpClient httpClient;
    private final AsyncCompletionHandler asyncCompletionHandler;

    @Value("${com.weather.api.token}")
    private String TOKEN;

    @Autowired
    public WeatherStatusService(AsyncHttpClient httpClient, AsyncCompletionHandler asyncCompletionHandler){
        this.httpClient = httpClient;
        this.asyncCompletionHandler = asyncCompletionHandler;
    }

//    @Cacheable(SHORT_LIVED_CITY_WEATHER)
//    public Optional<Weather> getWeatherStatus(String city) {
//        Optional<Weather> weather = Optional.empty();
//        String url = String.format(WEATHER_API + "?q=%s&units=metric&APPID=%s", city, TOKEN);
//        Optional<HttpResponse> response = executeGet(new GenericUrl(url));
//        try {
//            if (response.isPresent() && response.get().isSuccessStatusCode()) {
//                GenericJson json = response.get().parseAs(GenericJson.class);
//                weather = Optional.of(buildWeatherFromJson(json));
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return weather;
//    }

    @Cacheable(SHORT_LIVED_CITY_WEATHER)
    public CompletableFuture<CityWeather> getCityWeatherData(String city) {
        String url = String.format(WEATHER_API + "?q=%s&units=metric&APPID=%s", city, TOKEN);
        log.info(String.format("Query %s", url));
        return httpClient.prepareGet(url)
                .execute(asyncCompletionHandler)
                .toCompletableFuture();
    }

//    /**
//     * Get Http Request Object
//     *
//     * @param url URL
//     * @return
//     */
//    private HttpRequest getHttpRequest(GenericUrl url) {
//        try {
//            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
//                    (HttpRequest request) -> {
//                        request.setParser(new JsonObjectParser(JSON_FACTORY));
//                    });
//            return requestFactory.buildGetRequest(url);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return null;
//        }
//    }

//    private Optional<HttpResponse> executeGet(GenericUrl url) {
//        Optional<HttpResponse> response = Optional.empty();
//        try {
//            HttpRequest request = getHttpRequest(url);
//            response = Optional.ofNullable(request.execute());
//        }   catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return response;
//    }

//    /**
//     * Builds WeatherDetails Object based on OpenWeatherMap API
//     * https://openweathermap.org/current
//     *
//     * @param json
//     * @return WeatherDetails
//     */
//    private WeatherDetails buildWeatherDetailsFromJson(GenericJson json) {
//        Map<String, Object> mainInfos = (Map<String, Object>) json.get("main");
//        String temperature = mainInfos.get("temp").toString();
//        String pressure = mainInfos.get("pressure").toString();
//        String humidity = mainInfos.get("humidity").toString();
//
//        Map wind = (Map) json.get("wind");
//        String windSpeed = wind.get("speed").toString();
//
//        return new WeatherDetails(pressure, humidity, temperature, windSpeed);
//    }
//
//    /**
//     * Builds Weather Object based on OpenWeatherMap API
//     * https://openweathermap.org/current
//     *
//     * @param json
//     * @return Weather
//     */
//    private Weather buildWeatherFromJson(GenericJson json) {
//        WeatherDetails details = buildWeatherDetailsFromJson(json);
//        List<ArrayMap> weatherList = (List<ArrayMap>) json.get("weather");
//        String main = weatherList.get(0).get("main").toString();
//        String description = weatherList.get(0).get("description").toString();
//        String city = json.get("name").toString();
//        return new Weather(city, main, description, details);
//    }
}