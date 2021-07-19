package ua.bots.repository;

import org.springframework.data.repository.CrudRepository;
import ua.bots.model.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {

}
