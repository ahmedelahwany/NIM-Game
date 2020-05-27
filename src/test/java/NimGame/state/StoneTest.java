package NimGame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoneTest {

    @Test
    void getRow() {
        Stone stone =new Stone(3,5);
        assertEquals(3,stone.getRow());
    }

    @Test
    void getCol() {
        Stone stone =new Stone(3,5);
        assertEquals(5,stone.getCol());
    }


    @Test
    void isExists() {
        Stone stone =new Stone(3,5);
        assertEquals(1,stone.isExists());
    }

    @Test
    void setExists() {
        Stone stone =new Stone(3,5);
        stone.setExists(0);
        assertEquals(0,stone.isExists());
    }

    @Test
    void testToString() {
        Stone stone =new Stone(3,5);
        assertEquals("1",stone.toString());
        stone.setExists(0);
        assertEquals("0",stone.toString());

    }
}