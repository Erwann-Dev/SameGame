package controller;

/**
 * Contract for all input controllers in the SameGame application
 * (MVC: Controller layer).
 *
 * A controller translates raw input events (mouse clicks, key presses,
 * network messages, etc.) into calls on the model. Implementing this
 * interface and its two lifecycle methods keeps the rest of the system
 * decoupled from the input mechanism: swapping or adding a controller
 * requires no changes to the model or views.
 *
 * How to add a new controller:
 * 1. Create a class that implements {@code GameController}.
 * 2. Accept the {@code GameModel} in the constructor (and a view if
 *    coordinate translation is needed).
 * 3. In {@link #attach()}, register the event listeners.
 * 4. In {@link #detach()}, unregister them.
 * 5. Wire the controller in {@code Main} by calling {@link #attach()}.
 */
public interface GameController {

    /**
     * Registers this controller's event listeners so it begins receiving
     * input events. Must be called once after all components are wired.
     */
    void attach();

    /**
     * Unregisters this controller's event listeners, stopping all input
     * handling. Can be used to swap controllers at runtime.
     */
    void detach();
}
