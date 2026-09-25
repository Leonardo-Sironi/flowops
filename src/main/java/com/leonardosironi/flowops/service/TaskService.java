package com.leonardosironi.flowops.service;

import com.leonardosironi.flowops.domain.Employee;
import com.leonardosironi.flowops.domain.Task;
import com.leonardosironi.flowops.dto.CreateTaskRequest;
import com.leonardosironi.flowops.dto.EmployeeResponse;
import com.leonardosironi.flowops.dto.TaskResponse;
import com.leonardosironi.flowops.exception.EmployeeNotFoundException;
import com.leonardosironi.flowops.exception.TaskNotFoundException;
import com.leonardosironi.flowops.repository.EmployeeRepository;
import com.leonardosironi.flowops.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public TaskService(
            TaskRepository taskRepository,
            EmployeeRepository employeeRepository
    ) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<TaskResponse> getTasks() {
        List<TaskResponse> responses = new ArrayList<>();

        for (Task task : taskRepository.findAll()) {
            responses.add(toResponse(task));
        }

        return responses;
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskById(id);

        return toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getDeadline(),
                request.getPriority()
        );

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    @Transactional
    public TaskResponse assignEmployee(Long taskId, Long employeeId) {
        Task task = findTaskById(taskId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found")
                );

        task.assignEmployee(employee);

        return toResponse(task);
    }

    @Transactional
    public TaskResponse startTask(Long id) {
        Task task = findTaskById(id);

        task.startTask();

        return toResponse(task);
    }

    @Transactional
    public TaskResponse completeTask(Long id) {
        Task task = findTaskById(id);

        task.completeTask();

        return toResponse(task);
    }

    @Transactional
    public TaskResponse approveTask(Long id) {
        Task task = findTaskById(id);

        task.approveReview();

        return toResponse(task);
    }

    @Transactional
    public TaskResponse rejectTask(Long id) {
        Task task = findTaskById(id);

        task.rejectReview();

        return toResponse(task);
    }

    @Transactional
    public TaskResponse cancelTask(Long id) {
        Task task = findTaskById(id);

        task.cancelTask();

        return toResponse(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found")
                );
    }

    private TaskResponse toResponse(Task task) {

        EmployeeResponse employeeResponse = null;

        if (task.getEmployeeInCharge() != null) {
            employeeResponse = new EmployeeResponse(
                    task.getEmployeeInCharge().getId(),
                    task.getEmployeeInCharge().getName(),
                    task.getEmployeeInCharge().getEmail()
            );
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getCurrentStatus(),
                task.getReview(),
                task.isLate(),
                employeeResponse
        );
    }
}