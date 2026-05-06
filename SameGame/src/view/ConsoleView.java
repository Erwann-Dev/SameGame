package view;

import model.GameModel;
import model.GameState;

/**
 * Text-based view that prints the game state to standard output (MVC: View layer).
 *
 * Each call to {@link #update} prints the current score, a character-art
 * representation of the board, and an end-game message when appropriate.
 *
 * Color index to character mapping:
 *   0 = '.' (empty)   1 = R   2 = G   3 = B   4 = Y   5 = P
 *
 * This view runs alongside {@code SwingView} for debugging without any
 * additional configuration.
 */
public class ConsoleView implements GameView {

    /**
     * Character symbols for each color index. Index 0 is the empty cell;
     * indices 1-5 map to the first five game colors.
     */
    private static final String[] COLOR_CHARS = {".", "R", "G", "B", "Y", "P"};

    /**
     * Prints the full board state to stdout. Called automatically by the model
     * after every state change.
     *
     * @param model the current game model; provides the board, score, and state
     */
    @Override
    public void update(GameModel model) {
        System.out.println("Score: " + model.getScore() + "  State: " + model.getState());
        for (int r = 0; r < model.getBoard().getRows(); r++) {
            for (int c = 0; c < model.getBoard().getCols(); c++) {
                int color = model.getBoard().getTile(r, c).getColor();
                System.out.print(COLOR_CHARS[Math.min(color, COLOR_CHARS.length - 1)] + " ");
            }
            System.out.println();
        }
        if (model.getState() == GameState.WIN)  System.out.println("==> YOU WIN!");
        if (model.getState() == GameState.LOSE) System.out.println("==> GAME OVER");
        System.out.println();
    }

    /** No-op: this view requires no setup to become visible. */
    @Override public void display() {}

    /** No-op: this view holds no resources to release. */
    @Override public void close() {}
}
