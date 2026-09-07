package com.example;

import java.util.ArrayList;

import com.example.entity.Role;

public record CreateTaskRequest(String title, int starttime, int endtime, ArrayList<Role> neededRoles, int issuerID) {};