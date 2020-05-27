package NimGame.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Class representing the result of a game played by a specific player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class HighScores {



    /**
     * The name of the player.
     */
    @Column(nullable = false)
    @Id
    private String player;

    /**
     * The number of games won by the player.
     */
    @Column(nullable = false)
    private int NumberOfGames;


}
