package ua.bots.service;

import ua.bots.model.Game;

import java.util.Optional;

public interface GameService {
    Game create(Game game);
    Game create(Long chatId);
    Optional<Game> findActiveGame(Long chatId);
}
