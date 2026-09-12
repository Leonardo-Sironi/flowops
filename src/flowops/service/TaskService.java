package flowops.service;

import flowops.domain.Task;

public class TaskService {

    private NotificationService notificationService;

    public TaskService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void completeTask(Task task){
        task.completeTask();

        notificationService.sendNotification(
                "Task completed and waiting for manager review");
    }
}
