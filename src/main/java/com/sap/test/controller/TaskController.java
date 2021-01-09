package com.sap.test.controller;

import com.sap.test.model.Task;
import com.sap.test.service.TaskService;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value="api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET OUTPUT
    @RequestMapping(value = "/output", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getOutput() {
        List<String> output = taskService.getOutput();
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

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
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CREATE
    @RequestMapping(method=RequestMethod.POST, consumes="application/json")
    public ResponseEntity<Object> createTask(@Valid @RequestBody Task task, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            task.setId(UUID.randomUUID().toString());
            taskService.create(task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }
    }

    // UPDATE
    @RequestMapping(method=RequestMethod.PUT, consumes="application/json")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            taskService.update(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
    }

    // DELETE
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        Task task = taskService.findOne(id);
        if (task != null) {
            taskService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Name exists
    @RequestMapping(value = "/val/name", method = RequestMethod.GET)
    public ResponseEntity<Boolean> nameExists(@RequestParam("name") String name) {
        Boolean nameExists = taskService.nameExists(name);
        return new ResponseEntity<>(nameExists, HttpStatus.OK);
    }

    // Is cron valid
    @RequestMapping(value = "/val/cron", method = RequestMethod.GET)
    public ResponseEntity<Boolean> isCronValid(@RequestParam("cron") String cron) {
        Boolean isValid = false;
        try {
           new CronTrigger(cron);
           isValid = true;
        } catch (Exception e) {
            // Invalid cron expression
        }
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    // is groovy script valid
    @RequestMapping(value = "/val/code", method = RequestMethod.GET)
    public ResponseEntity<Boolean> isCodeValid(@RequestParam("code") String code) {
        Boolean isValid = false;
        try {
            new GroovyShell().parse(code);
            isValid = true;
        } catch (Exception e) {
            // Invalid expression
        }
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

}
