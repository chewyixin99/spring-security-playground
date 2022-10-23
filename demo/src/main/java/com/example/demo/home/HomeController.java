package com.example.demo.home;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/api/hello")
    public String home(Principal principal) {
        return "Hello " + principal.getName();
    }

}

