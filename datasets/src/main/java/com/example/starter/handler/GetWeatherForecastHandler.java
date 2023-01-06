package com.example.starter.handler;

import com.example.starter.service.WeatherForecastService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class GetWeatherForecastHandler implements Handler<RoutingContext> {
    private final WeatherForecastService weatherForecastService;

    public GetWeatherForecastHandler(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String location = ctx.request().params().get("location");
        weatherForecastService.getHourlyWeatherForecast(location)
            .onSuccess(ctx::json)
            .onFailure(cause -> {
                ctx.response().setStatusCode(404);
                ctx.json(new JsonObject().put("message", "Could not find weather forecast"));
            });
    }
}
