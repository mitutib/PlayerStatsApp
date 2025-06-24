package com.example.demo.service;

import com.example.demo.dto.PlayerStatsDTO;
import com.example.demo.model.Player;
import com.example.demo.model.PlayerStats;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerStatsService {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public List<PlayerStats> getAllStats() {
        return playerStatsRepository.findAll();
    }


//    public PlayerStats saveStats(PlayerStats stats) {
//        if (stats.getPlayer() == null || stats.getPlayer().getId() == null) {
//            throw new RuntimeException("Player ID is required");
//        }
//
//
//        Player player = playerRepository.findById(stats.getPlayer().getId())
//                .orElseThrow(() -> new RuntimeException("Player not found"));
//
//
//        stats.setPlayer(player);
//
//        return playerStatsRepository.save(stats);
//    }


    public PlayerStats saveStats(PlayerStats stats) {
        if (stats.getPlayer() == null || stats.getPlayer().getId() == null) {
            throw new RuntimeException("Player ID is required");
        }

        Player player = playerRepository.findById(stats.getPlayer().getId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        stats.setPlayer(player);
        return playerStatsRepository.save(stats);
    }



    public PlayerStats updateStats(Long id, PlayerStats updatedStats) {
        return playerStatsRepository.findById(id).map(stats -> {
            stats.setScore(updatedStats.getScore());
            stats.setGamesPlayed(updatedStats.getGamesPlayed());
            return playerStatsRepository.save(stats);
        }).orElseThrow(() -> new RuntimeException("Stats not found with id " + id));
    }

    public boolean deleteStats(Long id) {
        playerStatsRepository.deleteById(id);
        return false;
    }
}
