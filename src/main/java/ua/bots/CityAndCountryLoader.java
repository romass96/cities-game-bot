package ua.bots;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import ua.bots.model.City;
import ua.bots.model.Country;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CityAndCountryLoader {

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

    public static void main(String[] args) {
        CityAndCountryLoader loader = new CityAndCountryLoader();
        loader.loadFromJson().forEach(System.out::println);
    }

    @Data
    private static class JsonGeoObject {
        private String country;
        private String[] cities;

        public Country convertToCountry() {
            Country countryObject = new Country();
            countryObject.setName(country);

            Set<City> setOfCities = Arrays.stream(cities).map(cityName -> {
                City city = new City();
                city.setName(cityName);
                city.setCountry(countryObject);
                return city;
            }).collect(Collectors.toSet());
            countryObject.setCities(setOfCities);

            return countryObject;
        }
    }
}
