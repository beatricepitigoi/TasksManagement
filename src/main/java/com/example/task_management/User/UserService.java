package com.example.task_management.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(User user) {
        System.out.println(user.getemail()+user.getpassword()+user.getusername());

        if(user.getpassword() == null || user.getpassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        String encodedPassword = passwordEncoder.encode(user.getpassword());
        user.setpassword(encodedPassword);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        System.out.println(user.getemail()+user.getpassword()+user.getusername());

        user.setusername(userDetails.getusername());
        user.setpassword(userDetails.getpassword());
        user.setemail(userDetails.getemail());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.delete(user);
    }
}