package com.example.demo.service;

import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

//    @Autowired
//    PlayerRepository playerRepository;


    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("PlayerRepository initialized: " + (playerRepository != null));
    }


    public Player addPlayer(Player player) {
        Optional<Player> existingPlayer = playerRepository.findByName(player.getName());
        if (existingPlayer.isPresent()) {
            throw new RuntimeException("Player with name " + player.getName() + " already exists.");
        }
        return playerRepository.save(player);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id).map(player -> {
            player.setName(updatedPlayer.getName());
            player.setAge(updatedPlayer.getAge());
            return playerRepository.save(player);
        }).orElseThrow(() -> new RuntimeException("Player not found with id " + id));
    }


    public boolean deletePlayer(Long id) {
        playerRepository.deleteById(id);
        return false;
    }


}
