package com.leonardosironi.flowops.integration;

import com.leonardosironi.flowops.domain.Priority;
import com.leonardosironi.flowops.domain.Task;
import com.leonardosironi.flowops.repository.TaskRepository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskPersistenceIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveTask() {
        Task task = new Task(
                "Integration test",
                "Verify PostgreSQL persistence",
                LocalDate.now().plusDays(7),
                Priority.HIGH
        );

        Task saved = taskRepository.save(task);

        assertNotNull(saved.getId());

        entityManager.flush();
        entityManager.clear();

        Task retrieved = taskRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals("Integration test", retrieved.getTitle());
        assertEquals(Priority.HIGH, retrieved.getPriority());
    }
}