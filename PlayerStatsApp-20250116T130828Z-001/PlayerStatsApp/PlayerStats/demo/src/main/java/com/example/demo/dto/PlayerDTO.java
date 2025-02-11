package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerDTO {
    private Long id;
    private String name;
    private int age;
    private List<PlayerStatsDTO> stats;

    public PlayerDTO(Long id, String name, int age, List<PlayerStatsDTO> stats) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.stats = stats;
    }
}
