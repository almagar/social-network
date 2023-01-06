package com.example.starter.handler;

import com.example.starter.service.WeatherForecastService;
import io.vertx.core.Handler;

public class FetchWeatherHandler implements Handler<Long> {
    private final WeatherForecastService weatherForecastService;

    public FetchWeatherHandler(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @Override
    public void handle(Long aLong) {
        weatherForecastService.getHourlyWeatherForecast();
    }
}
