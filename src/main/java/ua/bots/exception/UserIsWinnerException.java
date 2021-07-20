package ua.bots.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.bots.model.Game;

@Getter
@AllArgsConstructor
public class UserIsWinnerException extends RuntimeException {
    private final Game activeGame;
}
