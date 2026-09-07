package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.entity.Role;
import com.example.service.TaskService;
import com.example.service.UserService;

class TaskServiceTest {

    private UserService userService;
    private TaskService taskService;
    private int hr1Id, hr2Id, worker1Id, student1Id;
    private int t0Id, t1Id;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        taskService = new TaskService(userService); 

        hr1Id = userService.createUser("Alice", Role.HR).getUid();
        hr2Id = userService.createUser("Bob", Role.HR).getUid();
        worker1Id = userService.createUser("Charlie", Role.Worker).getUid();
        student1Id = userService.createUser("Dan", Role.StudentHelper).getUid();

        t0Id = taskService.createTask("survey", 800, 1600,
                new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0).getId();
        t1Id = taskService.createTask("install server", 800, 1400,
                new ArrayList<>(Arrays.asList(Role.Worker, Role.Worker)), 0).getId();
    }

    @Test
    void assigningValidParticipantAddsToList() {
        taskService.assignTaskParticipant(t0Id, hr1Id);
        assertEquals(1, taskService.findTask(t0Id).get().getParticipants().size());
    }

    @Test
    void assigningInvalidRoleParticipantDoesNotAdd() {
        taskService.assignTaskParticipant(t0Id, worker1Id);
        assertEquals(0, taskService.findTask(t0Id).get().getParticipants().size());
    }

    @Test
    void assigningToFullHasNoEffect() {
        taskService.assignTaskParticipant(t0Id, hr1Id);
        taskService.assignTaskParticipant(t0Id, student1Id);
        assertEquals(2, taskService.findTask(t0Id).get().getParticipants().size());
        taskService.assignTaskParticipant(t0Id, hr2Id);
        assertEquals(2, taskService.findTask(t0Id).get().getParticipants().size());
    }
    
    @Test
    void assigningSameUserTwiceNoEffect() {
        taskService.assignTaskParticipant(t1Id, worker1Id);
        assertEquals(1, taskService.findTask(t1Id).get().getParticipants().size());
        taskService.assignTaskParticipant(t1Id, worker1Id);
        assertEquals(1, taskService.findTask(t1Id).get().getParticipants().size());
    }

    @Test
    void assigningMoreUsersWhenAllenoughOfTheirRoleAssignedNoEffect() {
        taskService.assignTaskParticipant(t0Id, hr1Id);
        assertEquals(1, taskService.findTask(t0Id).get().getParticipants().size());
        taskService.assignTaskParticipant(t0Id, hr2Id);
        assertEquals(1, taskService.findTask(t0Id).get().getParticipants().size());
    }

}