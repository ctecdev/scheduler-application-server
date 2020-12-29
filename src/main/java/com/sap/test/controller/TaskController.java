package com.sap.test.controller;

import com.sap.test.model.Task;
import com.sap.test.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value="api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET ALL
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // GET ONE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Task> getOneTask(@PathVariable String id) {
        Task task = taskService.findOne(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // CREATE
    @RequestMapping(method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<Task> saveTask(@RequestBody Task task){
        task.setId(UUID.randomUUID().toString());
        taskService.save(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    // UPDATE
    @RequestMapping(method=RequestMethod.PUT, consumes="application/json")
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        taskService.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // DELETE
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
