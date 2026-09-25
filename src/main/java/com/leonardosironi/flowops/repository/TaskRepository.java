package com.leonardosironi.flowops.repository;

import com.leonardosironi.flowops.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository <Task, Long> {
}
