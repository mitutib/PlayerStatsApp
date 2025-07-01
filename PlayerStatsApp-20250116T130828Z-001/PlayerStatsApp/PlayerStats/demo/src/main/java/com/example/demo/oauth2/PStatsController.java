package com.example.demo.oauth2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PStatsController {

    @GetMapping("/")
    public String hello() {

        return "Welcome to PlayerStats";


    }

}
