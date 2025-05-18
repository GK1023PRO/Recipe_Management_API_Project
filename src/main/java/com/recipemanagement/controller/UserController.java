package com.recipemanagement.controller;

import com.recipemanagement.dto.UserDTO;
import com.recipemanagement.model.Role;
import com.recipemanagement.model.User;
import com.recipemanagement.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for user management.
 * Provides endpoints for retrieving user information (admin only).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers(
            @RequestParam(name = "role", required = false) Role roleFilter // 👈 Optional role filter
    ) {
        List<User> users;
        if (roleFilter != null) {
            // Filter users by role (in-memory)
            users = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRoles().contains(roleFilter))
                    .collect(Collectors.toList());
        } else {
            // Return all users if no filter
            users = userRepository.findAll();
        }

        return users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
}
