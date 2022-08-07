package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.entity.Operation;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();

    Optional<User> findByUsername(String username);

    void updateUserOperation(long id, Operation operation);
    void updateUserRole(long id, String role);
    void deleteUserByUsername(String username) throws Exception;
}
