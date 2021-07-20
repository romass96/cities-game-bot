package ua.bots.service;

import org.springframework.stereotype.Service;
import ua.bots.model.City;
import ua.bots.repository.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository)
    {
        this.cityRepository = cityRepository;
    }

    @Override
    public Optional<City> findCityByName(String cityName) {
        return cityRepository.findFirstByName(cityName.toLowerCase());
    }

    @Override
    public List<City> findCitiesStartingWith(char firstLetter) {
        return cityRepository.findAllByNameStartingWith(firstLetter);
    }

}
