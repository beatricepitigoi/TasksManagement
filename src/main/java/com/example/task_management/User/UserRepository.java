package com.example.task_management.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//Database Interaction
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username); //se genereaza automat de JPA
}
