package com.example.demo.controller;

import com.example.demo.model.Player;
import com.example.demo.model.PlayerStats;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.PlayerStatsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")

class PlayerStatsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Player player;

    @BeforeEach
    void setUp() {
        playerStatsRepository.deleteAll();
        playerRepository.deleteAll();

        player = new Player();
        player.setName("Test Player");
        player.setAge(30);
        player = playerRepository.save(player);
    }

    @Test
    void testCreateAndRetrieveStats() throws Exception {

        PlayerStats stats = new PlayerStats();
        stats.setPlayer(player);
        stats.setScore(50);
        stats.setGamesPlayed(10);


        String json = objectMapper.writeValueAsString(stats);

        MvcResult postResult = mockMvc.perform(post("/stats").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.score").value(50)).andExpect(jsonPath("$.gamesPlayed").value(10)).andReturn();


        String responseBody = postResult.getResponse().getContentAsString();
        Long statsId = objectMapper.readTree(responseBody).get("id").asLong();


        mockMvc.perform(get("/stats")).andExpect(status().isOk()).andExpect(jsonPath("$[0].score").value(50));


    }
}
