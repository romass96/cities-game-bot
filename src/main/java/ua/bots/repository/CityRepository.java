package ua.bots.repository;

import org.springframework.data.repository.CrudRepository;
import ua.bots.model.City;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityRepository extends CrudRepository<City, Long> {
    Optional<City> findFirstByName(String cityName);
    List<City> findAllByNameStartingWith(char prefix);
}
