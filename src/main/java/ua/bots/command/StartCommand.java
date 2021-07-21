package ua.bots.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ua.bots.logic.GameLogic;

@Component
public class StartCommand extends ServiceCommand {
    private static final String ANSWER_TEXT = "Go ahead. Type your city";

    @Autowired
    private GameLogic gameLogic;

    public StartCommand() {
        super("start", "Start command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        gameLogic.processStartCommand(chat.getId());

        String userName = getUserName(user);
        sendAnswer(absSender, chat.getId(), userName, ANSWER_TEXT);
    }
}
