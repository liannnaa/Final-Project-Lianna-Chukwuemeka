package com.company.gamestore.controller;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Game;
import com.company.gamestore.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private final ObjectMapper mapper = new ObjectMapper();
    private Game game;

    @BeforeEach
    public void setup() {
        game = new Game();
        game.setGameId(1);
        game.setTitle("Test Title");
        game.setEsrbRating("Test Rating");
        game.setDescription("Test Description");
        game.setPrice(1.00);
        game.setStudio("Test Studio");
        game.setQuantity(1);
    }

    @Test
    public void addGameTest() throws Exception {
        when(gameService.addGame(any(Game.class))).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/games")
                        .content(mapper.writeValueAsString(game))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    public void getGameByIdTest() throws Exception {
        when(gameService.getGameById(1)).thenReturn(Optional.ofNullable(game));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    public void getAllGamesTest() throws Exception {
        when(gameService.getAllGames()).thenReturn(Collections.singletonList(game));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    public void updateGameTest() throws Exception {
        when(gameService.updateGame(any(Game.class))).thenReturn(game);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/games")
                        .content(mapper.writeValueAsString(game))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    public void deleteGameTest() throws Exception {
        doNothing().when(gameService).deleteGame(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/games/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getGamesByStudioTest() throws Exception {
        when(gameService.findByStudio("Test Studio")).thenReturn(Collections.singletonList(game));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/studio/{studio}", "Test Studio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    public void getGamesByEsrbRatingTest() throws Exception {
        when(gameService.findByEsrbRating("Test Rating")).thenReturn(Collections.singletonList(game));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/rating/{rating}", "Test Rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    public void getGamesByTitleTest() throws Exception {
        when(gameService.findByTitle("Test Title")).thenReturn(Collections.singletonList(game));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/title/{title}", "Test Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    public void getAllGamesNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No games found.")).when(gameService).getAllGames();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No games found."));
    }

    @Test
    public void findByStudioNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No games found with studio: Test Studio")).when(gameService).findByStudio("Test Studio");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/studio/{studio}", "Test Studio"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No games found with studio: Test Studio"));
    }

    @Test
    public void findByEsrbRatingNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No games found with ESRB rating: Test Rating")).when(gameService).findByEsrbRating("Test Rating");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/rating/{rating}", "Test Rating"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No games found with ESRB rating: Test Rating"));
    }

    @Test
    public void findByTitleNotFoundTest() throws Exception {
        doThrow(new NotFoundException("No games found with title: Test Title")).when(gameService).findByTitle("Test Title");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/games/title/{title}", "Test Title"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No games found with title: Test Title"));
    }

    @Test
    public void updateGameNotFoundTest() throws Exception {
        doThrow(new NotFoundException("Game not found with id: 1")).when(gameService).updateGame(any(Game.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/games")
                        .content(mapper.writeValueAsString(game))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Game not found with id: 1"));
    }

    @Test
    public void deleteGameNotFoundTest() throws Exception {
        doThrow(new NotFoundException("Game not found with id: 1")).when(gameService).deleteGame(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/games/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Game not found with id: 1"));
    }

}
