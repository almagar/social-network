package com.example.starter.handler;

import com.example.starter.model.WeatherForecast;
import com.example.starter.service.WeatherForecastService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWeatherConditionSummaryHandler implements Handler<RoutingContext> {
    private final WeatherForecastService weatherForecastService;

    public GetWeatherConditionSummaryHandler(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String location = ctx.request().params().get("location");
        weatherForecastService.getHourlyWeatherForecast(location)
            .onSuccess(forecast -> {
                var weatherForecasts = forecast.getJsonArray("list")
                    .stream()
                    .filter(JsonObject.class::isInstance)
                    .map(JsonObject.class::cast)
                    .map(o -> o.mapTo(WeatherForecast.class))
                    .toList();
                ctx.json(getConditionSummary(weatherForecasts));
            })
            .onFailure(cause -> {
                ctx.response().setStatusCode(404);
                ctx.json(new JsonObject().put("message", "Could not find weather forecast"));
            });
    }

    public static Map<String, Integer> getConditionSummary(List<WeatherForecast> weatherForecasts) {
        var conditionCount = new HashMap<String, Integer>();
        weatherForecasts.forEach(weatherForecast ->
            weatherForecast.getWeathers().forEach(weather ->
                conditionCount.merge(weather.getMain(), 1, Integer::sum)));

        return conditionCount;
    }
}
