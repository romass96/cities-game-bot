package ua.bots.model;

import lombok.Data;

import javax.persistence.*;

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
}
