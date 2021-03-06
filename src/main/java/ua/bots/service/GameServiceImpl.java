package ua.bots.service;

import org.springframework.stereotype.Service;
import ua.bots.model.City;
import ua.bots.model.Game;
import ua.bots.model.GameCity;
import ua.bots.repository.GameRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game create(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game create(Long chatId) {
        Game game = new Game();
        game.setChatId(chatId);
        game.setActive(true);

        return create(game);
    }

    @Override
    public Optional<Game> findActiveGame(Long chatId) {
        return gameRepository.findByActiveAndChatId(true, chatId);
    }

    @Override
    public Game addCityToGame(City city, Game game) {
        game.addCity(city);
        return gameRepository.save(game);
    }

    @Override
    public boolean gameContainsCity(Game game, City city) {
        Set<City> gameCities = game.getCities().stream().map(GameCity::getCity).collect(Collectors.toSet());
        return gameCities.contains(city);
    }

    @Override
    public void stopGame(Game game) {
        game.setActive(false);
        game.setUserWinning(false);
        gameRepository.save(game);
    }

    @Override
    public void setUserWinner(Game game) {
        game.setUserWinning(true);
        gameRepository.save(game);
    }

    @Override
    public String getStatistics(Long chatId) {
        List<Game> allGames = gameRepository.findByChatId(chatId);
        long userWinnings = allGames.stream().filter(Game::getUserWinning).count();
        return String.format("Total: %d%nUser winnings: %d%nBot winnings:%d",
                allGames.size(), userWinnings, (allGames.size() - userWinnings));
    }

}
