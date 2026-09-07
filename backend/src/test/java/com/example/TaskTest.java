package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.entity.Role;
import com.example.entity.Task;

//unittesting for the task class
public class TaskTest {
    private Task t1;
    @BeforeEach
    void setUp() {
        t1 = new Task(0,"survey",800,1600,new ArrayList<>(Arrays.asList(Role.HR, Role.StudentHelper)), 0);
    }

    @Test 
    void assigningEnoughWorkersMakesTaskFull() {
        assertEquals(false, t1.isFull());
        t1.addParticipant(0);
        assertEquals(false, t1.isFull());
        t1.addParticipant(1);
        assertEquals(true, t1.isFull());
    }

    @Test 
    void removingAssignedWorkerMakesTaskNotFull() {
        t1.addParticipant(0);
        t1.addParticipant(1);
        assertEquals(true, t1.isFull());
        t1.removeParticipant(1);
        assertEquals(false, t1.isFull());
    }

    @Test 
    void removingUnassignedWorkerChangesNothing() {
        t1.addParticipant(0);
        t1.addParticipant(1);
        assertEquals(true, t1.isFull());
        t1.removeParticipant(3);
        assertEquals(true, t1.isFull());
    }
}
