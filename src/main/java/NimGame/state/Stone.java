package NimGame.state;

/**
 * Class representing the stones in the board game and whether it exits or not.
 */
public class  Stone {

    /**
     *  fields of the stone class.
     *  row field represents the row number of the stone in the board
     *  col field represents the column of the stone in the board
     *  exits represents whether the stone is still on the board (1) or it's been taken by one of the players(0)

     */
    private int row;
    private int col;


    private int exists;


    public Stone(int row, int col) {
        this.row = row;
        this.col = col;
        this.exists=1;
    }

    /**
     * Returns the row field that represents this instance on the board.
     *
     * @return the row field that represents this instance on the board.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column field that represents this instance on the board.
     *
     * @return the column field that represents this instance on the board.
     */

    public int getCol() {
        return col;
    }




    /**
     * Returns the exits field that represents whether this instance exits on the board or not.
     *
     * @return Returns the exits field that represents whether this instance exits on the board or not.
     */
    public int isExists() {
        return exists;
    }
    /**
     * setting a new value for the exits field of the instance
     * @param exists represent the new boolean value of the instance's exits filed.
     */
    public void setExists(int exists) {
        this.exists = exists;
    }


    public String toString() {
        return exists==0?"0":"1";
    }

}
