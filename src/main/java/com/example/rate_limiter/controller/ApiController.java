package com.example.rate_limiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ApiController {

    @GetMapping("/hello")
    public String getHello(){
        return "Hello from controller";
    }

}
