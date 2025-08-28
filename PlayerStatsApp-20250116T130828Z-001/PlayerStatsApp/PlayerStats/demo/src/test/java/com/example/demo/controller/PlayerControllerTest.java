package com.example.demo.controller;

import com.example.demo.dto.PlayerDTO;
import com.example.demo.model.Player;
import com.example.demo.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PlayerControllerTest {
    private PlayerService playerService;
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        playerController = new PlayerController(playerService);
    }

    @Test
    void testGetAllPlayers() {
        Player player1 = new Player(1L, "John", 25, Collections.emptyList());
        Player player2 = new Player(2L, "Mike", 28, Collections.emptyList());
        when(playerService.getAllPlayers()).thenReturn(List.of(player1, player2));

        List<PlayerDTO> result = playerController.getAllPlayers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        verify(playerService, times(1)).getAllPlayers();
    }

    @Test
    void testAddPlayer_Success() {
        Player player = new Player(null, "Alex", 22, Collections.emptyList());
        Player saved = new Player(3L, "Alex", 22, Collections.emptyList());
        when(playerService.addPlayer(player)).thenReturn(saved);

        ResponseEntity<?> response = playerController.addPlayer(player);

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(PlayerDTO.class, response.getBody());
        assertEquals("Alex", ((PlayerDTO) response.getBody()).getName());
    }


    @Test
    void testAddPlayer_Failure() {
        Player player = new Player(null, "Invalid", -5, Collections.emptyList());
        when(playerService.addPlayer(player)).thenThrow(new RuntimeException("Invalid age"));

        ResponseEntity<?> response = playerController.addPlayer(player);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid age", response.getBody());
    }


}
