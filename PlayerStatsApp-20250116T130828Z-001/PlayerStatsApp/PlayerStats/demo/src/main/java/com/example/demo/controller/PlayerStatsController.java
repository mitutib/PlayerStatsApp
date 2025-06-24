package com.example.demo.controller;

import com.example.demo.dto.PlayerMapper;
import com.example.demo.dto.PlayerStatsDTO;
import com.example.demo.model.Player;
import com.example.demo.model.PlayerStats;
import com.example.demo.service.PlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/stats")
public class PlayerStatsController {

    @Autowired
    private PlayerStatsService playerStatsService;


    @GetMapping
    public List<PlayerStatsDTO> getAllStats() {
        List<PlayerStats> stats = playerStatsService.getAllStats();
        return stats.stream().map(PlayerMapper::toDTO).collect(Collectors.toList());
    }


//    @PostMapping
//    public ResponseEntity<PlayerStatsDTO> saveStats(@RequestBody PlayerStats stats) {
//        PlayerStats savedStats = playerStatsService.saveStats(stats);
//        PlayerStatsDTO statsDTO = PlayerMapper.toDTO(savedStats);
//        return ResponseEntity.ok(statsDTO);
////    }


    @PostMapping
    public ResponseEntity<PlayerStatsDTO> saveStats(@RequestBody PlayerStatsDTO statsDto) {
        PlayerStats stats = new PlayerStats();
        stats.setScore(statsDto.getScore());
        stats.setGamesPlayed(statsDto.getGamesPlayed());

        if (statsDto.getPlayerId() != null) {
            Player player = new Player();
            player.setId(statsDto.getPlayerId());
            stats.setPlayer(player);
        } else {
            throw new RuntimeException("Player ID is required in DTO");
        }

        PlayerStats savedStats = playerStatsService.saveStats(stats);

        PlayerStatsDTO responseDto = new PlayerStatsDTO();
        responseDto.setId(savedStats.getId());
        responseDto.setScore(savedStats.getScore());
        responseDto.setGamesPlayed(savedStats.getGamesPlayed());
        responseDto.setPlayerId(savedStats.getPlayer().getId());

        return ResponseEntity.ok(responseDto);
    }





    @PutMapping("/{id}")
    public ResponseEntity<PlayerStatsDTO> updateStats(@PathVariable Long id, @RequestBody PlayerStats updatedStats) {
        PlayerStats updated = playerStatsService.updateStats(id, updatedStats);
        if (updated != null) {
            PlayerStatsDTO statsDTO = PlayerMapper.toDTO(updated);
            return ResponseEntity.ok(statsDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStats(@PathVariable Long id) {
        boolean isDeleted = playerStatsService.deleteStats(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
