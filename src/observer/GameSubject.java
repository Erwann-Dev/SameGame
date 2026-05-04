package observer;

/**
 * Subject interface for the SameGame observer pattern.
 *
 * Implemented by {@code GameModel}, which maintains a list of registered
 * {@link GameObserver}s and notifies them after every state change.
 * Keeping this interface separate from the model means views and controllers
 * depend only on the contract, not the concrete model class.
 */
public interface GameSubject {

    /**
     * Registers an observer to receive future state-change notifications.
     *
     * @param observer the observer to add; must not be null
     */
    void addObserver(GameObserver observer);

    /**
     * Removes a previously registered observer. Has no effect if the observer
     * was never registered.
     *
     * @param observer the observer to remove
     */
    void removeObserver(GameObserver observer);

    /**
     * Pushes the current state to all registered observers by calling
     * {@link GameObserver#update} on each one in registration order.
     */
    void notifyObservers();
}
