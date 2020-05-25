package NimGame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    void testToString() {
        Player test = new Player("Lena");
        test.setName("Mat");
        assertEquals("Mat",test.toString());
    }
}