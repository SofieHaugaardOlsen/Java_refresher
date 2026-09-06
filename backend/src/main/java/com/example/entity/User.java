package com.example.entity;
import java.util.ArrayList;

public class User {
    /*Object representing users of the system */
    private int uid;
    private Role role;
    private ArrayList<Task> assignedTasks; //should appear on their timetable view

    public User(int uid, Role role) {
        this.uid = uid;
        this.role = role;
        assignedTasks = new ArrayList<Task>();
    }

    //accessors
    public int getUid() { return uid; }
    public Role getRole() { return role; }
    public int getTaskCount() { return assignedTasks.size(); }

    //mutators
    public void setUid(int new_uid) {this.uid = new_uid;}
    public void setRole(Role new_role) {this.role = new_role;}
    public void addTask(Task task) {this.assignedTasks.add(task);}
    public void removeTask(Task task) {this.assignedTasks.remove(task);}
}