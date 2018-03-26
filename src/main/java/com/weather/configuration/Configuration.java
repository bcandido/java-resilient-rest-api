package com.weather.configuration;

import com.weather.services.CityWeatherAsyncCompletionHandler;
import org.asynchttpclient.*;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public AsyncHttpClient getAsyncHttpClient() {
        AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder()
                .setRequestTimeout(10000).build();
        return new DefaultAsyncHttpClient(config);
    }

    @Bean
    public AsyncCompletionHandler getAsyncCompletionHandler() {
        return new CityWeatherAsyncCompletionHandler();
    }
}
