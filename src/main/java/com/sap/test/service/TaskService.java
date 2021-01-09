package com.sap.test.service;

import com.sap.test.model.Task;
import com.sap.test.repository.TaskRepository;
import com.sap.test.scheduler.MyTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MyTaskScheduler scheduler;
    @Autowired
    private TaskStringWriter writer;

    public List<String> getOutput() {
        String[] outputArr = writer.toString().split("\\r?\\n");
        writer.getBuffer().setLength(0);
        return Arrays.asList(outputArr);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }

    public Boolean nameExists(String name) {
        Optional<Task> optionalTask = taskRepository.findOneByName(name);
        return optionalTask.isPresent();
    }

    @Transactional
    public void create(Task task) {
        scheduler.schedule(task);
        taskRepository.save(task);
    }

    @Transactional
    public void update(Task task) {
        scheduler.cancelFutureSchedulerTask(task.getId());
        scheduler.schedule(task);
        taskRepository.save(task);
    }

    @Transactional
    public void delete(String id) {
        scheduler.cancelFutureSchedulerTask(id);
        taskRepository.deleteById(id);
    }


}
