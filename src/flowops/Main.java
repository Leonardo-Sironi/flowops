package flowops;

import flowops.domain.*;
import flowops.service.EmailNotificationService;
import flowops.service.NotificationService;
import flowops.service.TaskService;
import flowops.service.TaskManager;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Employee employee1 = new Employee(
                1,
                "Leonardo",
                "leonardo@email.com"
        );

        Employee employee2 = new Employee(
                2,
                "Ana",
                "ana@email.com"
        );

        Manager manager = new Manager(
                3,
                "Carlos",
                "carlos@email.com"
        );

        NotificationService notificationService = new EmailNotificationService();
        TaskService taskService = new TaskService(notificationService);
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(
                "Review contract",
                "Review customer contract",
                LocalDate.now().plusDays(2),
                Priority.HIGH
        );

        Task task2 = new Task(
                "Prepare report",
                "Prepare weekly operational report",
                LocalDate.now().minusDays(1),
                Priority.CRITICAL
        );

        Task task3 = new Task(
                "Update customer data",
                "Update customer information in the system",
                LocalDate.now().plusDays(5),
                Priority.MEDIUM
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        task1.assignEmployee(employee1);
        task2.assignEmployee(employee2);

        task1.startTask();
        task2.startTask();

        taskService.completeTask(task1);

        manager.approveTask(task1);

        System.out.println("----- TASK 1 -----");
        System.out.println("Title: " + task1.getTitle());
        System.out.println("Status: " + task1.getCurrentStatus());
        System.out.println("Review: " + task1.getReview());

        System.out.println();

        System.out.println("----- OVERDUE TASKS -----");

        for (Task task : taskManager.getOverdueTasks()) {
            System.out.println(task.getTitle());
        }

        System.out.println();

        System.out.println("----- METRICS -----");

        System.out.println(
                "Pending: "
                        + taskManager.countTasksByStatus(Status.PENDING)
        );

        System.out.println(
                "In progress: "
                        + taskManager.countTasksByStatus(Status.IN_PROGRESS)
        );

        System.out.println(
                "Completed: "
                        + taskManager.countTasksByStatus(Status.COMPLETED)
        );

        System.out.println();

        System.out.println("----- CRITICAL TASKS -----");

        for (Task task : taskManager.getTasksByPriority(Priority.CRITICAL)) {
            System.out.println(task.getTitle());
        }
    }
}