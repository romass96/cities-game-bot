package ua.bots;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bots.exception.GameLogicException;
import ua.bots.model.City;
import ua.bots.model.Game;
import ua.bots.model.GameCity;
import ua.bots.service.CityService;
import ua.bots.service.GameService;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GameLogic {
    @Autowired
    private GameService gameService;

    @Autowired
    private CityService cityService;

    @Autowired
    private GameBot gameBot;

    public void processGameUpdate(Message message) {
        Long chatId = message.getChatId();

        try {
            Game activeGame = processActiveGame(chatId);
            String userInputCityName = message.getText().toLowerCase();
            checkIfUserCityStartsWithCorrectLetter(activeGame, userInputCityName);

            City userCity = processUserInputCity(userInputCityName);
            checkIfCityIsPresentInGame(activeGame, userCity);

            gameService.addCityToGame(userCity, activeGame);

            City cityForAnswer = processCityForAnswer(activeGame, userCity);
            log.info("{} found for answer", cityForAnswer.getName());
            gameService.addCityToGame(cityForAnswer, activeGame);
            printCityAnswer(cityForAnswer, chatId);
        } catch ( GameLogicException e ) {
            sendAnswer(chatId, e.getMessage());
        }
    }

    private Game processActiveGame(Long chatId) {
        return gameService.findActiveGame(chatId)
            .orElseThrow(() -> new GameLogicException("You don't have active games. Type /start to start game"));
    }

    private void checkIfUserCityStartsWithCorrectLetter(Game activeGame, String userInputCity) {
        log.info("User input: {}", userInputCity);
        //TODO Fix this logic
        activeGame.getLastCity().ifPresent(lastCity -> {
            String desiredLetter = String.valueOf(getLastChar(lastCity.getName()));
            if ( !userInputCity.startsWith(desiredLetter) ) {
                throw new GameLogicException("Wrong city. It should start with " + desiredLetter + " letter");
            }
        });
    }

    private void checkIfCityIsPresentInGame(Game activeGame, City userCity) {
        boolean isCityPresentInGame = gameService.gameContainsCity(activeGame, userCity);
        if ( isCityPresentInGame ) {
            throw new GameLogicException("This city is already present in the game");
        }
    }

    private City processCityForAnswer(Game activeGame, City userCity) {
        return findCityForAnswer(userCity, activeGame)
            .orElseThrow(() -> new GameLogicException("You are winner. Type /start to start new game"));
    }

    private City processUserInputCity(String userInputCity) {
        return cityService.findCityByName(userInputCity)
            .orElseThrow(() -> new GameLogicException("Such city doesn't exist"));
    }

    private void sendAnswer(Long chatId, String answer) {
        log.info("Sending answer {} to chat {}", answer, chatId);
        gameBot.sendAnswer(chatId, answer);
    }

    private Optional<City> findCityForAnswer(City userCity, Game game) {
        char lastLetter = getLastChar(userCity.getName());
        List<City> citiesStartingWith = cityService.findCitiesStartingWith(lastLetter);
        Set<City> gameCities = game.getCities().stream().map(GameCity::getCity).collect(Collectors.toSet());
        citiesStartingWith.removeAll(gameCities);

        return getRandomCity(citiesStartingWith);
    }

    private Optional<City> getRandomCity(List<City> cities) {
        int citiesSize = cities.size();
        if ( citiesSize > 0 ) {
            int index = new Random().nextInt(citiesSize);
            return Optional.of(cities.get(index));
        } else {
            return Optional.empty();
        }
    }

    private void printCityAnswer(City city, Long chatId) {
        String capitalizedCity = StringUtils.capitalize(city.getName());
        String capitalizedCountry = StringUtils.capitalize(city.getCountry().getName());
        String text = capitalizedCity + ", " + capitalizedCountry;
        sendAnswer(chatId, text);
    }

    private char getLastChar(String word) {
        return word.charAt(word.length() - 1);
    }

}
