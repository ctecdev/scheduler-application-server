package com.sap.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.test.model.Task;
import com.sap.test.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @Test
    public void testGetAllTasks() throws Exception {
        String url = "/api/tasks";

        Task task = new Task("random-id", "random-name","* 5 * * * *", "println \"testing get all\";");
        taskService.create(task);

        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Task[] taskArr = objectMapper.readValue(jsonResponse, Task[].class);

        assertThat(taskArr).hasAtLeastOneElementOfType(Task.class);
    }

    @Test
    @Transactional
    public void testCreateTask() throws Exception {
        String url = "/api/tasks";
        Task task = new Task("TestTask-1", "0/30 * * * * *", "println \"Testing...\"");

        MvcResult mvcResult = mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String actualJson = mvcResult.getResponse().getContentAsString();
        Task actualTask = objectMapper.readValue(actualJson, Task.class);

        Task savedTask = taskService.findOne(actualTask.getId());
        String savedJson = objectMapper.writeValueAsString(savedTask);
        assertThat(actualJson).isEqualToIgnoringWhitespace(savedJson);

    }

    @Test
    @Transactional
    public void testCreateUpdateTask() throws Exception {
        String url = "/api/tasks";
        Task task = new Task("TestTask-saved-1", "0/40 * * * * *", "println \"Testing...\"");
        //save
        MvcResult savedMvcResult = mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String savedJson = savedMvcResult.getResponse().getContentAsString();
        Task savedTask = objectMapper.readValue(savedJson, Task.class);
        //change name
        String newName = "TestTask-updated-1";
        savedTask.setName(newName);

        //update
        MvcResult updatedMvcResult = mockMvc.perform(
                put(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String updatedJson = updatedMvcResult.getResponse().getContentAsString();
        Task updatedTask = objectMapper.readValue(updatedJson, Task.class);

        Task dbTask = taskService.findOne(savedTask.getId());
        assertThat(updatedTask.getName()).isEqualTo(dbTask.getName());

    }

    @Test
    @Transactional
    public void testCreateTaskNameMustoNotBeBlank() throws Exception {
        String url = "/api/tasks";
        Task task = new Task("", "0/1 * * * * *", "println \"Testing...\"");

        mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    public void testSaveDeleteTask() throws Exception{
        String createUrl = "/api/tasks";
        Task task = new Task("TestTask-delete-1", "0/40 * * * * *", "println \"Testing...\"");
        //save
        MvcResult savedMvcResult = mockMvc.perform(
                post(createUrl).contentType("application/json")
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String savedJson = savedMvcResult.getResponse().getContentAsString();
        Task savedTask = objectMapper.readValue(savedJson, Task.class);

        //delete
        String deleteUrl = createUrl + "/" + savedTask.getId();
        mockMvc.perform(delete(deleteUrl)).andExpect(status().isOk());

        Task deletedTask = taskService.findOne(savedTask.getId());
        assertThat(deletedTask).isEqualTo(null);
    }


}
