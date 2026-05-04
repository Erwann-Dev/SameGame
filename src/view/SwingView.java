package view;

import model.GameModel;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Graphical view built with Java Swing (MVC: View layer).
 *
 * Displays the game board as a grid of rounded rectangles. Tiles belonging
 * to the group currently under the mouse are highlighted with a brighter fill
 * and a white border, giving the player visual feedback before committing a
 * click. A status bar at the bottom shows the score, game state, and a
 * Restart button.
 *
 * Window layout:
 *   Top (CENTER)  - BoardPanel: custom JPanel that draws the tile grid.
 *   Bottom (SOUTH) - Status bar: score label, state label, restart button.
 *
 * Controller attachment:
 *   Rather than accepting a controller directly (which would create a circular
 *   import), this class exposes {@link #getBoardComponent()} so the controller
 *   can attach its mouse listeners to the board panel from outside this package.
 */
public class SwingView extends JFrame implements GameView {

    /**
     * Pixel width and height of each tile square, including a 1-pixel gap on
     * every side. Also used by {@code MouseController} to convert pixel
     * coordinates back to grid positions.
     */
    public static final int TILE_SIZE = 40;

    /**
     * Colour palette indexed by tile color value. Index 0 is the empty-cell
     * colour; indices 1-5 correspond to the five possible game colours.
     */
    private static final Color[] TILE_COLORS = {
        new Color(40, 40, 40),      // 0 = empty (dark grey background)
        new Color(220, 50,  50),    // 1 = red
        new Color(50,  200, 80),    // 2 = green
        new Color(50,  100, 220),   // 3 = blue
        new Color(220, 200, 50),    // 4 = yellow
        new Color(160, 50,  220),   // 5 = purple
    };

    /** Reference to the model, updated on every {@link #update} call. */
    private GameModel model;

    /** Custom panel that renders the tile grid. */
    private final BoardPanel boardPanel;

    /** Displays the current accumulated score. */
    private final JLabel scoreLabel;

    /** Displays "Playing", "YOU WIN!", or "Game Over". */
    private final JLabel stateLabel;

    /**
     * Constructs the window and lays out all Swing components.
     * Does not make the window visible; call {@link #display()} after
     * attaching the controller.
     *
     * @param model the game model whose state will be rendered; must not be null
     */
    public SwingView(GameModel model) {
        this.model = model;
        setTitle("SameGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        scoreLabel = new JLabel("Score: 0");
        stateLabel = new JLabel("Playing");
        JButton restartBtn = new JButton("Restart");
        restartBtn.addActionListener(e -> model.restart(3));
        statusBar.add(scoreLabel);
        statusBar.add(stateLabel);
        statusBar.add(restartBtn);
        add(statusBar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Returns the board panel so an external controller can register mouse
     * listeners on it without creating a circular dependency between the
     * view and controller packages.
     *
     * @return the {@link JComponent} that renders the tile grid
     */
    public JComponent getBoardComponent() { return boardPanel; }

    /**
     * Refreshes the view to reflect the latest model state. Typically called
     * by the model via the observer mechanism.
     *
     * @param model the updated game model
     */
    @Override
    public void update(GameModel model) {
        this.model = model;
        scoreLabel.setText("Score: " + model.getScore());
        stateLabel.setText(switch (model.getState()) {
            case WIN  -> "YOU WIN!";
            case LOSE -> "Game Over";
            default   -> "Playing";
        });
        boardPanel.repaint();
    }

    /**
     * Makes the window visible. Should be called on the Swing Event Dispatch
     * Thread after all observers and controllers have been attached.
     */
    @Override public void display() { setVisible(true); }

    /**
     * Disposes of the window and releases its native resources.
     */
    @Override public void close() { dispose(); }

    // -------------------------------------------------------------------------
    // Inner class
    // -------------------------------------------------------------------------

    /**
     * Custom panel responsible for painting the tile grid.
     *
     * On each {@link #paintComponent} call it reads the current model state
     * directly (board tiles and highlighted group) and redraws every cell.
     * Highlighted tiles are rendered with a brighter fill and a white border.
     */
    private class BoardPanel extends JPanel {

        /**
         * Sets the preferred size to exactly fit the board at the current
         * {@link SwingView#TILE_SIZE}.
         */
        BoardPanel() {
            setPreferredSize(new Dimension(
                model.getBoard().getCols() * TILE_SIZE,
                model.getBoard().getRows() * TILE_SIZE
            ));
            setBackground(Color.BLACK);
        }

        /**
         * Paints every tile on the board. For each cell: looks up the color,
         * brightens it if the tile is in the highlighted group, fills a rounded
         * rectangle, and optionally draws a white outline for highlighted tiles.
         *
         * @param g the {@link Graphics} context provided by Swing's paint system
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (model == null) return;

            // Build a fast 2-D lookup for highlighted positions
            List<int[]> highlighted = model.getHighlightedGroup();
            boolean[][] isHighlighted = new boolean[model.getBoard().getRows()][model.getBoard().getCols()];
            for (int[] pos : highlighted)
                isHighlighted[pos[0]][pos[1]] = true;

            for (int r = 0; r < model.getBoard().getRows(); r++) {
                for (int c = 0; c < model.getBoard().getCols(); c++) {
                    int colorIdx = model.getBoard().getTile(r, c).getColor();
                    Color base = colorIdx < TILE_COLORS.length ? TILE_COLORS[colorIdx] : Color.WHITE;
                    Color fill = isHighlighted[r][c] ? base.brighter() : base;

                    int x    = c * TILE_SIZE + 1;
                    int y    = r * TILE_SIZE + 1;
                    int size = TILE_SIZE - 2;

                    g.setColor(fill);
                    g.fillRoundRect(x, y, size, size, 8, 8);

                    if (isHighlighted[r][c] && colorIdx != 0) {
                        g.setColor(Color.WHITE);
                        ((Graphics2D) g).setStroke(new BasicStroke(2));
                        g.drawRoundRect(x, y, size, size, 8, 8);
                    }
                }
            }
        }
    }
}
