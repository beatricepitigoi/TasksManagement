package com.example.task_management.Task;

import com.example.task_management.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<List <Task>> findByUser(User user);
}
