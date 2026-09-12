package flowops.domain;

import flowops.exception.InvalidTaskOperationException;

import java.time.LocalDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private String title;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private Status currentStatus;
    private Employee employeeInCharge;
    private RevisionStatus review;

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

    public Priority getPriority() {
        return priority;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public Employee getEmployeeInCharge() {
        return employeeInCharge;
    }

    public RevisionStatus getReview() {
        return review;
    }

    public void startTask() {
        if (employeeInCharge == null) {
            throw new InvalidTaskOperationException(
                    "flowops.domain.Task cannot be started without an employee in charge"
            );
        }

        if (currentStatus != Status.PENDING) {
            throw new InvalidTaskOperationException(
                    "flowops.domain.Task cannot be started because it is not pending"
            );
        }

        this.currentStatus = Status.IN_PROGRESS;
    }

    public void completeTask() {
        if (currentStatus != Status.IN_PROGRESS) {
            throw new InvalidTaskOperationException(
                    "flowops.domain.Task can only be completed when it is in progress"
            );
        }

        this.currentStatus = Status.COMPLETED;
        this.review = RevisionStatus.PENDING_REVIEW;
    }

    public void cancelTask() {
        if (currentStatus == Status.COMPLETED) {
            throw new InvalidTaskOperationException(
                    "A completed task cannot be canceled"
            );
        }

        this.currentStatus = Status.CANCELED;
    }

    public void changeDeadline(LocalDate newDeadline) {
        if (currentStatus == Status.COMPLETED) {
            throw new InvalidTaskOperationException(
                    "Cannot change deadline of a completed task"
            );
        }

        if (currentStatus == Status.CANCELED) {
            throw new InvalidTaskOperationException(
                    "Cannot change deadline of a canceled task"
            );
        }

        this.deadline = newDeadline;
    }

    public void assignEmployee(Employee employee) {
        if (currentStatus == Status.COMPLETED) {
            throw new InvalidTaskOperationException(
                    "Cannot assign an employee to a completed task"
            );
        }

        if (currentStatus == Status.CANCELED) {
            throw new InvalidTaskOperationException(
                    "Cannot assign an employee to a canceled task"
            );
        }

        this.employeeInCharge = employee;
    }

    public boolean isLate() {
        if (currentStatus == Status.COMPLETED
                || currentStatus == Status.CANCELED) {
            return false;
        }

        return LocalDate.now().isAfter(deadline);
    }

    public void approveReview() {
        if (review != RevisionStatus.PENDING_REVIEW) {
            throw new InvalidTaskOperationException(
                    "flowops.domain.Task review must be pending before it can be approved"
            );
        }

        this.review = RevisionStatus.APPROVED;
    }

    public void rejectReview() {
        if (review != RevisionStatus.PENDING_REVIEW) {
            throw new InvalidTaskOperationException(
                    "flowops.domain.Task review must be pending before it can be rejected"
            );
        }

        this.review = RevisionStatus.REJECTED;
    }

    public static class TaskManager {
        private List<Task> tasks = new ArrayList<>();


        public void addTask(Task task) {
            tasks.add(task);
        }

        public List<Task> getOverdueTasks() {
            List<Task> overdueTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isLate()) {
                    overdueTasks.add(task);
                }
            }
            return overdueTasks;
        }

        public List<Task> getTasksByStatus(Status status) {
            List<Task> filteredTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getCurrentStatus() == status) {
                    filteredTasks.add(task);
                }
            }
            return filteredTasks;
        }

        public List<Task> getTasksByPriority(Priority priority) {
            List<Task> filteredTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getPriority() == priority) {
                    filteredTasks.add(task);
                }
            }
            return filteredTasks;
        }

        public int countTasksByStatus(Status status){
            int count = 0;

            for (Task task : tasks){
                if (task.getCurrentStatus() == status){
                    count ++;
                }
            } return count;
        }

    }
}