package com.sap.test.service;

import com.sap.test.model.Task;
import com.sap.test.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

    @MockBean
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    public void testFindOne() {
        String id = UUID.randomUUID().toString();

        Task task = new Task(id, "test-task-name", "cron", "code");
        Optional<Task> optional = Optional.of(task);
        Mockito.when(repository.findById(id)).thenReturn(optional);

        Task result = service.findOne(id);

        Assertions.assertEquals(id, result.getId());
    }

    @Test
    public void testFindOneShouldReturnNull() {
        String id = UUID.randomUUID().toString();

        Optional<Task> optional = Optional.empty();
        Mockito.when(repository.findById(id)).thenReturn(optional);

        Task result = service.findOne(id);

        Assertions.assertNull(result);
    }

    @Test
    public void testNameExists() {
        String name = "test-task-name";

        Optional<Task> optional = Optional.of(new Task());
        Mockito.when(repository.findOneByName(name)).thenReturn(optional);

        boolean result = service.nameExists(name);
        Assertions.assertTrue(result);

    }

}
