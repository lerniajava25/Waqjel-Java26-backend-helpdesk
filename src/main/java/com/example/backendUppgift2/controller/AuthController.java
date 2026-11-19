package com.example.backendUppgift2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "redirect:/tickets";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
