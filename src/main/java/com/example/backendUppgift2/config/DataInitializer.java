package com.example.backendUppgift2.config;

import com.example.backendUppgift2.entity.Role;
import com.example.backendUppgift2.entity.User;
import com.example.backendUppgift2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            createUser("admin", "admin123", "System Admin", Role.ADMIN);
            createUser("agent", "agent123", "Support Agent", Role.AGENT);
            createUser("user", "user123", "Regular User", Role.USER);
        }
    }

    private void createUser(String username, String rawPass, String fullName, Role role) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPass));
        u.setFullName(fullName);
        u.setRole(role);
        userRepo.save(u);
    }
}
