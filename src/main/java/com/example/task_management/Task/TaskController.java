package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.Security.UserDetailsImpl;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    /*
    @Operation(summary = "Get tasks by user")
    @GetMapping()
    public ResponseEntity<Optional<Task>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        //Collection<?> authorities =  ((UserDetailsImpl) authentication.getPrincipal()).getAuthorities();
        //System.out.println(authorities);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Optional<Task> tasks = taskRepository.findByUser(user);
        return ResponseEntity.ok(tasks);
    }

     */

    @Operation(summary = "Get tasks by user")
    @GetMapping()
    public ResponseEntity<Optional<List<Task>>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the principal is an in-memory user
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            // Handle in-memory user
            org.springframework.security.core.userdetails.User userDetails2 =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // Get roles
            Collection<GrantedAuthority> authorities = userDetails2.getAuthorities();
            Set<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            System.out.println("In-memory user roles: " + roles);

            // If the user has the admin role, return all tasks
            if (roles.contains("ROLE_ADMIN")) {
                List<Task> allTasks = taskService.getAllTasks(); // Assuming this returns a List<Task>
                return ResponseEntity.ok(Optional.ofNullable(allTasks)); // Wrap the result in ResponseEntity
            }
        } else {
            // Handle user from your database (e.g. UserDetailsImpl)
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String username = userDetails.getUsername(); // Get username from token

            // Fetch the user from the database
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

            // Get tasks for the specific user
            Optional<List<Task>> userTasks = taskRepository.findByUser(user);
            return ResponseEntity.ok(userTasks);
        }

        // If the user doesn't match any conditions, return an empty response
        return ResponseEntity.ok(Optional.empty());
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
        return taskService.createTask(taskDTO);
    }

    //------------------- Shared Tasks ------------------------
    @Operation(summary = "Share the task id with users")
    @PostMapping("/{id}/share")
    public ResponseEntity<Task> shareTask(@PathVariable Long id, @RequestBody Set<Long> sharedUserIds) {
        Task updatedTask = taskService.shareTaskWithUsers(id, sharedUserIds);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Share the task id with users")
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
