package com.leonardosironi.flowops.dto;

import com.leonardosironi.flowops.domain.Priority;
import com.leonardosironi.flowops.domain.RevisionStatus;
import com.leonardosironi.flowops.domain.Status;

import java.time.LocalDate;

public class TaskResponse {

        private Long id;
        private String title;
        private String description;
        private LocalDate deadline;
        private Priority priority;
        private Status currentStatus;
        private RevisionStatus review;
        private boolean late;

        public TaskResponse(
                Long id,
                String title,
                String description,
                LocalDate deadline,
                Priority priority,
                Status currentStatus,
                RevisionStatus review,
                boolean late
        ) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.deadline = deadline;
            this.priority = priority;
            this.currentStatus = currentStatus;
            this.review = review;
            this.late = late;
        }

        public Long getId() {
            return id;
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

        public RevisionStatus getReview() {
            return review;
        }

        public boolean isLate() {
            return late;
        }
    }
