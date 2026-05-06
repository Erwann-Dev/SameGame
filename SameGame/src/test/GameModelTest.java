package test;

import model.GameModel;
import model.GameState;
import org.junit.Test;

import static org.junit.Assert.*;
/*To test*/
public class GameModelTest {

    @Test
    public void testInitialScoreIsZero() {
        GameModel model = new GameModel(3);
        assertEquals(0, model.getScore());
    }

    @Test
    public void testGameStartsPlaying() {
        GameModel model = new GameModel(3);
        assertEquals(GameState.PLAYING, model.getState());
    }

    @Test
    public void testRestartResetsScore() {
        GameModel model = new GameModel(3);
        model.restart(3);
        assertEquals(0, model.getScore());
    }

    @Test
    public void testHighScoreExists() {
        GameModel model = new GameModel(3);
        assertTrue(model.getHighScore() >= 0);
    }
}