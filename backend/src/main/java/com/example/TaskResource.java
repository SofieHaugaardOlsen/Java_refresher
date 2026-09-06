package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;

@Path("/tasks")
public class TaskResource {

    private ArrayList<Task> tasks;

    public TaskResource() {
        tasks = new ArrayList<>();  //dummy instance mem
    }
    
    private Task findTask(int id){
        for (Task task : tasks) {
            if (task.get_id() == id) {return task;}
        }
        return null; //UGLY :C
    }

    //helper for testing
    void resetTasks() {
        tasks.clear();
    }

    //fetch all tasks
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    //issue a new task
    @POST 
    @Produces(MediaType.APPLICATION_JSON)
    public Task createTask(Task task) {
        tasks.add(task);
        return task; 
    }

    //assign a user to a task
    @POST 
    @Path("/{id}/participants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignParticipant(@PathParam("id") int id, User user) {
        Task task = findTask(id);
        if (task == null) {return Response.status(Response.Status.NOT_FOUND).build();}
        else {
            task.Assign(user);
            return Response.ok(task).build();
        }      
    }

    @DELETE 
    @Path("/{id}/participants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeParticipant(@PathParam("id") int id, User user) {
        Task task = findTask(id);
        if (task == null) {return Response.status(Response.Status.NOT_FOUND).build();}
        else {
            task.removeUser(user);
            return Response.ok(task).build();
        }      
    }

}
