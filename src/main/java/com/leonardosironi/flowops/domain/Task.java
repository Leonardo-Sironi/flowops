package com.leonardosironi.flowops.domain;

import com.leonardosironi.flowops.exception.InvalidTaskOperationException;
import java.time.LocalDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.*;

@Entity
public class Task {
    private String title;
    private String description;
    private LocalDate deadline;
    @ManyToOne
    private Employee employeeInCharge;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status currentStatus;
    @Enumerated(EnumType.STRING)
    private RevisionStatus review;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Task() {
    }

    public Task(String title, String description, LocalDate deadline, Priority priority) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.currentStatus = Status.PENDING;
        this.review = RevisionStatus.NOT_REVIEWED;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Priority getPriority() { return priority; }

    public Long getId() { return id; }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public Employee getEmployeeInCharge() {
        return employeeInCharge;
    }

    public RevisionStatus getReview() {
        return review;
    }

    public void setId(Long id) { this.id = id; }

    public void startTask() {
        if (employeeInCharge == null) {
            throw new InvalidTaskOperationException(
                    "Task must have an employee assigned before starting"
            );
        }

        if (currentStatus != Status.PENDING) {
            throw new InvalidTaskOperationException(
                    "Only pending tasks can be started"
            );
        }

        currentStatus = Status.IN_PROGRESS;
    }

    public void assignEmployee(Employee employee) {
        if (currentStatus == Status.COMPLETED || currentStatus == Status.CANCELED) {
            throw new InvalidTaskOperationException(
                    "Cannot assign employee to a completed or canceled task"
            );
        }

        this.employeeInCharge = employee;
    }

    public void completeTask() {
        if (currentStatus != Status.IN_PROGRESS) {
            throw new InvalidTaskOperationException(
                    "Only tasks in progress can be completed"
            );
        }

        currentStatus = Status.COMPLETED;
        review = RevisionStatus.PENDING_REVIEW;
    }

    public void cancelTask() {
        if (currentStatus == Status.COMPLETED) {
            throw new InvalidTaskOperationException(
                    "Completed tasks cannot be canceled"
            );
        }

        currentStatus = Status.CANCELED;
    }

    public void changeDeadline(LocalDate newDeadline) {
        if (currentStatus == Status.COMPLETED || currentStatus == Status.CANCELED) {
            throw new InvalidTaskOperationException(
                    "Cannot change deadline of a completed or canceled task"
            );
        }

        this.deadline = newDeadline;
    }

    public boolean isLate() {
        if (currentStatus == Status.COMPLETED || currentStatus == Status.CANCELED) {
            return false;
        }

        return LocalDate.now().isAfter(deadline);
    }

    public void approveReview() {
        if (review != RevisionStatus.PENDING_REVIEW) {
            throw new InvalidTaskOperationException(
                    "Only tasks pending review can be approved"
            );
        }

        review = RevisionStatus.APPROVED;
    }

    public void rejectReview() {
        if (review != RevisionStatus.PENDING_REVIEW) {
            throw new InvalidTaskOperationException(
                    "Only tasks pending review can be rejected"
            );
        }

        review = RevisionStatus.REJECTED;
    }
}
