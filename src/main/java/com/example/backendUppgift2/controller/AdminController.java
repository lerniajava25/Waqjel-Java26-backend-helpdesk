package com.example.backendUppgift2.controller;

import com.example.backendUppgift2.entity.Role;
import com.example.backendUppgift2.entity.User;
import com.example.backendUppgift2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role) {
        User user = userRepo.findById(id).orElseThrow();
        user.setRole(role);
        userRepo.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setActive(!user.isActive());
        userRepo.save(user);
        return "redirect:/admin/users";
    }
}
