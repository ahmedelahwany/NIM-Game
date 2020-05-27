package NimGame.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class NimGameState {


    /**
     *  fields of the NimGameState class.
     *  player1 field represents player who is supposed to have the first turn.
     *  player2 field represents player who is supposed to have the second turn
     *  activePlayer field represents the player who is currently having the turn
     *  winner field represents the winner fo the game
     *  tmpMovement field represents the array which stores the previous stone's coordinates that has been taken by a player it his/her turn.
     *  isRow field represents whether the second taken stone in a single turn is in a row or a column
     *  gameRulesViolated represents whether a player has violated one of the predefined game rules while taking a stone.
     *  ErrorMessage field stores the messages what will be displayed to the players in the GUI.
     */

    private  Player player1 =new Player("");
    private  Player player2 =new Player("");
    private  Player activePlayer =new Player("");
    private Player winner = new Player("");

    private List<int[]> tempMovement = new ArrayList<>();

    private boolean isRow ;
    private boolean gameRulesViolated=false;
    private String ErrorMessage="";


    /**
     * The array representing the initial configuration of the board.
     */
    public static final int[][] INITIAL = {
            {1, 1, 1,1},
            {1, 1, 1,1},
            {1, 1, 1,1},
            {1, 1, 1,1}
    };



    /**
     * The array storing the current configuration of the board in terms of stones.
     */
    @Setter(AccessLevel.NONE)
    private Stone[][] board;




    /**
     * Creates a {@code NimGameState} object representing the (original)
     * initial state of the puzzle.
     */
    public NimGameState(Player Player1 ,Player Player2) {
        this(INITIAL,Player1,Player2);
    }

    /**
     * Creates a {@code NimGameState} object that is initialized it with
     * the specified array.
     *
     * @param a an array of size 3&#xd7;3 representing the initial configuration
     *          of the board
     */
    public NimGameState(int[][] a,Player Player1 ,Player Player2) {

        initboard(a);
        this.player1=Player1;
        this.player2=Player2;
       activePlayer=player1;
    }



    private void initboard(int[][] a) {
        this.board = new Stone[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4;j++) {
                this.board[i][j]=new Stone(i,j);
                this.board[i][j].setExists(a[i][j]);
            }
        }
    }

    /**
     * Checks whether the puzzle is solved and specify the winner depending on the last active player.
     *
     * @return {@code true} if the puzzle is solved (all the stones have been taken), {@code false} otherwise
     */
    public boolean isSolved() {
        for (Stone[] row : board) {
            for (Stone stone : row) {
                if (stone.isExists()!=0) {
                    return false;
                }
            }
        }
       winner = activePlayer.equals(player2)?player2:player1;
        return true;
    }

    /**
     * Returns whether the stone at the specified position can be taken by a player in the GUI.
     *
     * @param row the row of the stone to be taken
     * @param col the column of the stone to be taken
     * @return {@code true} if the stone at the specified position can be taken
     * @throws IllegalArgumentException if the stone at the specified position
     * can not be taken because the click is outside the board or the stone is already taken.
     */
    public boolean canBeTaken(int row, int col) {
        if(0 <= row && row <= 3 && 0 <= col && col <= 3 &&
           board[row][col].isExists()==1) {return true;}
        else{
            this.ErrorMessage="Please, choose a Square with a stone\n" +
                              "inside the game board";

            throw new IllegalArgumentException();
        }
    }
    /**
     * Switching players depending on the number of movements made by player per turn and the active player.
     * @return if one of the players tried to switch turns without taking any stones.
     */
    public void switchPlayers(){
        if(tempMovement.size()==0 ){
            this.ErrorMessage="You have to take at least one stone ,then switch";
            return;
        }
        activePlayer= activePlayer==player1?player2:player1;
        if(tempMovement.size()==4){
            this.ErrorMessage= "MAXIMUM TAKES PER TURN REACHED \n" +
                                                       "TURNS HAVEN BEEN SWITCHED AUTOMATICALLY\n" +
                                                       activePlayer.toString()+" has the turn now";
        }
        if(tempMovement.size()<4)this.ErrorMessage="Player "+ activePlayer.toString()+" now Has the turn";
        tempMovement.clear();
        gameRulesViolated=false;
    }


    /**
     * perform the operation of removing stone according to the game rules
     *
     * @param row the row of the stone to be taken
     * @param col the column of the stone to be taken
     * @throws UnsupportedOperationException if the stone at the specified position
     *     can not be taken according the the game rules.
     */
    public void takeStone(int row, int col) {
        if (tempMovement.size() == 4) {
           switchPlayers();

            log.info(this.ErrorMessage);
            throw new UnsupportedOperationException();
        } else {
            if (canBeTaken(row, col)) {
                int[] position = new int[2];
               // To check if the Second taken stone is in the same row or column of the previous taken stone,in addition if it's adjacent
                if (tempMovement.size() == 1) {
                    if (((Math.abs(tempMovement.get(0)[0] - row) > 1) || (Math.abs(tempMovement.get(0)[1] - col) > 1))||
                        ((Math.abs(tempMovement.get(0)[0] - row) == 1) && (Math.abs(tempMovement.get(0)[1] - col) == 1))) {
                        this.ErrorMessage = "This Stone at (" + row +","+col +") can't be taken \n" +
                                            "according to game rules,choose adjacent one \n" +
                                            "in the same row or column or click switch button";
                        log.info(this.ErrorMessage);
                        throw new UnsupportedOperationException();

                    }
                    isRow = row == tempMovement.get(0)[0];
                } else {
                    for (int[] array : tempMovement) {
                   // to check if the third or fourth taken stone is in the same row and adjacent
                        if (isRow) {
                            if (Math.abs(array[1] - col) == 1 && row==array[0]) {
                                gameRulesViolated=false;
                                this.ErrorMessage="";
                                break;
                            }
                            this.ErrorMessage = "This Stone at (" + row +","+col +") can't be taken \n" +
                                                " according to game rules,choose adjacent one \n" +
                                                " in the same row or click switch button";

                        } else {    // to check if the third or fourth taken stone is in the same column and adjacent

                            if (Math.abs(array[0] - row) == 1 && col == array[1]) {
                                gameRulesViolated=false;
                                this.ErrorMessage="";
                                break;
                            }
                            this.ErrorMessage = "This Stone at (" + row +","+col +") can't be taken \n" +
                                                " according to game rules,choose adjacent one\n" +
                                                " in the same column or click switch button";
                        }
                        gameRulesViolated=true;

                    }
                }
                if(gameRulesViolated){   log.info(this.ErrorMessage);
                    throw new UnsupportedOperationException();
                }
                position[0] = row;
                position[1] = col;
                tempMovement.add(position);
                board[row][col].setExists(0);
                activePlayer.setTotalMoves(activePlayer.getTotalMoves() + 1);
                log.info("stone at ({},{}) is removed from board", row, col);
            }
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Stone[] row : board) {
            for (Stone stone : row) {
                sb.append(stone).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }


}
