package com.company.gamestore.controller;

import com.company.gamestore.exception.GameNotFoundException;
import com.company.gamestore.exception.InvalidGameException;
import com.company.gamestore.model.Game;
import com.company.gamestore.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(GameNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidGameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidGame(InvalidGameException ex) {
        return ex.getMessage();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Optional<Game> getGameById(@PathVariable int id) {
        return gameService.getGameById(id);
    }

    @GetMapping("/studio/{studio}")
    public List<Game> findByStudio(@PathVariable String studio) {
        return gameService.findByStudio(studio);
    }

    @GetMapping("/rating/{esrbRating}")
    public List<Game> findByEsrbRating(@PathVariable String esrbRating) {
        return gameService.findByEsrbRating(esrbRating);
    }

    @GetMapping("/title/{title}")
    public List<Game> findByTitle(@PathVariable String title) {
        return gameService.findByTitle(title);
    }

    @PutMapping
    public Game updateGame(@RequestBody Game game) {
        return gameService.updateGame(game);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable int id) {
        gameService.deleteGame(id);
    }
}