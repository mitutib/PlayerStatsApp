package com.example.demo.controller;


import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class PlayerControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
    }


    @Test
    @DisplayName("Player has been successfully added")
    void testAddPlayer() throws Exception {

        // JSON.de adaugat obiect
        String playerJson = """
                    {
                      "name": "James",
                      "age": 40,
                      "stats": []
                    }
                """;

        mockMvc.perform(post("/players").contentType(MediaType.APPLICATION_JSON).content(playerJson)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("James")).andExpect(jsonPath("$.age").value(40));

    }



    @Test

    void testAddAndGetPlayers() throws Exception {

        String playerJson = """
                {
                  "name": "James",
                  "age": 40,
                  "stats": []
                }
                """;

        mockMvc.perform(post("/players").contentType(MediaType.APPLICATION_JSON).content(playerJson)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("James")).andExpect(jsonPath("$.age").value(40));

        mockMvc.perform(get("/players")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("James")).andExpect(jsonPath("$[0].age").value(40));
    }

    @Test
    @DisplayName("Player has been updated")
    void testUpdatePlayer() throws Exception {

        Player initialPlayer = new Player(null, "Old Name", 25, List.of());
        Player saved = playerRepository.save(initialPlayer);


        String updatedJson = """
                    {
                      "name": "Davis",
                      "age": 30,
                      "stats": []
                    }
                """;

        mockMvc.perform(put("/players/{id}", saved.getId()).contentType(MediaType.APPLICATION_JSON).content(updatedJson)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Davis")).andExpect(jsonPath("$.age").value(30));
    }


    @Test
    @DisplayName("Player has been successfully deleted")
    void testDeletePlayer() throws Exception {

        String playerJson = """
                {
                  "name": "ToDelete",
                  "age": 28,
                  "stats": []
                }
                """;

        MvcResult result = mockMvc.perform(post("/players").contentType(MediaType.APPLICATION_JSON).content(playerJson)).andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long playerId = ((Integer) JsonPath.read(responseBody, "$.id")).longValue();

        mockMvc.perform(delete("/players/{id}", playerId)).andExpect(status().isNoContent());

        mockMvc.perform(get("/players/{id}", playerId)).andExpect(status().isNotFound());



    }


}
