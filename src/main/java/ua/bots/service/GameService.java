package ua.bots.service;

import ua.bots.model.City;
import ua.bots.model.Game;

import java.util.Optional;

public interface GameService {
    Game create(Game game);
    Game create(Long chatId);
    Optional<Game> findActiveGame(Long chatId);
    Game addCityToGame(City city, Game game);
    boolean gameContainsCity(Game game, City city);
    void stopGame(Game game);
    void setUserWinner(Game game);
    String getStatistics(Long chatId);
}
