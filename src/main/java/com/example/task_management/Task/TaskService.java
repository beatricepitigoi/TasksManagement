package com.example.task_management.Task;

import com.example.task_management.Task.DTO.TaskDTO;
import com.example.task_management.User.User;
import com.example.task_management.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
}
