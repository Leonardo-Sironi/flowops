package flowops.domain;

public class Manager extends Employee {
    public Manager(int id, String name, String email) {
        super(id, name, email);
    }

    public void approveTask(Task task) {
        task.approveReview();
    }

    public void rejectTask(Task task) {
        task.rejectReview();
    }
}
