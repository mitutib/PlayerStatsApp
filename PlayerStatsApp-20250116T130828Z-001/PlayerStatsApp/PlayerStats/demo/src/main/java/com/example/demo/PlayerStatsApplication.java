package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan

public class PlayerStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayerStatsApplication.class, args);
	}

}
