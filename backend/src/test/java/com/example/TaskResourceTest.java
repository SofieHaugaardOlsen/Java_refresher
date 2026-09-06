package com.example;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Arrays;

@QuarkusTest
class TaskResourceTest {
    private TaskResource taskResource;

    @BeforeEach 
    void setUp() {
        taskResource = new TaskResource();
    }
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

    //posting a new task updates the tasks
    @Test 
    void assigningValidUserIncreasesLengthOfParticipants() {
        //pushign task to system
        ArrayList<Role> roles = new ArrayList<Role>(Arrays.asList(Role.Worker, Role.Worker));
        taskResource.createTask(new Task(0, "test_task", 800, 1600 ,roles, new User("alice1",Role.Boss)));

        //assignign a worker
        User worker1 = new User("w1", Role.Worker);
        taskResource.assignParticipant(0, worker1);
        given().when().get("/tasks/0").then().body("_participants.size()", is(1));
    }

}