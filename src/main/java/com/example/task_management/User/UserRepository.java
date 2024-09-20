package com.example.task_management.User;

import org.springframework.data.jpa.repository.JpaRepository;

//Database Interaction
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
