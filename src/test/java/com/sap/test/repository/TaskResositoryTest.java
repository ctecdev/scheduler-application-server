package com.sap.test.repository;

import com.sap.test.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class TaskResositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository repository;

    @Test
    public void testSaveTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName("test-task-name");
        task.setRecurrency("0/35 * * * * *");
        task.setCode("println \"testing repo\";");

        Task result = repository.save(task);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(task.getId(), result.getId());
    }

}
