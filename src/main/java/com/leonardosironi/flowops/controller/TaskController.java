package com.leonardosironi.flowops.controller;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.dto.CreateTaskRequest;
import com.leonardosironi.flowops.dto.TaskResponse;
import com.leonardosironi.flowops.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse task = taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }

    @PatchMapping("/{id}/assign")
    public TaskResponse assignEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee
    ) {
        return taskService.assignEmployee(id, employee);
    }

    @PatchMapping("/{id}/start")
    public TaskResponse startTask(@PathVariable Long id) {
        return taskService.startTask(id);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponse completeTask(@PathVariable Long id) {
        return taskService.completeTask(id);
    }

    @PatchMapping("/{id}/approve")
    public TaskResponse approveTask(@PathVariable Long id) {
        return taskService.approveTask(id);
    }

    @PatchMapping("/{id}/reject")
    public TaskResponse rejectTask(@PathVariable Long id) {
        return taskService.rejectTask(id);
    }

    @PatchMapping("/{id}/cancel")
    public TaskResponse cancelTask(@PathVariable Long id) {
        return taskService.cancelTask(id);
    }
}