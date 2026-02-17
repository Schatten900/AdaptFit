package com.AdaptFit.SistemaFitness.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/health")
    public String health() {
        System.out.println(">>> TestController.health() called");
        return "Backend is running!";
    }
}
