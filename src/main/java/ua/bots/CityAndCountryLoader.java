package ua.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.bots.model.City;
import ua.bots.model.Country;
import ua.bots.repository.CountryRepository;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CityAndCountryLoader implements CommandLineRunner
{
    private final CountryRepository countryRepository;

    public CityAndCountryLoader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @SneakyThrows
    public Set<Country> loadFromJson() {
        try(InputStream jsonInputStream = getClass().getClassLoader().getResourceAsStream("cities.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonInputStream);
            JsonNode dataNode = jsonNode.get("data");
            JsonGeoObject[] jsonObjects = objectMapper.treeToValue(dataNode, JsonGeoObject[].class);

            return Arrays.stream(jsonObjects).map(JsonGeoObject::convertToCountry).collect(Collectors.toSet());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Set<Country> countries = loadFromJson();
        countryRepository.saveAll(countries);
    }

    @Data
    private static class JsonGeoObject {
        private String country;
        private String[] cities;

        public Country convertToCountry() {
            Country countryObject = new Country();
            countryObject.setName(country.toLowerCase());

            Set<City> setOfCities = Arrays.stream(cities).map(cityName -> {
                City city = new City();
                city.setName(cityName.toLowerCase());
                city.setCountry(countryObject);
                return city;
            }).collect(Collectors.toSet());
            countryObject.setCities(setOfCities);

            return countryObject;
        }
    }
}
