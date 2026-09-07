package com.example.service;

import java.util.ArrayList;
import java.util.Optional;

import com.example.entity.Role;
import com.example.entity.User;

import jakarta.enterprise.context.ApplicationScoped;



//keeps track of all users
@ApplicationScoped
public class UserService {
    private ArrayList<User> allUsers;
    private int nextId = 0;

    public UserService() {
        allUsers = new ArrayList<User>();
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }
    public Optional<User> findUser(int id) {
        return allUsers.stream().filter(u -> u.getUid() == id).findAny();
    }

    public User createUser(String name, Role role) {
        User newuser = new User(nextId++, name , role);
        allUsers.add(newuser);
        return newuser;
    }

    public void deleteUser(int id) {
        Optional<User> target = findUser(id);
        if (target.isPresent()) {allUsers.remove(target.get());} 
    }

    public void resetUsers() {
        allUsers.clear();
    }

}
