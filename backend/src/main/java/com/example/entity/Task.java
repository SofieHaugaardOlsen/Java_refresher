package com.example.entity;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

enum Status {
    NOT_FULL,
    FULL
}

public class Task {
    private int id;
    private String title;
    private int starttime;
    private int endtime;
    private ArrayList<Role> neededRoles;
    private ArrayList<User> participants;
    private int issuer;
    private Status status;

    @JsonCreator 
    public Task(@JsonProperty("id") int id,
                @JsonProperty("title") String title,
                @JsonProperty("starttime") int starttime,
                @JsonProperty("endtime") int endtime,
                @JsonProperty("neededRoles") ArrayList<Role> needed_roles,
                @JsonProperty("issuer") int issuer) {
        this.id = id;
        this.title = title;
        this.starttime = starttime;
        this.neededRoles = needed_roles;
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
            long number_needed = neededRoles.stream().filter(r -> r == role).count();
            long number_assigned = participants.stream().filter(u -> u.get_role() == role).count();
            return number_needed > number_assigned;
        }
    }

    /*assigns a user to the task if they are needed*/
    public void Assign(User user) {
        if (canAssign(user)) {
            participants.add(user);
            user.addTask(this);
            if (participants.size() == neededRoles.size()) {status = Status.FULL;} 
        }
    }

    /*removes a user from the task if present*/
    public void removeUser(int uid) {
        participants.stream().filter(u -> u.getUid() != uid);
        //user.removeTask(this);
        if (participants.size() != neededRoles.size()) {status = Status.NOT_FULL;} 
    }

    /////////////////////////
    /// 
    /// Getters and setters
    /// 
    ///////////////////////
    /// 
    public int getId() { return id;}
    public int getStarttime() { return starttime;}
    public int getEndtime() { return endtime;}
    public String getTitle() { return title;}
    public int getIssuer() { return issuer;}
    public Status getStatus() { return status;}
    public ArrayList<Role> getNeededRoles() { return neededRoles;}
    public ArrayList<User> getParticipants() { return participants;}
  

}
