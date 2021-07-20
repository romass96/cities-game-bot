package ua.bots.service;

import ua.bots.model.City;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityService {
    Optional<City> findCityByName(String cityName);
    List<City> findCitiesStartingWith(char firstLetter);
}
