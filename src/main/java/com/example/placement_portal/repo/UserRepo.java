package com.example.placement_portal.repo;

import com.example.placement_portal.entity.User;
import com.example.placement_portal.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Find by email (for login)
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find users by role
    List<User> findByRole(UserRole role);

    // Find users by name containing (search)
    List<User> findByNameContainingIgnoreCase(String name);

}
