package com.example.service;

import java.util.ArrayList;
import java.util.Optional;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

//keeps track of all tasks
@ApplicationScoped
public class TaskService {
    private ArrayList<Task> tasks;
    private int nextId = 0;
    private final UserService userService;

    @Inject
    public TaskService(UserService userService) {
        this.userService = userService;
        tasks = new ArrayList<>();  //dummy instance mem

    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public Optional<Task> findTask(int id){
        return tasks.stream().filter(t -> t.getId() == id).findAny();
    }

    public Task createTask(String title, int starttime, int endtime, ArrayList<Role> neededRoles, int issuerID) {
        Task newTask = new Task(nextId++ , title, starttime, endtime, neededRoles, issuerID);
        tasks.add(newTask);
        return newTask;
    }

    public void deleteTask(int id) {
        Optional<Task> target = findTask(id);
        if (target.isPresent()) {tasks.remove(target.get());}
    }

    //helper for testing
    public void resetTasks() {
        tasks.clear();
        nextId = 0;
    }

    private boolean userValidForTask( Optional<Task> otask, Optional<User> ouser) {
        if(otask.isEmpty() || ouser.isEmpty()) {return false;}
        Task task = otask.get();
        User user = ouser.get();
        if(task.isFull()) {return false;}
        if(task.getParticipants().contains(user.getUid())) {return false;}

        //count number of users of same role already assigned
        long rolecount = task.getParticipants().stream()
                        .map(u -> userService.findUser(u))
                        .filter(ou -> ou.isPresent())
                        .filter(p -> p.get().getRole() == user.getRole())
                        .count();
        long roleneeded = task.getNeededRoles().stream().filter(r -> r == user.getRole()).count();
        return rolecount < roleneeded;
        
    }

    public boolean assignTaskParticipant(int taskid, int userid) {
        Optional<User> t_user = userService.findUser(userid);
        Optional<Task> t_task = findTask(taskid);

        if (userValidForTask(t_task, t_user)) {
            t_task.get().addParticipant(userid);
            return true;
        }
        return false;
    }

    public boolean removeTaskParticipant(int taskid, int userid) {
        Optional<User> t_user = userService.findUser(userid);
        Optional<Task> t_task = findTask(taskid);
        if(t_user.isEmpty() || t_task.isEmpty()) {return false;}
        if (t_task.get().getParticipants().contains(userid)) {
            t_task.get().removeParticipant(userid);
            return true;
        } else {
            return false;
        }
        
    }
}
