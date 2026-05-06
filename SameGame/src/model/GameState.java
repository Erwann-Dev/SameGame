package model;

/**
 * Represents the three possible states of a SameGame session.
 *
 * PLAYING: the game is in progress and valid moves remain.
 * WIN:     the board has been completely cleared.
 * LOSE:    tiles remain but no valid group of at least 2 adjacent
 *          same-color tiles can be found.
 *
 * {@code GameModel} transitions between these states after each tile removal
 * and notifies all registered observers so views can update accordingly.
 */
public enum GameState {
    PLAYING,
    WIN,
    LOSE
}
