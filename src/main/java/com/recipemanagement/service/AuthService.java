package com.recipemanagement.service;

import com.recipemanagement.dto.LoginRequest;
import com.recipemanagement.dto.RegisterRequest;
import com.recipemanagement.model.Role;
import com.recipemanagement.model.User;
import com.recipemanagement.repository.UserRepository;
import com.recipemanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import lombok.extern.slf4j.Slf4j;


/**
 * Service class for authentication operations.
 * Handles user registration, login, and JWT token generation.
 */
@Service
//@Slf4j
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private  AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private JwtUtil jwtUtil;
    private IdGeneratorService idGeneratorService;

    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder encoder,
            JwtUtil jwtUtil,
            IdGeneratorService idGeneratorService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.idGeneratorService=idGeneratorService;
    }
    /**
     * Registers a new user with system
     * @param request Registration data
     * @throws RuntimeException if username/email exists
     */
    public void register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) { // Add this check
            throw new RuntimeException("Email already exists");
        }
        Role role = request.getRole() != null ? request.getRole() : Role.CLIENT;

        User user = new User(
                request.getUsername(),
                encoder.encode(request.getPassword()),
                request.getEmail(),
                request.getRole()
        );

        user.setId(idGeneratorService.generateUserId());
        userRepository.save(user);
    }
    /**
     * Authenticates user and generates JWT token
     * @param request Login credentials
     * @return JWT token string
     * @throws RuntimeException if authentication fails
     */
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        logger.debug("Logged in user: {}", user.getUsername());
        return jwtUtil.generateToken(user.getUsername(), user.getRoles());
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
    }

    public void checkIfAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Access denied: Admins only");
        }
    }


}