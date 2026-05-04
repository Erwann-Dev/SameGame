package observer;

import model.GameModel;

/**
 * Observer interface for the SameGame observer pattern.
 *
 * Any class that wants to be notified of game state changes must implement
 * this interface and register itself with a {@link GameSubject} (typically
 * {@code GameModel}). When the model changes it calls {@link #update} on
 * every registered observer so they can refresh their representation of the
 * game state.
 *
 * Typical implementors are views: {@code SwingView} and {@code ConsoleView}.
 */
public interface GameObserver {

    /**
     * Called by the subject whenever the game state has changed.
     *
     * @param model the current game model, containing the updated board,
     *              score, game state, and highlighted group
     */
    void update(GameModel model);
}
