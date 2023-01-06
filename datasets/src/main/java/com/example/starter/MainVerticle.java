package com.example.starter;

import com.example.starter.handler.FetchWeatherHandler;
import com.example.starter.handler.GetWeatherConditionSummaryHandler;
import com.example.starter.handler.GetWeatherForecastHandler;
import com.example.starter.handler.GetWeatherTempStatsHandler;
import com.example.starter.service.WeatherForecastService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

import javax.naming.ConfigurationException;

public class MainVerticle extends AbstractVerticle {
    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8282/auth";

    private static final String KEYCLOAK_REALM = "socialnetwork";

    private static final String KEYCLOAK_CLIENT_ID = "vertx";

    public static void main(String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        // enable jackson to be able to serialize and deserialize Java 8 dates and time
        DatabindCodec.mapper().registerModule(new JavaTimeModule());
        DatabindCodec.mapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // TODO: config should be coming from ConfigMap
        String keycloakClientSecret = System.getenv("KEYCLOAK_CLIENT_SECRET");
        if (keycloakClientSecret == null)
            throw new ConfigurationException("Error: KEYCLOAK_CLIENT_SECRET is not defined");

        var keycloakOptions = new JsonObject()
            .put("auth-server-url", KEYCLOAK_SERVER_URL)
            .put("realm", KEYCLOAK_REALM)
            .put("resource", KEYCLOAK_CLIENT_ID)
            .put("credentials", new JsonObject()
                .put("secret", keycloakClientSecret));

        // setup authentication and authorization
        var oAuth2AuthHandler = OAuth2AuthHandler.create(
            vertx,
            KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakOptions),
            "http://localhost:8888"
        );

        // setup database
        // TODO: config should be coming from ConfigMap
        var mongoConfig = new JsonObject()
            .put("db_name", "socialnetwork")
            .put("connection_string", "mongodb://localhost:27017")
            .put("username", "admin")
            .put("password", "password");
        var db = MongoClient.createShared(vertx, mongoConfig);

        // create web client
        var webClient = WebClient.create(vertx);

        // create service classes
        var weatherForecastService = new WeatherForecastService(webClient, db);

        var router = Router.router(vertx);

        // setup periodic events
        // fetch new weather forecast every hour
        var fetchWeatherHandler = new FetchWeatherHandler(weatherForecastService);
        vertx.setPeriodic(0, 1000 * 60 * 60, fetchWeatherHandler);

        // setup handlers
        var getWeatherForecastHandler = new GetWeatherForecastHandler(weatherForecastService);
        var getWeatherConditionSummaryHandler = new GetWeatherConditionSummaryHandler(weatherForecastService);
        var getWeatherTempStatsHandler = new GetWeatherTempStatsHandler(weatherForecastService);

        // setup routes
        router.route().handler(oAuth2AuthHandler);
        router.route(HttpMethod.GET, "/weather").handler(getWeatherForecastHandler);
        router.route(HttpMethod.GET, "/weather/conditions").handler(getWeatherConditionSummaryHandler);
        router.route(HttpMethod.GET, "/weather/temps").handler(getWeatherTempStatsHandler);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8888, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port 8888");
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }
}
