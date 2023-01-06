package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

// Description of fields can be found at https://openweathermap.org/forecast5, not all are used and thus not included
@Value
public class WeatherForecast {
    long dt;

    WeatherMain main;

    @JsonCreator
    public WeatherForecast(
        @JsonProperty("dt") Long dt,
        @JsonProperty("main") WeatherMain main,
        @JsonProperty("weather") List<Weather> weathers) {
        this.dt = dt;
        this.main = main;
        this.weathers = weathers;
    }

    @JsonProperty("weather")
    List<Weather> weathers;


    @Value
    public static class Weather {
        int id;
        String main;

        @JsonCreator
        public Weather(
            @JsonProperty("id") int id,
            @JsonProperty("main") String main) {
            this.id = id;
            this.main = main;
        }
    }
}
