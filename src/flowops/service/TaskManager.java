package flowops.service;

import flowops.domain.Priority;
import flowops.domain.Status;
import flowops.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

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

    public int countTasksByStatus(Status status) {
        int count = 0;

        for (Task task : tasks) {
            if (task.getCurrentStatus() == status) {
                count++;
            }
        }

        return count;
    }
}