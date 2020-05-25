package NimGame.state;


/**
 * Class representing the players in the game and their different properties.
 */
public class  Player {

    /**
     *  fields of the stone class.
     *  totalMoves field represents total number of taken stones made by a player during the game.
     *  name field represents player's name;
     */
    private int totalMoves ;



    public Player(String namep) {
        this.name=namep;
        this.totalMoves = 0;

    }

    private String name;

    /**
     * Returns the field  that represents total number of taken stones made by a player during the game.
     *
     * @return the  field that represents total number of taken stones made by a player during the game.
     */
    public int getTotalMoves() {
        return totalMoves;
    }
    /**
     * setting a new value for total moves if the player has taken another stone;
     * @param totalMoves represent the value for the totalMoves field;
     */
    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }


    /**
     * Returns the field  that represents the name of the player
     *
     * @return the  field  that represents the name of the player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * setting a new value for the player's name;
     * @param name represent new value of the player's name
     */

    public void setName(String name) {
        this.name = name;
    }




    public String toString() {
        return name;
    }

}
