package app.security.controllers;

import app.config.ApplicationConfig;
import app.routes.Routes;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class SecurityControllerTest {
    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:7080";
        RestAssured.basePath = "/api/";
        ApplicationConfig.
                getInstance()
                .initiateServer()
                //  .checkSecurityRoles() // check for role when route is called
                //  .setRoute(new SecurityRoutes().getSecurityRoutes())
                //  .setRoute(SecurityRoutes.getSecuredRoutes())
                .setRoute(new Routes().getRoutes())
                .startServer(7080);
    }


    @Test
    void login() {
        String loginJson =
                """
                {
                    "username": "ADMIN",
                    "password": "1234"
                }
                """;

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginJson)
                        .when()
                        .post("auth/login")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(200)
                        .body("username",equalTo("ADMIN"))
                        .extract().response();

        token = response.path("token");
    }

    @Test
    void register() {
    }

    @Test
    void authenticate() {
    }

    @Test
    void authorize() {
    }
}