package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;

//unittesting for the task class
public class TaskTest {

    private User boss1, HR1, HR2,  worker1, worker2, student1;
    private Task t0, t1;

    @BeforeEach 
    void setUp() {
        //some dummy tasks and users to work with
        boss1 = new User(0, Role.Boss);
        HR1 = new User(1, Role.HR);
        HR2 = new User(2, Role.HR);
        worker1 = new User(3, Role.Worker);
        worker2 = new User(4, Role.Worker);
        student1 = new User(5, Role.StudentHelper);

        //dummy tasks
        t0 = new Task(0,"survey",800,1600,new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0);
        t1 = new Task(1,"install server",800,1400,new ArrayList<>(Arrays.asList(Role.Worker, Role.Worker)), 0);
    }
    
    @Test
    void assigningValidParticipantAddsToList() {
        t0.Assign(HR1);
        assertEquals(1, t0.getParticipants().size());
    }

    @Test
    void assigningInvalidRoleParticipantDoesntAddToList() {
        t0.Assign(worker1);
        assertEquals(0, t0.getParticipants().size());
    }

    @Test
    void assigningToFullHasNoEffect() {
        t0.Assign(HR1);
        t0.Assign(student1);
        assertEquals(2, t0.getParticipants().size());
        t0.Assign(HR2);
        assertEquals(2, t0.getParticipants().size());
    }

    @Test
    void assigningSameUserTwiceNoEffect() {
        t1.Assign(worker1);
        assertEquals(1, t1.getParticipants().size());
        t1.Assign(worker1);
        assertEquals(1, t1.getParticipants().size());
    }

    @Test
    void assigningMoreUsersWhenAllenoughOfTheirRoleAssignedNoEffect() {
        t0.Assign(HR1);
        assertEquals(1, t0.getParticipants().size());
        t0.Assign(HR2);
        assertEquals(1, t0.getParticipants().size());
    }

}
