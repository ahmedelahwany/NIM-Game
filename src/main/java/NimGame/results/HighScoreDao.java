package NimGame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link HighScores} entity.
 */
public class HighScoreDao extends GenericJpaDao<HighScores> {

    public HighScoreDao() {
        super(HighScores.class);
    }

    /**
     * Returns the list of {@code n} best results with respect to
     * number of games won.
     *     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to
     *   number of games won
     */
    @Transactional
    public  List<HighScores> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM HighScores r ORDER BY r.NumberOfGames DESC",HighScores.class)
                .setMaxResults(n)
                .getResultList();

    }

    /**
     * A method Returns the instance with the specified name

     *      @param name the name of the player whose record in the highscores table'll be returned
     * @return Returns the instance with the specified name {@code entityManager.find(HighScores.class,name);}
     *
     */
    @Transactional
    public  HighScores find(String name) {
        try{
        return entityManager.find(HighScores.class,name);}
        catch (Exception e){
            return null;
        }
    }

    /**
     * A method to find the instance with the specified name and update it with a new number of games won

     *      @param name the maximum number of results to be returned
     *        @param newNumberOfGames  the updated number of games that the player won
     */
    @Transactional
    public  void update(String name,int newNumberOfGames) {

       HighScores em =entityManager.find(HighScores.class,name);
       em.setNumberOfGames(newNumberOfGames);
       entityManager.merge(em);
    }



}
