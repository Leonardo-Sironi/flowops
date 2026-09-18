package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.domain.Task;
import com.leonardosironi.flowops.dto.CreateTaskRequest;
import com.leonardosironi.flowops.dto.TaskResponse;
import com.leonardosironi.flowops.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public List<TaskResponse> getTasks() {
        List<TaskResponse> responses = new ArrayList<>();

        for (Task task : tasks) {
            responses.add(toResponse(task));
        }

        return responses;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getDeadline(),
                request.getPriority()
        );

        task.setId(nextId++);
        tasks.add(task);

        return toResponse(task);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskById(id);
        return toResponse(task);
    }

    public TaskResponse assignEmployee(Long taskId, Employee employee) {
        Task task = findTaskById(taskId);

        task.assignEmployee(employee);

        return toResponse(task);
    }

    public TaskResponse startTask(Long id) {
        Task task = findTaskById(id);

        task.startTask();

        return toResponse(task);
    }

    public TaskResponse completeTask(Long id) {
        Task task = findTaskById(id);

        task.completeTask();

        return toResponse(task);
    }

    public TaskResponse approveTask(Long id) {
        Task task = findTaskById(id);

        task.approveReview();

        return toResponse(task);
    }

    public TaskResponse rejectTask(Long id) {
        Task task = findTaskById(id);

        task.rejectReview();

        return toResponse(task);
    }

    public TaskResponse cancelTask(Long id) {
        Task task = findTaskById(id);

        task.cancelTask();

        return toResponse(task);
    }

    private Task findTaskById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }

        throw new TaskNotFoundException("Task not found");
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getCurrentStatus(),
                task.getReview(),
                task.isLate()
        );
    }
}