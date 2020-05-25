package NimGame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    public GameResultDao() {
        super(GameResult.class);
    }




    /**
     * Returns the list of the results with respect to the time
     * spent for one of the players to win the game.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the time
     * spent for solving the puzzle
     */
    @Transactional
    public List<GameResult> find(int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r ORDER BY r.created DESC", GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }

}
