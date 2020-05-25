package NimGame.state;

import javafx.print.Collation;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NimGameStateTest {




    @Test
    void testOneArgConstructor_ValidArg() {
        int[][] a = new int[][] {
                {1, 1, 1,1},
                {1, 1, 1,1},
                {1, 1, 1,1},
                {1, 1, 1,1}
        };
        NimGameState state = new NimGameState(a,new Player("Mat"),new Player("Lena"));
        Integer[] checkArray=Arrays.stream(state.getBoard()).flatMap(x->Arrays.stream(x)).map(x->x.isExists()).toArray(Integer[]::new);
        assertArrayEquals(new Integer [] {
                1, 1, 1,1,
                1, 1, 1,1,
                1, 1, 1,1,
                1, 1, 1,1
        },checkArray);

    }

    @Test
    void testIsSolved() {
        assertFalse(new NimGameState(new Player("Mat"),new Player("Lena")).isSolved());
        assertTrue(new NimGameState(new int[][] {
                {0, 0, 0,0},
                {0, 0, 0,0},
                {0, 0, 0,0},
                {0, 0, 0,0}},new Player("Mat"),new Player("Lena")).isSolved());
    }

    @Test
    void testCanBeTaken() {
        NimGameState state = new NimGameState(new int[][] {
                {1, 1, 1,1},
                {1, 0, 1,1},
                {1, 1, 0,1},
                {1, 1, 1,1}
        },new Player("Mat"),new Player("Lena"));
        assertThrows(IllegalArgumentException.class, () -> state.canBeTaken(5, 5));
        assertThrows(IllegalArgumentException.class, () -> state.canBeTaken(-2, 5));
        assertTrue(state.canBeTaken(0, 0));
        assertTrue(state.canBeTaken(0, 1));
        assertTrue(state.canBeTaken(0, 2));
        assertTrue(state.canBeTaken(0, 3));
        assertThrows(IllegalArgumentException.class, () -> state.canBeTaken(1, 1));
        assertTrue(state.canBeTaken(1, 0));
        assertTrue(state.canBeTaken(1, 2));
        assertTrue(state.canBeTaken(1, 3));
        assertTrue(state.canBeTaken(2, 1));
        assertThrows(IllegalArgumentException.class, () -> state.canBeTaken(2, 2));
        assertTrue(state.canBeTaken(2, 3));
        assertTrue(state.canBeTaken(2, 0));
        assertTrue(state.canBeTaken(3, 1));
        assertTrue(state.canBeTaken(3, 3));
        assertTrue(state.canBeTaken(3, 2));
        assertTrue(state.canBeTaken(3, 0));
    }

    @Test
    void testSwitchPlayers(){
        NimGameState state = new NimGameState(new Player("Mat"),new Player("Lena"));

        // simulating switching players before taking any stones (i.e tmpmovement array' size is zero)
        state.switchPlayers();
        assertEquals(state.getErrorMessage(),"You have to take at least one stone ,then switch");
        assertEquals(state.getPlayer1(),state.getActivePlayer());

        // simulating switching players because maximum moves per turn has been reached

        List<int[]> list = new LinkedList<>(List.of(new int[]{0, 2}, new int[]{0, 3}, new int[]{0, 2}, new int[]{0, 1}));
        state.setTempMovement(list);
        state.switchPlayers();
        assertEquals(state.getErrorMessage(),"MAXIMUM TAKES PER TURN REACHED \n" +
                                             "TURNS HAVEN BEEN SWITCHED AUTOMATICALLY\n" +
                                             state.getActivePlayer().toString()+" has the turn now");
        assertEquals(state.getPlayer2(),state.getActivePlayer());

        // simulating switching players before reaching the maximum moves per turn (i.e tmpmovement array' size is less than 4 and greater than 0)
        List<int[]> list2 = new LinkedList<>(List.of(new int[]{0, 2}, new int[]{0, 3}));
        state.setTempMovement(list2);
        state.switchPlayers();
        assertEquals(state.getErrorMessage(),"Player "+ state.getActivePlayer().toString()+" now Has the turn");
        assertEquals(state.getPlayer1(),state.getActivePlayer());
    }

    @Test
    void testTakeStone() {
        NimGameState state = new NimGameState(new Player("Mat"),new Player("Lena"));
        state.setTempMovement(List.of(new int[] {0,2},new int[] {0,3},new int[] {0,2},new int[] {0,1}));

        // initializing the tempmovement to make it of size 4 to check whether the function 'll throw exception or not
        assertThrows(UnsupportedOperationException.class, () -> state.takeStone(2, 3));
        List<int[]> tempMovement = new ArrayList<int[]>();
        state.setTempMovement( tempMovement);// clearing the tempmovement array to restart checking
        state.takeStone(1,1);//simulating taking the first stone from the board

        // checking if the stone's position 's been added to the tempmovement array which 'll assist in evaluating game logic
        assertArrayEquals(state.getTempMovement().get(0),new int[]{1,1});
        assertEquals(state.getActivePlayer().getTotalMoves(),1);// check whether total moves by the player have been increased or not
        assertEquals(state.getBoard()[1][1].isExists(),0);// check whether the stone has been removed or not
        state.takeStone(0,0);//simulating taking  wrong stone from the board according the rules
        assertEquals(state.getTempMovement().size(),1);

        // TAKING FROM THE SAME ROW
        state.takeStone(1,0);//simulating taking right second stone from the board (same row) according the rules
        assertArrayEquals(state.getTempMovement().get(1),new int[]{1,0});

        state.takeStone(2,2);//simulating taking  wrong stone from the board (not second row) according the rules
        assertEquals(state.getTempMovement().size(),2);
        state.takeStone(1,2);//simulating taking right third stone from the board (same row) according the rules
        assertArrayEquals(state.getTempMovement().get(2),new int[]{1,2});

        // RESETTING THE tempmvement array TO CHECK IF THE PLAYER CN TAKE FORM THE COLUMN AS WELL ACCORDING TO THE RULES
        List<int[]> tempMovement2 = new ArrayList<int[]>();
        tempMovement2=state.getTempMovement();
        tempMovement2.remove(2);
        tempMovement2.remove(1);
        state.setTempMovement(tempMovement2);

        // TAKING FROM THE SAME column
        state.takeStone(0,1);//simulating taking right second stone from the board (same column) according the rules
        assertArrayEquals(state.getTempMovement().get(1),new int[]{0,1});
        state.takeStone(2,2);//simulating taking  wrong stone from the board (not second column) according the rules
        assertEquals(state.getTempMovement().size(),2);
        state.takeStone(3,1);//simulating taking  third stone from the board (same column not adjacent) according the rules
        assertEquals(state.getTempMovement().size(),2);
        state.takeStone(2,1);//simulating taking right second stone from the board (same column and adjacent) according the rules
        assertArrayEquals(state.getTempMovement().get(2),new int[]{2,1});
        System.out.println(state);

    }

    @Test
    void testToString() {
        NimGameState state = new NimGameState(new Player("Mat"),new Player("Lena"));
        assertEquals("1 1 1 1 \n"
                     + "1 1 1 1 \n"
                     + "1 1 1 1 \n"
                     + "1 1 1 1 \n"
                , state.toString());
    }

}
