package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.Security.UserDetailsImpl;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails2 =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            Collection<GrantedAuthority> authorities = userDetails2.getAuthorities();
            Set<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            //System.out.println("In-memory user roles: " + roles);
            if (roles.contains("ROLE_ADMIN")) {
                List<Task> allTasks = taskService.getAllTasks();
                return ResponseEntity.ok(Optional.ofNullable(allTasks));
            }
        } else {

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
            Optional<List<Task>> userTasks = taskRepository.findByUser(user);
            return ResponseEntity.ok(userTasks);
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        taskDTO.setownerid(user.getid());
        return taskService.createTask(taskDTO);
    }

    @Operation(summary = "Update an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        // Get the authentication information of the logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return ResponseEntity.notFound().build(); // Task not found
        }
        Task task = optionalTask.get();
        if (!task.getuser().getid().equals(user.getid())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Forbidden: You do not have permission to modify this task."); // Custom message
        }
        taskDTO.setownerid(user.getid());
        Task updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }



    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


    // ------------------- Shared Tasks ------------------------
    @Operation(summary = "Share the task id with users")
    @PostMapping("/{id}/share")
    public ResponseEntity<Task> shareTask(@PathVariable Long id, @RequestBody Set<Long> sharedUserIds) {
        Task updatedTask = taskService.shareTaskWithUsers(id, sharedUserIds);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Unshare the task id with users")
    @PostMapping("/{id}/unshare")
    public ResponseEntity<Task> unshareTask(@PathVariable Long id, @RequestBody Set<Long> sharedUserIds) {
        Task updatedTask = taskService.unshareTaskWithUsers(id, sharedUserIds);
        return ResponseEntity.ok(updatedTask);
    }
    // ---------------------------------------------------------


}
