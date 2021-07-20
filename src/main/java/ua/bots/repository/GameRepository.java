package ua.bots.repository;

import org.springframework.data.repository.CrudRepository;
import ua.bots.model.Game;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Long> {
    Optional<Game> findByActiveAndChatId(boolean active, Long chatId);
    List<Game> findByChatId(Long chatId);
}
