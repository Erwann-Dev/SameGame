package view;

import observer.GameObserver;

/**
 * Marker interface for all SameGame views (MVC: View layer).
 *
 * Extends {@link GameObserver} so every view is automatically a valid
 * observer that can be registered with the model. Concrete views
 * ({@code SwingView}, {@code ConsoleView}) implement both
 * {@link #update} (from {@code GameObserver}) and the lifecycle methods below.
 *
 * Adding a new view only requires implementing this interface and registering
 * the instance with {@code GameModel.addObserver()}.
 */
public interface GameView extends GameObserver {

    /**
     * Makes the view visible to the user. Called once after all wiring
     * (model and controller) is complete.
     */
    void display();

    /**
     * Releases any resources held by the view and hides it. Useful when
     * swapping views at runtime or shutting down.
     */
    void close();
}
