package ua.bots.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ua.bots.model.Game;
import ua.bots.repository.GameRepository;
import ua.bots.service.GameService;

public class StartCommand extends ServiceCommand {
    private static final String ANSWER_TEXT = "Let's start. If you need a help, please, type /help";

    private final GameService gameService;

    public StartCommand(GameService gameService) {
        super("start", "Start command");
        this.gameService = gameService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        gameService.create(chat.getId());

        String userName = getUserName(user);
        sendAnswer(absSender, chat.getId(), userName, ANSWER_TEXT);
    }


}
