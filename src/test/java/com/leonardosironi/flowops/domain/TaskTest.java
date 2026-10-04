package com.leonardosironi.flowops.domain;

import com.leonardosironi.flowops.exception.InvalidTaskOperationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task createTask() {
        return new Task(
                "Implement tests",
                "Create automated tests",
                LocalDate.now().plusDays(5),
                Priority.HIGH
        );
    }

    @Test
    void shouldCreateTaskWithPendingStatus() {
        Task task = createTask();

        assertEquals(Status.PENDING, task.getCurrentStatus());
        assertEquals(RevisionStatus.NOT_REVIEWED, task.getReview());
    }

    @Test
    void shouldAssignEmployeeToTask() {
        Task task = createTask();
        Employee employee = new Employee(
                "Leonardo",
                "leonardo@example.com"
        );

        task.assignEmployee(employee);

        assertSame(employee, task.getEmployeeInCharge());
    }

    @Test
    void shouldStartTaskWithAssignedEmployee() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );

        task.startTask();

        assertEquals(Status.IN_PROGRESS, task.getCurrentStatus());
    }

    @Test
    void shouldNotStartTaskWithoutEmployee() {
        Task task = createTask();

        assertThrows(
                InvalidTaskOperationException.class,
                task::startTask
        );
    }

    @Test
    void shouldCompleteTaskAndRequestReview() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );
        task.startTask();

        task.completeTask();

        assertEquals(Status.COMPLETED, task.getCurrentStatus());
        assertEquals(RevisionStatus.PENDING_REVIEW, task.getReview());
    }

    @Test
    void shouldNotCompletePendingTask() {
        Task task = createTask();

        assertThrows(
                InvalidTaskOperationException.class,
                task::completeTask
        );
    }

    @Test
    void shouldCancelPendingTask() {
        Task task = createTask();

        task.cancelTask();

        assertEquals(Status.CANCELED, task.getCurrentStatus());
    }

    @Test
    void shouldNotCancelCompletedTask() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );
        task.startTask();
        task.completeTask();

        assertThrows(
                InvalidTaskOperationException.class,
                task::cancelTask
        );
    }

    @Test
    void shouldApproveCompletedTaskReview() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );
        task.startTask();
        task.completeTask();

        task.approveReview();

        assertEquals(RevisionStatus.APPROVED, task.getReview());
    }

    @Test
    void shouldRejectCompletedTaskReview() {
        Task task = createTask();
        task.assignEmployee(
                new Employee("Leonardo", "leonardo@example.com")
        );
        task.startTask();
        task.completeTask();

        task.rejectReview();

        assertEquals(RevisionStatus.REJECTED, task.getReview());
    }

    @Test
    void shouldNotApproveTaskWithoutPendingReview() {
        Task task = createTask();

        assertThrows(
                InvalidTaskOperationException.class,
                task::approveReview
        );
    }

    @Test
    void shouldDetectLateTask() {
        Task task = new Task(
                "Overdue task",
                "Test deadline",
                LocalDate.now().minusDays(1),
                Priority.HIGH
        );

        assertTrue(task.isLate());
    }

    @Test
    void shouldNotMarkFutureTaskAsLate() {
        Task task = createTask();

        assertFalse(task.isLate());
    }

    @Test
    void shouldNotMarkCanceledTaskAsLate() {
        Task task = new Task(
                "Old task",
                "Canceled task",
                LocalDate.now().minusDays(1),
                Priority.HIGH
        );

        task.cancelTask();

        assertFalse(task.isLate());
    }
}