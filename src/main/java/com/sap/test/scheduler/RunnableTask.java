package com.sap.test.scheduler;

import com.sap.test.model.Task;
import lombok.Getter;

@Getter
public class RunnableTask implements Runnable {

    private Task task;

    public RunnableTask(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        System.out.println("Runnable Task with name: " + task.getName() + " STARTS on thread " + Thread.currentThread().getName());
        //TODO execute groovy script
    }

}