package com.sap.test.repository;

import com.sap.test.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {

    Optional<Task> findOneByName(String name);

}
