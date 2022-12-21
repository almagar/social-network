package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth;
import io.vertx.ext.web.Router;
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
        // TODO: config should be coming from ConfigMap
        String keycloakClientSecret = System.getenv("KEYCLOAK_CLIENT_SECRET");
        if (keycloakClientSecret == null)
            throw new ConfigurationException("Error: KEYCLOAK_CLIENT_SECRET is not defined");

        JsonObject keycloakOptions = new JsonObject()
            .put("auth-server-url", KEYCLOAK_SERVER_URL)
            .put("realm", KEYCLOAK_REALM)
            .put("resource", KEYCLOAK_CLIENT_ID)
            .put("credentials", new JsonObject()
                .put("secret", keycloakClientSecret));

        // setup authentication and authorization
        JWTAuthOptions jwtAuthOptions = new JWTAuthOptions(keycloakOptions);
        OAuth2AuthHandler oAuth2AuthHandler = OAuth2AuthHandler.create(
            vertx,
            KeycloakAuth.create(vertx, OAuth2FlowType.AUTH_CODE, keycloakOptions),
            "http://localhost:8888"
        );

        Router router = Router.router(vertx);


        // setup routes
        router.route().handler(oAuth2AuthHandler);
        router.route(HttpMethod.GET, "/").handler(ctx -> ctx.response().end("User: " + ctx.user().principal()));

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
