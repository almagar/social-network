package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {
  private static final String KEYCLOAK_SERVER_URL = "http://localhost:8282/auth";

  private static final String KEYCLOAK_REALM = "socialnetwork";

  private static final String KEYCLOAK_CLIENT_ID = "socialnetwork";

  private static final String KEYCLOAK_CLIENT_SECRET = "Y3c4FMjQjGcu3Yz00V2e3fTIaNuoAe1W";

  private JWTAuth jwtAuth;

  public static void main(String[] args) {
    Launcher.executeCommand("run", MainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    JWTAuthOptions jwtAuthOptions = new JWTAuthOptions(new JsonObject()
        .put("keycloak", new JsonObject()
            .put("auth-server-url", KEYCLOAK_SERVER_URL)
            .put("realm", KEYCLOAK_REALM)
            .put("resource", KEYCLOAK_CLIENT_ID)
            .put("credentials", new JsonObject()
                .put("secret", KEYCLOAK_CLIENT_SECRET))
        ));

    jwtAuth = JWTAuth.create(vertx, jwtAuthOptions);

    Router router = Router.router(vertx);

    //router.route().handler(this::requireAuth); // TODO: temporarily disable
    router.route(HttpMethod.GET, "/").handler(ctx -> {
      ctx.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x! user: " + ctx.user());
    });

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

  private void requireAuth(RoutingContext ctx) {
    String authHeader = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwt = authHeader.substring("Bearer ".length());
      Credentials credentials = new TokenCredentials(jwt);
      jwtAuth.authenticate(credentials, authResult -> {
        if (authResult.succeeded()) {
          ctx.setUser(authResult.result());
          System.out.println("user: " + authResult.result());
          ctx.next();
        } else {
          ctx.response().setStatusCode(401).end();
        }
      });
    } else {
      ctx.response().setStatusCode(401).end();
    }
  }
}
