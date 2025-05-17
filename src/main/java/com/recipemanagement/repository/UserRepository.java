package com.recipemanagement.repository;

import com.recipemanagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
/**
 * Repository interface for user data access.
 * Extends MongoDB repository with user-specific queries.
 */
public interface UserRepository extends MongoRepository<User,String> {


    Optional<User>findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
