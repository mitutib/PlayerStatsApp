package com.example.demo.dto;

import lombok.Data;

@Data
public class PlayerStatsDTO {
    private Long id;
    private Long playerId;
    private int score;
    private int gamesPlayed;

    public PlayerStatsDTO(Long id, Long playerId, int score, int gamesPlayed) {
        this.id = id;
        this.playerId = playerId;
        this.score = score;
        this.gamesPlayed = gamesPlayed;
    }
}
