package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

@QuarkusTest
class TaskResourceTest {

    @Inject
    TaskResource taskResource;

    @BeforeEach
    void setUp() {
        taskResource.resetTasks();
    }

    //tasks exist and contain json objects?
    @Test
    void creatingTaskSucceeds() {
        Task t1 = new Task(0,"survey",800,1600,new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0);
        given()
            .contentType(ContentType.JSON)
            .body(t1)
            .when().post("/tasks")
            .then().statusCode(200)
            .body("id", is(0))
            .body("title", is("survey"))
            .body("starttime", is(800))
            .body("endtime", is(1600))
            .body("participants", hasSize(0));
    }

    @Test 
    void fetchingCreatedTaskSucceeds() {
        Task t1 = new Task(0,"survey",800,1600,new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0);
        int id = given()
            .contentType(ContentType.JSON)
            .body(t1)
            .when().post("/tasks")
            .then().statusCode(200).extract().path("id");

        given().when().get("/tasks").then().statusCode(200).body("$", hasSize(1));  //one message stored
        given().when().get("/tasks/" + id).then().statusCode(200).body("id",is(id)).body("title",is("survey")); //it's the same message
    }

    @Test 
    void fetchingNonexistingTaskFails() {
        given().when().get("/tasks/0").then().statusCode(404);
    }
    
}