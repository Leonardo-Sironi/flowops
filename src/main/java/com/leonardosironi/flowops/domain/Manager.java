package com.leonardosironi.flowops.domain;

public class Manager extends Employee {

    public Manager(String name, String email) {
        super(name, email);
    }

    public void approveTask(Task task) {
        task.approveReview();
    }

    public void rejectTask(Task task) {
        task.rejectReview();
    }
}