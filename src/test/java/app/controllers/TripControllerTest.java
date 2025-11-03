package app.controllers;

import app.config.ApplicationConfig;
import app.routes.route;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TripControllerTest {

    private static int createdTripId = 1;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:7080";
        RestAssured.basePath = "/api/";
        ApplicationConfig.
                getInstance()
                .initiateServer()
                .setRoute(new route().getRoutes())
                .startServer(7080);
    }

    @AfterAll
    public static void teardown(){
        ApplicationConfig.getInstance().stopServer();
    }


    @Test
    @Order(3)
    void read() {
        given()
                .pathParam("id", createdTripId)
                .when()
                .get("trips/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTripId));
    }

    @Test
    @Order(4)
    void readAll() {
        given()
                .when()
                .get("trips")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1)) // eller tjek specifik trip
                .body("[0].name", equalTo("Copenhagen"));
    }

    @Test
    @Order(2)
    void create() {
        String tripJson =
                """
                {
                    "name": "Copenhagen",
                    "startTime": "2026-04-20T09:00",
                    "endTime": "2026-04-20T17:00",
                    "location": "Denmark",
                    "price": 1200,
                    "category": "CITY"
                }
                """;

                given()
                        .contentType(ContentType.JSON)
                        .body(tripJson)
                        .when()
                        .post("trips")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .body("name", equalTo("Copenhagen"))
                        .body("location", equalTo("Denmark"))
                        .extract().response();
    }

@Test
@Order(1)
    void createGuide(){
        String guideJson =
            """
            {
            "name": "Anders",
            "email": "Anders@hotmail.com",
            "phoneNumber": "41321287",
            "yearsOfExperience": 8
            }
            """;
    given()
            .contentType(ContentType.JSON)
            .body(guideJson)
            .when()
            .post("trips/guides")
            .then()
            .contentType(ContentType.JSON)
            .statusCode(201)
            .body("name", equalTo("Anders"))
            .body("email", equalTo("Anders@hotmail.com"));

    }



    @Test
    @Order(5)
    void update() {
        String updatedTripJson =
                """
                {
                    "name": "Copenhagen",
                    "startTime": "2026-04-20T09:00",
                    "endTime": "2026-04-20T17:00",
                    "location": "Denmark",
                    "price": 1300,
                    "category": "CITY"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", createdTripId)
                .body(updatedTripJson)
                .when()
                .put("trips/{id}")
                .then()
                .statusCode(200)
                .body("price", equalTo(1300f));
    }


    @Test
    @Order(7)
    void delete() {
        given()
                .pathParam("id", createdTripId)
                .when()
                .delete("trips/{id}")
                .then()
                .statusCode(200)
                .body(containsString("deleted"));
    }



    @Test
    @Order(6)
    void addGuideToTrip() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", createdTripId)
                .when()
                .put("trips/{id}/guides/{id}")
                .then()
                .statusCode(200);
    }
}