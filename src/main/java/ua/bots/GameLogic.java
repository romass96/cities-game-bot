package ua.bots;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
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
public class GameLogic {
    @Autowired
    private GameService gameService;

    @Autowired
    private CityService cityService;

    @Autowired
    private GameBot gameBot;

    public void processGameUpdate(Message message) {
        Long chatId = message.getChatId();
        gameService.findActiveGame(chatId).ifPresentOrElse(game -> {
            String cityName = message.getText();
            cityService.findCityByName(cityName).ifPresentOrElse(city -> {
                boolean isCityPresentInGame = gameService.gameContainsCity(game, city);
                if (isCityPresentInGame) {
                    //TODO Fix this logic
                    sendAnswer(chatId, "This city is already present in the game");
                } else {
                    findCityForAnswer(city, game).ifPresentOrElse(answerCity -> {
                        gameService.addCityToGame(answerCity, game);
                        printCityAnswer(answerCity, chatId);
                    }, () -> sendAnswer(chatId, "You are winner"));
                }
            }, () -> sendAnswer(chatId, "Such city doesn't exist"));
        }, () -> sendAnswer(chatId, "You don't have active games. Type /start to start game"));
    }

    private void sendAnswer(Long chatId, String answer) {
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
        if (citiesSize > 0) {
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
