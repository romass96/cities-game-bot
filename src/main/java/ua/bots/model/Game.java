package ua.bots.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "games")
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    private Boolean userWinning;

    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<GameCity> cities = new HashSet<>();

    public void addCity(City city) {
        GameCity gameCity = new GameCity();
        gameCity.setCity(city);
        gameCity.setGame(this);
        cities.add(gameCity);
    }

    public Optional<City> getLastCity() {
        return cities.stream()
            .max(Comparator.comparing(GameCity::getId))
            .map(GameCity::getCity);
    }
}
