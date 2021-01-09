package com.sap.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.test.model.Task;
import com.sap.test.service.TaskService;
import com.sap.test.util.Helper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(TaskController.class)
@RunWith(SpringRunner.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    public void testGetOutput() throws Exception {
        List<String> listOutput = new ArrayList<>();
        listOutput.add("This is some random output");
        listOutput.add("Testing output");
        listOutput.add("Using custom StringWriter with name TaskStringWriter");

        Mockito.when(taskService.getOutput()).thenReturn(listOutput);

        String url = "/api/tasks/output";

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(listOutput);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    public void testGetAllTasks() throws Exception {
        List<Task> listTasks = new ArrayList<>();
        listTasks.add(new Task("1","Task1","cron1","groovy1"));
        listTasks.add(new Task("2","Task2","cron2","groovy2"));
        listTasks.add(new Task("3","Task3","cron3","groovy3"));
        listTasks.add(new Task("4","Task4","cron4","groovy4"));
        listTasks.add(new Task("5","Task5","cron5","groovy5"));
        Mockito.when(taskService.findAll()).thenReturn(listTasks);

        String url = "/api/tasks";

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(listTasks);

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
    }

    @Test
    public void testGetOneTask() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(taskService.findOne(id)).thenReturn(new Task());

        String url = "/api/tasks/" + id;

        mockMvc.perform(get(url)).andExpect(status().isOk());

        Mockito.verify(taskService, Mockito.times(1)).findOne(id);
    }

    @Test
    public void testGetOneTaskNotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(taskService.findOne(id)).thenReturn(null);

        String url = "/api/tasks/" + id;

        mockMvc.perform(get(url)).andExpect(status().isNotFound());

        Mockito.verify(taskService, Mockito.times(1)).findOne(id);
    }

    @Test
    public void testCreateTask() throws Exception {

        Task newTask = new Task("Task11", "0/1 * * * * *", "println \"Testing...\"");
        String newJsonTask = objectMapper.writeValueAsString(newTask);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).create(Mockito.any());

        MvcResult mvcResult = mockMvc.perform(
                post(url).contentType("application/json").content(newJsonTask))
                .andExpect(status().isCreated())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        Task actualTaskResult = objectMapper.readValue(actualJsonResponse, Task.class);

        assertThat(Helper.isUUID(actualTaskResult.getId()));

        actualTaskResult.setId(null);
        String actualJson = objectMapper.writeValueAsString(actualTaskResult);
        assertThat(actualJson).isEqualToIgnoringWhitespace(newJsonTask);

    }

    @Test
    public void testCreateTaskNameMustNotBeBlank() throws Exception {

        Task task = new Task("", "0/1 * * * * *", "println \"Testing...\"");
        String jsonTask = objectMapper.writeValueAsString(task);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).create(Mockito.any());

        mockMvc.perform(
                post(url).contentType("application/json").content(jsonTask))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.times(0)).create(task);
    }

    @Test
    public void testCreateTaskNameIsToShort() throws Exception {

        Task task = new Task("T", "0/1 * * * * *", "println \"Testing...\"");
        String jsonTask = objectMapper.writeValueAsString(task);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).create(Mockito.any());

        mockMvc.perform(
                post(url).contentType("application/json").content(jsonTask))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.times(0)).create(task);
    }

    @Test
    public void testCreateTaskReccurencyMustNotBeBlank() throws Exception {

        Task task = new Task("TaskName1", "", "println \"Testing...\"");
        String jsonTask = objectMapper.writeValueAsString(task);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).create(Mockito.any());

        mockMvc.perform(
                post(url).contentType("application/json").content(jsonTask))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.times(0)).create(task);
    }

    @Test
    public void testCreateTaskCodeMustNotBeBlank() throws Exception {

        Task task = new Task("TaskName1", "0/1 * * * * *", "");
        String jsonTask = objectMapper.writeValueAsString(task);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).create(Mockito.any());

        mockMvc.perform(
                post(url).contentType("application/json").content(jsonTask))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.times(0)).create(task);
    }

    @Test
    public void testUpdateTask() throws Exception {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, "Task11", "0/1 * * * * *", "println \"Testing...\"");
        String jsonTask = objectMapper.writeValueAsString(task);
        String url = "/api/tasks";

        Mockito.doNothing().when(taskService).update(Mockito.any());

        MvcResult mvcResult = mockMvc.perform(
                put(url).contentType("application/json").content(jsonTask))
                .andExpect(status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(jsonTask);
    }

    @Test
    public void testDeleteTask() throws Exception {
        String id = UUID.randomUUID().toString();

        Mockito.when(taskService.findOne(id)).thenReturn(new Task());
        Mockito.doNothing().when(taskService).delete(id);

        String url = "/api/tasks/" + id;

        mockMvc.perform(delete(url)).andExpect(status().isOk());
        Mockito.verify(taskService, Mockito.times(1)).findOne(id);
        Mockito.verify(taskService, Mockito.times(1)).delete(id);
    }

    @Test
    public void testDeleteTaskNotFound() throws Exception {
        String id = UUID.randomUUID().toString();

        Mockito.when(taskService.findOne(id)).thenReturn(null);

        String url = "/api/tasks/" + id;

        mockMvc.perform(delete(url)).andExpect(status().isNotFound());
        Mockito.verify(taskService, Mockito.times(1)).findOne(id);
    }

    @Test
    public void testNameExists() throws Exception{
        String url = "/api/tasks/val/name";

        String name = "TaskName1";
        Mockito.when(taskService.nameExists(name)).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(
                get(url).param("name", name))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(Boolean.valueOf(actual));

    }

    @Test
    public void testIsCronValid() throws Exception {
        String url = "/api/tasks/val/cron";

        String cron = "1/1 * * * * *";
        MvcResult mvcResult = mockMvc.perform(
                get(url).param("cron", cron))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(Boolean.valueOf(actual));
    }

    @Test
    public void testIsCodeValidTrue() throws Exception {
        String url = "/api/tasks/val/code";

        String code = "println \"Hello\";";
        MvcResult mvcResult = mockMvc.perform(
                get(url).param("code", code))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(Boolean.valueOf(actual));
    }

}
