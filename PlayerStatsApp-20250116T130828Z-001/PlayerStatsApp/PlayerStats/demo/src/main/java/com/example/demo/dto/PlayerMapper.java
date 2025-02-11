package com.example.demo.dto;

import com.example.demo.model.Player;
import com.example.demo.model.PlayerStats;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerMapper {


    public static PlayerDTO toDTO(Player player) {
        List<PlayerStatsDTO> statsDTO = player.getStats().stream().map(PlayerMapper::toDTO).collect(Collectors.toList());
        return new PlayerDTO(player.getId(), player.getName(), player.getAge(), statsDTO);
    }


    public static PlayerStatsDTO toDTO(PlayerStats playerStats) {
        return new PlayerStatsDTO(playerStats.getId(), playerStats.getPlayerId(), playerStats.getScore(), playerStats.getGamesPlayed());
    }

}
