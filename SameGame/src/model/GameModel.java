package model;

import observer.GameObserver;
import observer.GameSubject;
import model.HighScoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Central model for the SameGame application (MVC: Model layer).
 *
 * {@code GameModel} owns all game state: the {@link Board}, the current
 * score, the {@link GameState}, and the set of tiles currently highlighted
 * by the mouse. It implements {@link GameSubject} so any number of views
 * can register as observers and be refreshed automatically after every
 * state change.
 *
 * Scoring: removing a group of n tiles adds (n - 2) * (n - 2) points.
 * Removing exactly 2 tiles scores 0; larger groups score quadratically more.
 *
 * End conditions:
 *   WIN  - board is empty after a removal.
 *   LOSE - tiles remain but no group of at least 2 exists.
 *
 * Typical usage:
 *   GameModel model = new GameModel(3);  // 3 colors
 *   model.addObserver(myView);
 *   model.selectTile(row, col);          // called by a controller
 */
public class GameModel implements GameSubject {

    /** Default grid height used when creating a new board. */
    public static final int ROWS = 10;

    /** Default grid width used when creating a new board. */
    public static final int COLS = 15;

    private Board board;
    private int score;
    private GameState state;
    private List<GameObserver> observers;
    private int highScore;

    /** Tiles the mouse is currently over that form a valid group. Empty otherwise. */
    private List<int[]> highlightedGroup;

    /**
     * Creates a new game model and initialises a fresh board.
     *
     * @param numColors number of distinct tile colors on the board (at least 1);
     *                  fewer colors make the game easier because larger groups form
     */
    public GameModel(int numColors) {
        this.observers = new ArrayList<>();
        this.highScore = HighScoreManager.loadHighScore();
        init(numColors);
    }

    /**
     * Resets all mutable game state without touching the observer list.
     *
     * @param numColors number of distinct colors for the new board
     */
    private void init(int numColors) {
        this.board = new Board(ROWS, COLS, numColors);
        this.score = 0;
        this.state = GameState.PLAYING;
        this.highlightedGroup = new ArrayList<>();
    }

    /**
     * Attempts to remove the group containing the tile at (row, col).
     *
     * A group must have at least 2 tiles to be removable. If the group is
     * valid, the score is updated, tiles are removed from the board, and the
     * win/lose condition is evaluated. Observers are notified in all cases so
     * the view can clear any stale highlight.
     *
     * This method is a no-op when the game is not in PLAYING state, or when
     * the specified cell is empty or out of bounds.
     *
     * @param row row index of the clicked tile (0 = top)
     * @param col column index of the clicked tile (0 = left)
     */
    public void selectTile(int row, int col) {
        if (state != GameState.PLAYING) return;
        if (outOfBounds(row, col) || board.getTile(row, col).isEmpty()) return;

        List<int[]> group = board.findGroup(row, col);
        if (group.size() < 2) {
            highlightedGroup = new ArrayList<>();
            notifyObservers();
            return;
        }

        int n = group.size();
        score += (n - 2) * (n - 2);
        
        if (score > highScore) {
            highScore = score;
            HighScoreManager.saveHighScore(highScore);
        }
        
        board.removeTiles(group);
        highlightedGroup = new ArrayList<>();

        if (board.isEmpty()) {
            state = GameState.WIN;
        } else if (!board.hasValidMoves()) {
            state = GameState.LOSE;
        }

        notifyObservers();
    }

    /**
     * Updates the highlighted group based on the tile the mouse is over.
     *
     * If the cursor is over a valid group (at least 2 tiles), that group is
     * stored and observers are notified so views can draw a preview highlight.
     * If the group has fewer than 2 tiles or the position is out of bounds,
     * the highlight is cleared.
     *
     * This method is a no-op when the game is not in PLAYING state.
     *
     * @param row row index under the cursor (0 = top)
     * @param col column index under the cursor (0 = left)
     */
    public void hoverTile(int row, int col) {
        if (state != GameState.PLAYING) return;
        if (outOfBounds(row, col)) {
            highlightedGroup = new ArrayList<>();
            notifyObservers();
            return;
        }
        List<int[]> group = board.findGroup(row, col);
        highlightedGroup = group.size() >= 2 ? group : new ArrayList<>();
        notifyObservers();
    }

    /**
     * Resets the game to a fresh state with a new random board and notifies
     * all observers so views update immediately.
     *
     * @param numColors number of distinct colors for the new board
     */
    public void restart(int numColors) {
        init(numColors);
        notifyObservers();
    }

    /**
     * Returns true if the given position falls outside the board bounds.
     *
     * @param row row to check
     * @param col column to check
     * @return true when out of bounds
     */
    private boolean outOfBounds(int row, int col) {
        return row < 0 || row >= ROWS || col < 0 || col >= COLS;
    }

    /**
     * Returns the current board.
     *
     * @return the {@link Board} owned by this model
     */
    public Board getBoard() { return board; }

    /**
     * Returns the accumulated score for this game session.
     *
     * @return current score (0 or greater)
     */
    public int getScore() { return score; }
    
    public int getHighScore() { return highScore; }

    /**
     * Returns the current game state.
     *
     * @return one of PLAYING, WIN, or LOSE
     */
    public GameState getState() { return state; }

    /**
     * Returns the group of tiles currently highlighted by the mouse hover,
     * or an empty list when no valid group is under the cursor.
     *
     * @return list of int[]{row, col} pairs; never null
     */
    public List<int[]> getHighlightedGroup() { return highlightedGroup; }

    /** {@inheritDoc} */
    @Override
    public void addObserver(GameObserver observer) { observers.add(observer); }

    /** {@inheritDoc} */
    @Override
    public void removeObserver(GameObserver observer) { observers.remove(observer); }

    /** {@inheritDoc} */
    @Override
    public void notifyObservers() {
        for (GameObserver o : observers) o.update(this);
    }
}
