package com.example.backendUppgift2.repository;

import com.example.backendUppgift2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backendUppgift2.entity.Role;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRoleInAndActiveTrue(List<Role> roles);
}
