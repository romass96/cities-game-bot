package ua.bots.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ua.bots.GameLogic;

@Component
public class StatisticsCommand extends ServiceCommand {

    @Autowired
    private GameLogic gameLogic;

    public StatisticsCommand() {
        super("statistics", "Statistics command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        gameLogic.processStatisticsCommand(chat.getId());
    }
}
