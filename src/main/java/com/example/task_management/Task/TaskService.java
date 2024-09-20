package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        task.setcreatedAt(LocalDateTime.now());
        task.setupdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();


            task.settitle(taskDTO.gettitle());
            task.setdescription(taskDTO.getdescription());
            task.setstatus(taskDTO.getstatus());


            if (taskDTO.getownerid() != null) {
                Optional<User> userOptional = userRepository.findById(taskDTO.getownerid());
                userOptional.ifPresent(task::setuser);
            }

            task.setupdatedAt(LocalDateTime.now());


            return taskRepository.save(task);
        }
        return null; // Returnăm null dacă task-ul nu există
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


    //-------------------- Shared tasks ----------------------------
    public Task shareTaskWithUsers(Long taskId, Set<Long> userIds) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Set<User> sharedUsers = task.getsharedusers(); // Obținem utilizatorii existenți

            for (Long userId : userIds) {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    sharedUsers.add(userOptional.get()); // Adăugăm utilizatorul la set
                }
            }

            task.setsharedusers(sharedUsers);
            task.setupdatedAt(LocalDateTime.now());
            return taskRepository.save(task); // Salvăm task-ul cu utilizatorii partajați
        }

        throw new IllegalArgumentException("Task-ul nu a fost găsit.");
    }


    public Task unshareTaskWithUsers(Long taskId, Set<Long> userIds) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Set<User> sharedUsers = task.getsharedusers();

            for (Long userId : userIds) {
                sharedUsers.removeIf(user -> user.getid().equals(userId)); // Scoatem utilizatorul din set
            }

            task.setsharedusers(sharedUsers);
            task.setupdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }

        throw new IllegalArgumentException("Task-ul nu a fost găsit.");
    }

}
