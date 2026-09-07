package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.entity.Role;
import com.example.entity.Task;
import com.example.entity.User;
import com.example.service.TaskService;

import jakarta.inject.Inject;
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

    @Inject TaskService taskService;
    


    //fetch all tasks
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)   //Java object -> JSON (serialize)
    public Response getTask(@PathParam ("id") int id) {
        Optional<Task> target = taskService.findTask(id);
        if (target.isPresent()) {return Response.ok(target.get()).build();}
        else {return Response.status(404).build();}
    }

    //issue a new task
    @POST 
    @Consumes(MediaType.APPLICATION_JSON)   //JSON -> Java object (deserialize)
    @Produces(MediaType.APPLICATION_JSON)
    public Task createTask(CreateTaskRequest ctr) {
        Task newTask = taskService.createTask(ctr.title(), ctr.starttime(), ctr.endtime() , ctr.neededRoles(), ctr.issuerID());
        return newTask; 
    }

    //assign a user to a task
    @POST 
    @Path("/{id}/participants/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignParticipant(@PathParam("id") int id, @PathParam("uid") int uid) {
        //TODO error report could be made clearer here with an enum type
        boolean res = taskService.assignTaskParticipant(id, uid);  
        if (res) {return Response.ok().build();}   else {return Response.status(404).build();}
    }

    @DELETE 
    @Path("/{id}/participants/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeParticipant(@PathParam("id") int id, @PathParam("uid") int uid) {
        boolean res = taskService.removeTaskParticipant(id, uid);
         if (res) {return Response.ok().build();}   else {return Response.status(404).build();}
    }

}
