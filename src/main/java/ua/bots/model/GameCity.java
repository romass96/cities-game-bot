package ua.bots.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Table(name = "game_cities")
@Entity
public class GameCity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Game game;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;
}
