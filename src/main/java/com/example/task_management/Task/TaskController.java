package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Operation(summary = "Get all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get task by id")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new task")
    @PostMapping("/tasks")
    public Task createTask(@RequestBody TaskDTO taskDTO) {
        Optional<User> userOptional = userRepository.findById(taskDTO.getownerid());

        System.out.println(taskDTO.getownerid()+taskDTO.getdescription()+taskDTO.gettitle()+taskDTO.getstatus());
        Task task = new Task();
        task.settitle(taskDTO.gettitle());
        task.setdescription(taskDTO.getdescription());
        task.setstatus(taskDTO.getstatus());
        System.out.println("useroptional "+userOptional.get().getid()+userOptional.get().getpassword());
        // Verificăm și setăm user-ul dacă ownerId nu este null
        if (taskDTO.getownerid() != null) {

            if (userOptional.isPresent()) {
                task.setuser(userOptional.get());
            } else {
                throw new IllegalArgumentException("User-ul cu acest ID nu există");
            }
        } else {
            throw new IllegalArgumentException("Trebuie să specifici un ownerId valid.");
        }

        return taskRepository.save(task);
    }

    //------------------- Shared Tasks ------------------------
    @PostMapping("/{id}/share")
    public ResponseEntity<Task> shareTask(@PathVariable Long id, @RequestBody Set<Long> sharedUserIds) {
        Task updatedTask = taskService.shareTaskWithUsers(id, sharedUserIds);
        return ResponseEntity.ok(updatedTask);
    }
    @PostMapping("/{id}/unshare")
    public ResponseEntity<Task> unshareTask(@PathVariable Long id, @RequestBody Set<Long> sharedUserIds) {
        Task updatedTask = taskService.unshareTaskWithUsers(id, sharedUserIds);
        return ResponseEntity.ok(updatedTask);
    }
    //---------------------------------------------------------

    @Operation(summary = "Update an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDTO task) {
        Task updatedTask = taskService.updateTask(id, task);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
