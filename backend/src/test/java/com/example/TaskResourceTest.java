package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.service.TaskService;
import com.example.service.UserService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

@QuarkusTest
class TaskResourceTest {

    @Inject TaskService taskService;
    @Inject UserService userService;

    private CreateTaskRequest t1;
    private int aid, bid;

    //helper for creating a task
    private int createTaskViaHttp(CreateTaskRequest ctr) {
    return given()
        .contentType(ContentType.JSON)
        .body(ctr)
        .when().post("/tasks")
        .then().statusCode(200)
        .extract().path("id");
    }

    @BeforeEach
    void setUp() {
        taskService.resetTasks();
        userService.resetUsers();
        t1 = new CreateTaskRequest("survey",800,1600,new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0);

        aid = userService.createUser("Alice", Role.HR).getUid();
        bid = userService.createUser("Bob", Role.Worker).getUid();
    }

    //tasks exist and contain json objects?
    @Test
    void creatingTaskSucceeds() {
        given()
            .contentType(ContentType.JSON)
            .body(t1)
            .when().post("/tasks")
            .then().statusCode(200)
            .body("id", notNullValue())    //has some ID
            .body("title", is("survey"))
            .body("starttime", is(800))
            .body("endtime", is(1600))
            .body("participants", hasSize(0));
    }

    @Test 
    void fetchingCreatedTaskSucceeds() {
        int id = createTaskViaHttp(t1);

        given().when().get("/tasks").then().statusCode(200).body("$", hasSize(1));  //one task stored
        given().when().get("/tasks/" + id).then().statusCode(200).body("id",is(id)).body("title",is("survey")); //it's the correct task
    }


    @Test 
    void fetchingNonexistingTaskFails() {
        given().when().get("/tasks/0").then().statusCode(404);
    }


    //TODO update these tests when more sophisticated errors have been implemennted
    @Test
    void assigningNonExistingUserToValidTaskFails() {
         int id = createTaskViaHttp(t1);
         given().post("/tasks/" + id + "/participants/500").then().statusCode(404);
        
    }

    @Test
    void assigningToNonExistingTaskFails() {
         given().post("/tasks/0" + "/participants/"+ aid).then().statusCode(404);
    }

    @Test
    void assigningInvalidRoleToValidTaskFails() {
        int id = createTaskViaHttp(t1);
        given().post("/tasks/" + id + "/participants/" + bid).then().statusCode(404);

    }

    @Test
    void assigningvalidRoleToValidTaskSucceedsAndUpdatesTask() {
        int id = createTaskViaHttp(t1);
        given().post("/tasks/" + id + "/participants/" + aid).then().statusCode(200);
        given().get("/tasks/" + id).then().body("participants", hasSize(1));

    }

    @Test
    void removingValidParticipantSucceedsAndUpdatesTask() {
        int id = createTaskViaHttp(t1);
        given().post("/tasks/" + id + "/participants/" + aid).then().statusCode(200);
        given().delete("/tasks/" + id + "/participants/" + aid).then().statusCode(200);
        given().get("/tasks/" + id).then().body("participants", hasSize(0));
    }

    @Test
    void removingInvalidParticipantFailsAndDoesntUpdatesTask() {
        int id = createTaskViaHttp(t1);
        given().post("/tasks/" + id + "/participants/" + aid).then().statusCode(200);
        given().delete("/tasks/" + id + "/participants/" + bid).then().statusCode(404);
        given().get("/tasks/" + id).then().body("participants", hasSize(1));
    }


}