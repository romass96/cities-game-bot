package ua.bots.service;

import org.springframework.stereotype.Service;
import ua.bots.model.Game;
import ua.bots.repository.GameRepository;

import java.util.Optional;

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
}
