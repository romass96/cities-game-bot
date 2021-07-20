package ua.bots.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ua.bots.GameLogic;

@Component
public class StopCommand extends ServiceCommand {
    private static final String ANSWER_TEXT = "You have stopped this game and it is lost. "
        + "In order to start new game type /start";

    @Autowired
    private GameLogic gameLogic;

    public StopCommand() {
        super("stop", "Stop command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        gameLogic.processStopCommand(chat.getId());

        String userName = getUserName(user);
        sendAnswer(absSender, chat.getId(), userName, ANSWER_TEXT);
    }
}
