package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


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



    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDTO task) {
        Task updatedTask = taskService.updateTask(id, task);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
