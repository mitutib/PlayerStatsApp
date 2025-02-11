package com.example.demo.controller;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.dto.PlayerMapper;
import com.example.demo.model.Player;
import com.example.demo.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return players.stream().map(PlayerMapper::toDTO).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        if (player != null) {
            PlayerDTO playerDTO = PlayerMapper.toDTO(player);
            return ResponseEntity.ok(playerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> addPlayer(@RequestBody Player player) {
        try {
            Player savedPlayer = playerService.addPlayer(player);
            PlayerDTO playerDTO = PlayerMapper.toDTO(savedPlayer);
            return ResponseEntity.ok(playerDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        Player updated = playerService.updatePlayer(id, updatedPlayer);
        if (updated != null) {
            PlayerDTO playerDTO = PlayerMapper.toDTO(updated);
            return ResponseEntity.ok(playerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        boolean isDeleted = playerService.deletePlayer(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
