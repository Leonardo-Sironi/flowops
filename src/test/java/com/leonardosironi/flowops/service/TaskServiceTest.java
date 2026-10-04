package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.*;
import com.leonardosironi.flowops.dto.TaskResponse;
import com.leonardosironi.flowops.exception.EmployeeNotFoundException;
import com.leonardosironi.flowops.exception.TaskNotFoundException;
import com.leonardosironi.flowops.repository.EmployeeRepository;
import com.leonardosironi.flowops.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private EmployeeRepository employeeRepository;
    private TaskService taskService;

    private Task createTask() {
        return new Task(
                "Test task",
                "Service test",
                LocalDate.now().plusDays(5),
                Priority.HIGH
        );
    }

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        employeeRepository = mock(EmployeeRepository.class);

        taskService = new TaskService(
                taskRepository,
                employeeRepository
        );
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskRepository.findAll())
                .thenReturn(List.of(createTask(), createTask()));

        List<TaskResponse> result = taskService.getTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldFindTaskById() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(createTask()));

        TaskResponse result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test task", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(99L)
        );
    }

    @Test
    void shouldStartAssignedTask() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        TaskResponse result = taskService.startTask(1L);

        assertEquals(Status.IN_PROGRESS, result.getCurrentStatus());
    }

    @Test
    void shouldNotStartTaskWithoutEmployee() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(createTask()));

        assertThrows(
                RuntimeException.class,
                () -> taskService.startTask(1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(createTask()));

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> taskService.assignEmployee(1L, 99L)
        );
    }

    @Test
    void shouldAssignEmployeeToTask() {
        Task task = createTask();
        Employee employee = new Employee(
                "Leonardo",
                "leonardo@example.com"
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(employeeRepository.findById(2L))
                .thenReturn(Optional.of(employee));

        TaskResponse result = taskService.assignEmployee(1L, 2L);

        assertNotNull(result.getEmployeeInCharge());
        assertEquals(
                "Leonardo",
                result.getEmployeeInCharge().getName()
        );
    }
}