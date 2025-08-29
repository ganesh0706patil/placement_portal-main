package com.example.placement_portal.service;

import com.example.placement_portal.entity.User;
import com.example.placement_portal.entity.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User updateUser(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAllUsers();
    List<User> findByRole(UserRole role);
    List<User> searchUsersByName(String name);
    boolean existsByEmail(String email);
    void deleteUser(Long id);
    User authenticateUser(String email, String password);
}
