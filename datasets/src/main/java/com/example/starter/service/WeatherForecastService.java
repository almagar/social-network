package com.example.starter.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class WeatherForecastService {
    private final WebClient webClient;

    private final MongoClient db;

    private final String apiKey;

    public WeatherForecastService(WebClient webClient, MongoClient db) throws RuntimeException {
        this.webClient = webClient;
        this.db = db;
        apiKey = System.getenv("WEATHER_API_KEY");
        if (apiKey == null || apiKey.equals("")) {
            throw new RuntimeException("Error: Missing 'WEATHER_API_KEY' environment variable");
        }
    }

    public Future<JsonObject> getHourlyWeatherForecast(String location) {
        var time = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        // TODO: find out why query doesn't work with city name
        var query = new JsonObject()
            .put("createdAt", Json.encode(time))
            .put("city", new JsonObject().put("name", StringUtils.capitalize(location)));
        System.out.println(query);
        Promise<JsonObject> promise = Promise.promise();
        // try to find the forecast in our database first
        db.findOne("weatherForecasts", query, null, ar -> {
            if (ar.succeeded()) {
                var result = ar.result();
                if (result == null) {
                    System.out.println("not found 1");
                    promise.fail("Forecast not found");
                } else {
                    System.out.println("found");
                    promise.complete(result);
                }
            } else {
                System.out.println("not found 2");
                promise.fail("Forecast not found");
            }
        });
        return promise.future()
            .recover(cause -> fetchWeatherForecast(location)) // if not found, fetch from weather api
            .onSuccess(this::saveWeatherForecast);
    }

    private Future<JsonObject> fetchWeatherForecast(String location) {
        System.out.println("fetching from api");
        var escapedLocation = URLEncoder.encode(location.toLowerCase(Locale.ROOT), StandardCharsets.UTF_8);
        var url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&appid=%s",
            escapedLocation, apiKey);
        System.out.println(url);
        var request = webClient
            .getAbs(url)
            .ssl(true)
            .putHeader("Accept", "application/json")
            .as(BodyCodec.jsonObject())
            .expect(ResponsePredicate.SC_OK);

        return request.send().map(HttpResponse::body);
    }

    private void saveWeatherForecast(JsonObject forecast) {
        var time = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        forecast.put("createdAt", Json.encode(time));
        db.save("weatherForecasts", forecast, res -> {
            if (res.succeeded()) {
                String id = res.result();
                System.out.println("Saved forecast with id " + id);
            } else {
                System.out.println("Error: Failed to save forecast: " + res.cause().getMessage());
                res.cause().printStackTrace();
            }
        });
    }
}
