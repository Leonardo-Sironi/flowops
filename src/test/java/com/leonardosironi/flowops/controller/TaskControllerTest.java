
package com.leonardosironi.flowops.controller;

import com.leonardosironi.flowops.domain.*;
import com.leonardosironi.flowops.dto.TaskResponse;
import com.leonardosironi.flowops.exception.InvalidTaskOperationException;
import com.leonardosironi.flowops.exception.TaskNotFoundException;
import com.leonardosironi.flowops.handler.GlobalExceptionHandler;
import com.leonardosironi.flowops.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;

    private Authentication createAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                "employee@example.com",
                "password"
        );
    }

    private TaskResponse createResponse() {
        return new TaskResponse(
                1L,
                "Test task",
                "Testing HTTP endpoints",
                LocalDate.of(2026, 12, 20),
                Priority.HIGH,
                Status.PENDING,
                RevisionStatus.NOT_REVIEWED,
                false,
                null
        );
    }

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        when(taskService.getTasks())
                .thenReturn(List.of(createResponse()));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test task"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(1L))
                .thenReturn(createResponse());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.currentStatus").value("PENDING"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        when(taskService.createTask(any()))
                .thenReturn(createResponse());

        String json = """
                {
                  "title": "Test task",
                  "description": "Testing HTTP endpoints",
                  "deadline": "2026-12-20",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test task"));

        verify(taskService).createTask(any());
    }

    @Test
    void shouldReturn400ForInvalidTask() throws Exception {
        String json = """
                {
                  "title": "",
                  "description": "Testing validation",
                  "deadline": "2026-12-20",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    void shouldAssignEmployee() throws Exception {
        when(taskService.assignEmployee(1L, 2L))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/assign/2"))
                .andExpect(status().isOk());

        verify(taskService).assignEmployee(1L, 2L);
    }

    @Test
    void shouldStartTask() throws Exception {
        Authentication authentication = createAuthentication();

        when(taskService.startTask(eq(1L), any(Authentication.class)))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/start")
                        .principal(authentication))
                .andExpect(status().isOk());

        verify(taskService).startTask(1L, authentication);
    }

    @Test
    void shouldCompleteTask() throws Exception {
        Authentication authentication = createAuthentication();

        when(taskService.completeTask(eq(1L), any(Authentication.class)))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/complete")
                        .principal(authentication))
                .andExpect(status().isOk());

        verify(taskService).completeTask(1L, authentication);
    }

    @Test
    void shouldApproveTask() throws Exception {
        when(taskService.approveTask(1L))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/approve"))
                .andExpect(status().isOk());

        verify(taskService).approveTask(1L);
    }

    @Test
    void shouldRejectTask() throws Exception {
        when(taskService.rejectTask(1L))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/reject"))
                .andExpect(status().isOk());

        verify(taskService).rejectTask(1L);
    }

    @Test
    void shouldCancelTask() throws Exception {
        when(taskService.cancelTask(1L))
                .thenReturn(createResponse());

        mockMvc.perform(patch("/tasks/1/cancel"))
                .andExpect(status().isOk());

        verify(taskService).cancelTask(1L);
    }

    @Test
    void shouldReturn400ForInvalidOperation() throws Exception {
        Authentication authentication = createAuthentication();

        when(taskService.startTask(eq(1L), any(Authentication.class)))
                .thenThrow(new InvalidTaskOperationException(
                        "Invalid task operation"
                ));

        mockMvc.perform(patch("/tasks/1/start")
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid task operation"));
    }
}
