package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.task_management.Task.DTO.TaskResponseDTO;
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

    @Transactional
    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found")));
    }

    public Task createTask(TaskDTO taskDTO) {
        Optional<User> userOptional = userRepository.findById(taskDTO.getownerid());
        Task task = new Task();
        task.settitle(taskDTO.gettitle());
        task.setdescription(taskDTO.getdescription());
        task.setstatus(taskDTO.getstatus());
        //System.out.println("useroptional "+userOptional.get().getid()+userOptional.get().getpassword());
        if (taskDTO.getownerid() != null) {
            if (userOptional.isPresent()) {
                task.setuser(userOptional.get());
            } else {
                throw new IllegalArgumentException("User-ul cu acest ID nu există");
            }
        } else {
            throw new IllegalArgumentException("Trebuie să specifici un ownerId valid.");
        }
        task.setcreatedAt(LocalDateTime.now());
        task.setupdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {
        Optional<User> userOptional = userRepository.findById(taskDTO.getownerid());
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if(taskDTO.gettitle() != null) {
                task.settitle(taskDTO.gettitle());
            }
            if(taskDTO.getdescription() != null) {
                task.setdescription(taskDTO.getdescription());
            }
            if(taskDTO.getstatus() != null) {
                task.setstatus(taskDTO.getstatus());
            }

            if (taskDTO.getownerid() != null) {

                if (userOptional.isPresent()) {
                    task.setuser(userOptional.get());
                } else {
                    throw new IllegalArgumentException("User-ul cu acest ID nu există");
                }
            } else {
                throw new IllegalArgumentException("Trebuie să specifici un ownerId valid.");
            }


            task.setupdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null; //task ul nu exista
    }

/*
    public TaskResponseDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Update task fields based on the DTO
            if (taskDTO.gettitle() != null) {
                task.settitle(taskDTO.gettitle());
            }
            if (taskDTO.getdescription() != null) {
                task.setdescription(taskDTO.getdescription());
            }
            if (taskDTO.getstatus() != null) {
                task.setstatus(taskDTO.getstatus());
            }

            // Update the timestamp
            task.setupdatedAt(LocalDateTime.now());

            taskRepository.save(task);

            // Create and return a TaskResponseDTO
            TaskResponseDTO responseDTO = new TaskResponseDTO();
            responseDTO.settitle(task.gettitle());
            responseDTO.setdescription(task.getdescription());
            responseDTO.setstatus(task.getstatus());

            return responseDTO;
        }
        return null;
    }
*/

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

// -------------------- Shared tasks ----------------------------
    @Transactional
    public Task shareTaskWithUsers(Long taskId, Set<Long> userIds) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Set<User> sharedUsers = task.getsharedusers();
            for (Long userId : userIds) {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    sharedUsers.add(userOptional.get());
                }
            }
            task.setsharedusers(sharedUsers);
            task.setupdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
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
// ----------------------------------------------------------------
}
