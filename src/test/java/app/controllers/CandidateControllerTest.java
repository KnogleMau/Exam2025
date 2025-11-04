package app.controllers;

import app.config.ApplicationConfig;
import app.routes.Routes;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CandidateControllerTest {

    private static int createdCandidateId = 1;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:7080";
        RestAssured.basePath = "/api/";
        ApplicationConfig.
                getInstance()
                .initiateServer()
                .setRoute(new Routes().getRoutes())
                .startServer(7080);
    }

    @AfterAll
    public static void teardown(){
        ApplicationConfig.getInstance().stopServer();
    }


    @Test
    @Order(6)
    void read() {
        given()
                .pathParam("id", createdCandidateId)
                .when()
                .get("candidates/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdCandidateId))
                .body("skills", notNullValue())
                .body("skillStatsDTOS", notNullValue())
                .body("skillStatsDTOS.size()", greaterThan(0))
                .body("skillStatsDTOS[0].popularityScore", notNullValue())
                .body("skillStatsDTOS[0].averageSalary", notNullValue());
    }

    @Test
    @Order(4)
    void readAll() {
        given()
                .when()
                .get("candidates")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("Rasmus"));
    }

    @Test
    @Order(2)
    void createCandidate() {
        String candidateJson =
                """
                        {
                          "name": "Rasmus",
                          "phoneNumber": "91919191",
                          "educationBackground":"Ek"
                        }
                """;

                given()
                        .contentType(ContentType.JSON)
                        .body(candidateJson)
                        .when()
                        .post("candidates")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .body("name", equalTo("Rasmus"))
                        .body("phoneNumber", equalTo("91919191"))
                        .extract().response();
    }

@Test
@Order(1)
    void createSkill(){
        String skillJson =
            """
                    {
                      "name": "Framework",
                      "category": "FRAMEWORK",
                      "description": "Application frameworks and libraries",
                      "slug": "spring-boot,react,angular"
                    }
            """;
    given()
            .contentType(ContentType.JSON)
            .body(skillJson)
            .when()
            .post("skill")
            .then()
            .contentType(ContentType.JSON)
            .statusCode(201)
            .body("name", equalTo("Framework"))
            .body("category", equalTo("FRAMEWORK"));

    }



    @Test
    @Order(5)
    void update() {
        String updatedCandidateJson =
                """
                {
                      "name": "Rene",
                      "phoneNumber": "41414141",
                      "educationBackground":"Ek"
                   }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", createdCandidateId)
                .body(updatedCandidateJson)
                .when()
                .put("candidates/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Rene"))
                .body("phoneNumber", equalTo("41414141"));

    }


    @Test
    @Order(8)
    void delete() {
        given()
                .pathParam("id", createdCandidateId)
                .when()
                .delete("candidates/{id}")
                .then()
                .statusCode(200)
                .body(containsString("deleted"));
    }





    @Test
    @Order(3)
    void addSkillToCandidate() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", createdCandidateId)
                .when()
                .put("candidates/{id}/skills/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(7)
    void getPopularityFromCandidates() {
        given()
                .when()
                .get("candidates/top-by-popularity")
                .then()
                .statusCode(200)
                .body("[0].averagePopularityScore", greaterThan(0.0f))
                .body("[0].candidateName", equalTo("Rene"));
        }
    }
