package com.sap.test.service;

import com.sap.test.model.Task;
import com.sap.test.repository.TaskRepository;
import com.sap.test.scheduler.MyTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MyTaskScheduler scheduler;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findOne(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.orElse(null);
    }

    public void create(Task task) {
        scheduler.schedule(task);
        taskRepository.save(task);
    }

    public void update(Task task) {
        scheduler.cancelFutureSchedulerTask(task.getId());
        scheduler.schedule(task);
        taskRepository.save(task);
    }

    public void delete(String id) {
        scheduler.cancelFutureSchedulerTask(id);
        taskRepository.deleteById(id);
    }

}
