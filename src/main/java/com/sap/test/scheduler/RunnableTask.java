package com.sap.test.scheduler;

import com.sap.test.model.Task;
import com.sap.test.service.TaskStringWriter;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.Getter;

@Getter
public class RunnableTask implements Runnable {

    private Task task;
    private TaskStringWriter writer;

    public RunnableTask(Task task, TaskStringWriter writer) {
        this.task = task;
        this.writer = writer;
    }

    @Override
    public void run() {
        // call groovy expressions from Java code
        writer.setTask(task);
        Binding binding = new Binding();
        binding.setProperty("out", writer);
        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(task.getCode());
    }

}