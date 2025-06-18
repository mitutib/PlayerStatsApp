package com.example.demo.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.example.demo.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class PlayerRepositoryIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testSaveAndFindPlayer() {

        Player player = new Player(null, "Test Player", 25, List.of());


        Player saved = playerRepository.save(player);
        Player found = playerRepository.findById(saved.getId()).orElse(null);


        assertNotNull(found);
        assertEquals("Test Player", found.getName());
        assertEquals(25, found.getAge());
    }


}
