package com.example.task_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        task.setcreatedAt(LocalDateTime.now()); // Setează createdAt la momentul creării
        task.setupdatedAt(LocalDateTime.now()); // Setează updatedAt la momentul creării
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        existingTask.settitle(task.gettitle());
        existingTask.setdescription(task.getdescription());
        existingTask.setstatus(task.getstatus());
        existingTask.setupdatedAt(LocalDateTime.now()); // Actualizează updatedAt
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
