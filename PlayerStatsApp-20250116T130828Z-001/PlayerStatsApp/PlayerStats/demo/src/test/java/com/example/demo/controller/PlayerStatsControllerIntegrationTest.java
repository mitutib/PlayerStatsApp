package com.example.demo.controller;

import com.example.demo.model.Player;
import com.example.demo.model.PlayerStats;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.PlayerStatsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class PlayerStatsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // Jackson

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
//    private PlayerStatsRepository playerStatsRepository;

    private Player savedPlayer;

    @BeforeEach
    void setup() {
        playerStatsRepository.deleteAll();
        playerRepository.deleteAll();

        Player player = new Player();
        player.setName("Test Player");
        player.setAge(30);
        player.setStats(Collections.emptyList());

        savedPlayer = playerRepository.save(player);
    }

    @Test
    void testCreateAndGetAndDeleteStats() throws Exception {

        PlayerStats stats = new PlayerStats();
        stats.setPlayer(savedPlayer);
        stats.setScore(100);
        stats.setGamesPlayed(10);


        String json = objectMapper.writeValueAsString(stats);



        mockMvc.perform(post("/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(100))
                .andExpect(jsonPath("$.gamesPlayed").value(10))
                .andReturn().getResponse().getContentAsString();


        Long statsId = objectMapper.readTree(postResponse).get("id").asLong();


        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(statsId))
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[0].gamesPlayed").value(10));


        mockMvc.perform(delete("/stats/{id}", statsId))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
