package com.sap.test.service;

import com.sap.test.model.Task;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Date;

@Service
public class TaskStringWriter extends StringWriter {
    private Task task;

    public TaskStringWriter() {
        super();
    }

    @Override
    public void write(String str, int off, int len) {
        if (task != null) {
            str = format(str);
        }
        super.getBuffer().append(str, 0, str.length());
    }

    private String format(String output) {
        return String.format("%s | Task name: %s | Thread: %s | Output: %s", new Date(), task.getName(), Thread.currentThread().getName(), output);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
