package com.example.starter.handler;

import com.example.starter.model.WeatherForecast;
import com.example.starter.model.WeatherMain;
import com.example.starter.service.WeatherForecastService;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWeatherTempStatsHandler implements Handler<RoutingContext> {
    private final WeatherForecastService weatherForecastService;

    public GetWeatherTempStatsHandler(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        weatherForecastService.getHourlyWeatherForecast()
            .onSuccess(forecast -> {
                var weatherForecasts = forecast.getJsonArray("list")
                    .stream()
                    .filter(JsonObject.class::isInstance)
                    .map(JsonObject.class::cast)
                    .map(o -> o.mapTo(WeatherForecast.class))
                    .toList();
                ctx.json(getTempStats(weatherForecasts));
            })
            .onFailure(cause -> {
                ctx.response().setStatusCode(404);
                ctx.json(new JsonObject().put("message", "Could not find weather forecast"));
            });
    }

    public static Map<Long, WeatherMain> getTempStats(List<WeatherForecast> weatherForecasts) {
        var conditionCount = new HashMap<Long, WeatherMain>();
        weatherForecasts.forEach(weatherForecast ->
            conditionCount.put(weatherForecast.getDt(), weatherForecast.getMain()));

        return conditionCount;
    }
}
