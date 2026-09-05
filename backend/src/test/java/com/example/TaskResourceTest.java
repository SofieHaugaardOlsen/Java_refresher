package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TaskResourceTest {
    //tasks exist and contain json objects?
    @Test
    void getTasks() {
        given()
            .when()
            .get("/tasks")
            .then()
            .statusCode(200)
            .contentType("application/json");
    }
    
    //posting a new task updates the tasks
    @Test 
    void postTask(){
        given()
        .contentType("application/json")
        .body("{\"id\": 1, \"title\": \"prepare lunch\", \"starttime\": 800, \"endtime\": 1100, \"neededRoles\": [\"Worker\"], \"issuer\": {\"uid\": \"boss1\", \"role\": \"Boss\"}}")
        .when()
        .post("/tasks")
        .then()
        .statusCode(200)
        .contentType("application/json")
        .body("_id", is(1))
        .body("_title", is("prepare lunch"));

        given()
        .when()
        .get("/tasks")
        .then()
        .statusCode(200)
        .body("find {it._id == 1}._title", is("prepare lunch"));
    }

}