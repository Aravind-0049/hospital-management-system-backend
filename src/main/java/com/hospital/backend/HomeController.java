package com.hospital.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/api/status")
    public String status() {
        return "Hospital Management System Backend is running!";
    }
}
