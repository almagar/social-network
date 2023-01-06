package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class WeatherMain {
    float temp;

    float feelsLike;

    float tempMin;

    float tempMax;

    @JsonCreator
    public WeatherMain(
        @JsonProperty("temp") float temp,
        @JsonProperty("feels_like") float feelsLike,
        @JsonProperty("temp_min") float tempMin,
        @JsonProperty("temp_max") float tempMax) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
