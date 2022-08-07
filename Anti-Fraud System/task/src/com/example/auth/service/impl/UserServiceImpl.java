package com.example.auth.service.impl;

import com.example.auth.entity.User;
import com.example.auth.entity.Operation;
import com.example.auth.entity.Role;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        if(user.roleIsEmpty()){
            if(userRepository.findAllByOrderById().size()==0){
                user.setRole(Role.ADMINISTRATOR.getName());
            }else{
                user.setRole(Role.MERCHANT.getName());
            }
        }
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        return  userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByOrderById();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username);
    }

    @Override
    public void updateUserOperation(long id, Operation operation) {
        userRepository.updateOperation(id,operation);
    }

    @Override
    public void updateUserRole(long id, String role) {
        userRepository.updateRole(id,role);
    }

    @Override
    public void deleteUserByUsername(String username) throws Exception {
        Optional<User> optionalUser = userRepository.findUserByUsernameIgnoreCase(username);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new Exception("User not found");
        }
    }

}
