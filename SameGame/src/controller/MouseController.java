package controller;

import model.GameModel;
import view.SwingView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse-based input controller for the SameGame Swing interface
 * (MVC: Controller layer).
 *
 * Translates pixel-level mouse events on the board panel into grid
 * coordinates and forwards them to the model:
 *   Click - calls GameModel.selectTile(row, col) to attempt group removal.
 *   Move  - calls GameModel.hoverTile(row, col) to update the highlight preview.
 *
 * Pixel-to-grid conversion uses {@link SwingView#TILE_SIZE}:
 *   col = pixelX / TILE_SIZE
 *   row = pixelY / TILE_SIZE
 *
 * Listeners are registered on the component returned by
 * {@link SwingView#getBoardComponent()} to avoid a circular package
 * dependency between the view and controller packages.
 */
public class MouseController extends MouseAdapter implements GameController {

    /** The model that receives translated input commands. */
    private final GameModel model;

    /** The view that provides the board component and tile-size constant. */
    private final SwingView view;

    /**
     * Creates a mouse controller wired to the given model and view.
     * Call {@link #attach()} afterward to start receiving events.
     *
     * @param model the game model to forward input commands to
     * @param view  the Swing view whose board panel will be listened on
     */
    public MouseController(GameModel model, SwingView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles a mouse click on the board panel. Converts the click position
     * to a grid cell and calls {@link GameModel#selectTile}.
     *
     * @param e the mouse event from Swing, containing pixel coordinates
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / SwingView.TILE_SIZE;
        int row = e.getY() / SwingView.TILE_SIZE;
        model.selectTile(row, col);
    }

    /**
     * Handles mouse movement over the board panel. Converts the cursor
     * position to a grid cell and calls {@link GameModel#hoverTile} so the
     * model can update the highlighted group for preview purposes.
     *
     * @param e the mouse event from Swing, containing pixel coordinates
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int col = e.getX() / SwingView.TILE_SIZE;
        int row = e.getY() / SwingView.TILE_SIZE;
        model.hoverTile(row, col);
    }

    /**
     * Registers this controller as a mouse listener and motion listener on
     * the board panel. Must be called once before the view is displayed.
     */
    @Override
    public void attach() {
        JComponent board = view.getBoardComponent();
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
    }

    /**
     * Removes this controller's listeners from the board panel, stopping all
     * mouse input handling.
     */
    @Override
    public void detach() {
        JComponent board = view.getBoardComponent();
        board.removeMouseListener(this);
        board.removeMouseMotionListener(this);
    }
}
