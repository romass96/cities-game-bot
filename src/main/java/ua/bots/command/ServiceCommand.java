package ua.bots.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class ServiceCommand extends BotCommand
{
    public ServiceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    public void sendAnswer(AbsSender absSender, Long chatId, String userName, String text) {
        SendMessage message = new SendMessage();

        // enable markdown support to manage visibility of emojis and text
        message.enableMarkdown(false);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            absSender.execute(message);
        } catch ( TelegramApiException e) {
            log.warn("Unable to send message to {} in the chat {}", userName, chatId);
        }
    }

    protected String getUserName(User user) {
        return user.getUserName() != null ? user.getUserName() :
            String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
