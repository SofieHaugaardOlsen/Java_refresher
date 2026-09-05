package com.example.entity;
import java.util.ArrayList;

enum Status {
    NOT_FULL,
    FULL
}

public class Task {
    private int id;
    private String title;
    private int starttime;
    private int endtime;
    private ArrayList<Role> needed_roles;
    private ArrayList<User> participants;
    private User issuer;
    private Status status;

    public Task(int id , String title, int starttime, int endtime, ArrayList<Role> needed_roles, User issuer) {
        this.id = id;
        this.title = title;
        this.starttime = starttime;
        this.needed_roles = needed_roles;
        this.endtime = endtime;
        this.issuer = issuer;
        status = Status.NOT_FULL;

        participants = new ArrayList<>();
    }

    ///////////////////////////////////////////////////////////////////////////////
    /// 
    /// Functionality for adding and removing participants from tasks
    /// 
    ///////////////////////////////////////////////////////////////////////////////
    public boolean canAssign(User user) {
        if (status == Status.FULL) { return false;} //if task is full no more users can be assigned
        if (participants.contains(user)) { return false;} //if user already assigned, cannot be assigned again

        else {
            Role role = user.get_role();
            long number_needed = needed_roles.stream().filter(r -> r == role).count();
            long number_assigned = participants.stream().filter(u -> u.get_role() == role).count();
            return number_needed > number_assigned;
        }
    }

    /*assigns a user to the task if they are needed*/
    public void Assign(User user) {
        if (canAssign(user)) {
            participants.add(user);
            user.addTask(this);
            if (participants.size() == needed_roles.size()) {status = Status.FULL;} 
        }
    }

    /*removes a user from the task if present*/
    public void removeUser(User user) {
        participants.remove(user);
        user.removeTask(this);
        if (participants.size() != needed_roles.size()) {status = Status.NOT_FULL;} 
    }

    /////////////////////////
    /// 
    /// Getters and setters
    /// 
    ///////////////////////
    /// 
    public int get_id() { return id;}
    public int get_starttime() { return starttime;}
    public int get_endtime() { return endtime;}
    public String get_title() { return title;}
    public User get_issuer() { return issuer;}
    public Status get_status() { return status;}
    public ArrayList<Role> get_neededRoles() { return needed_roles;}
    public ArrayList<User> get_participants() { return participants;}
  

}
