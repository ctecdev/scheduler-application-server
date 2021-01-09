package com.sap.test.scheduler;

import com.sap.test.model.Task;
import com.sap.test.service.TaskService;
import com.sap.test.service.TaskStringWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class MyTaskScheduler implements CommandLineRunner {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    TaskStringWriter writer;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    // Run tasks that are saved in db when app starts
    @Override
    public void run(String... args) throws IOException {
        taskService.findAll().forEach(task -> {
            writer.setTask(task);
            ScheduledFuture<?> future = taskScheduler.schedule(new RunnableTask(task, writer), new CronTrigger(task.getRecurrency()));
            scheduledTasks.put(task.getId(), future);
        });
    }

    public ScheduledFuture<?> schedule(Task task) {
        writer.setTask(task);
        RunnableTask runnableTask = new RunnableTask(task, writer);
        CronTrigger cronTrigger = new CronTrigger(task.getRecurrency());
        ScheduledFuture<?> future = taskScheduler.schedule(runnableTask, cronTrigger);
        scheduledTasks.put(runnableTask.getTask().getId(), future);
        return future;
    }

    public void cancelFutureSchedulerTask(String id) {
        scheduledTasks.get(id).cancel(false);
        scheduledTasks.remove(id);
    }

    public void scheduleSavedTasks() {
        taskService.findAll().forEach(task -> {
            writer.setTask(task);
            ScheduledFuture<?> future = taskScheduler.schedule(new RunnableTask(task, writer), new CronTrigger(task.getRecurrency()));
            scheduledTasks.put(task.getId(), future);
        });
    }

}