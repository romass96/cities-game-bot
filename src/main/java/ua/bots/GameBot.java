package ua.bots;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.bots.command.ServiceCommand;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class GameBot extends TelegramLongPollingCommandBot
{
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Autowired
    private GameLogic gameLogic;

    @Autowired
    private ServiceCommand[] serviceCommands;

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        registerAll(serviceCommands);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            gameLogic.processGameUpdate(message);
        }
    }

    public void sendAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch ( TelegramApiException e) {
            log.warn("Unable to send answer in the chat " + chatId, e);
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
