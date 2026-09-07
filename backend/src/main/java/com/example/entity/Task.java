package com.example.entity;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
    private int id;
    private String title;
    private int starttime;
    private int endtime;
    private ArrayList<Role> neededRoles;
    private ArrayList<Integer> participants;
    private int issuer;
    private boolean isFull;

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
        isFull = false;

        participants = new ArrayList<>();
    }

    public void addParticipant(int uid) {
        participants.add(uid);
        if(participants.size() == neededRoles.size()) {isFull = true;}
    }

    public void removeParticipant(int uid) {
        participants.remove(uid);
        //may not remove if uid not present
        if(participants.size() != neededRoles.size()) {isFull = false;}
    }

    ///////////////////////
    /// Getters 
    ///////////////////////
    public int getId() { return id;}
    public int getStarttime() { return starttime;}
    public int getEndtime() { return endtime;}
    public String getTitle() { return title;}
    public int getIssuer() { return issuer;}
    public boolean isFull() {return isFull;}
    public ArrayList<Role> getNeededRoles() { return neededRoles;}
    public ArrayList<Integer> getParticipants() { return participants;}
  

}
