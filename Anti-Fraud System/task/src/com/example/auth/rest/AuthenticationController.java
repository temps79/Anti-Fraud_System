package com.example.auth.rest;

import com.example.auth.entity.User;
import com.example.auth.entity.Role;
import com.example.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;


    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) return ResponseEntity.badRequest().build();
        try {
            User newUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok(Map.of("username", username, "status", "Deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/role")
    public ResponseEntity<?> updateRole(@RequestBody User user) {
        Optional<User> findUserOptional = userService.findByUsername(user.getUsername());

        if (findUserOptional.isEmpty()) return ResponseEntity.notFound().build();

        User findUser = findUserOptional.get();

        if (!user.getRoleStr().equals(Role.MERCHANT.getName()) && !user.getRoleStr().equals(Role.SUPPORT.getName())) {
            return ResponseEntity.badRequest().build();
        }
        if (findUser.getRole().equals(user.getRoleStr())) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        findUser.setRole(user.getRoleStr());
        userService.updateUserRole(findUser.getId(),findUser.getRoleStr());
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }

    @PutMapping("/access")
    public ResponseEntity<?> access(@RequestBody User user) {
        Optional<User> findUserOptional = userService.findByUsername(user.getUsername());

        if (findUserOptional.isEmpty()) return ResponseEntity.notFound().build();

        User findUser = findUserOptional.get();
        if (findUser.getRole().equals(Role.ADMINISTRATOR.getValue())) return ResponseEntity.badRequest().build();

        findUser.setOperation(user.getOperation());
        userService.updateUserOperation(findUser.getId(), findUser.getOperation());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("status", String.format("User %s %sed!", findUser.getUsername(), findUser.getOperation().getName().toLowerCase())));
    }

}
