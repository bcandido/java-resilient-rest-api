package com.weather.controllers;

import com.google.api.client.json.GenericJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(WeatherControllerIntegrationTest.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WebApplicationContext webCtx;

    @LocalServerPort
    int port;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webCtx).build();
    }

    @Test
    public void testGETWeather_shouldReturnStatusOK() {
        ResponseEntity<GenericJson> out = makeRequest("campinas");
        Assert.assertEquals(out.getStatusCode(), HttpStatus.OK);
    }

    private ResponseEntity<GenericJson> makeRequest(String city) {
        String url = "http://localhost:" + port + "/weather?city=" + city;
        return restTemplate.getForEntity(url, GenericJson.class);
    }
}
