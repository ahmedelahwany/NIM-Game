package NimGame.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameResult {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the player1.
     */
    @Column(nullable = false)
    private String player1;

    /**
     * The name of the player2.
     */
    @Column(nullable = false)
    private String player2;

    /**
     * The name of the winner.
     */
    @Column(nullable = false)
    private String winner;

    /**
     * Indicates whether one of the players has won the game.
     */
    private boolean solved;

    /**
     * The number of stones Taken  by  player1.
     */
    private int stepsPlayer1;

    /**
     * The number of stones Taken  by  player2.
     */
    private int stepsPlayer2;

    /**
     * The duration of the game.
     */
    @Column(nullable = false)
    private Duration duration;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }

}
