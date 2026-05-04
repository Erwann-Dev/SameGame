import controller.MouseController;
import model.GameModel;
import view.ConsoleView;
import view.SwingView;

import javax.swing.SwingUtilities;

/**
 * Application entry point — wires all MVC components together and starts the game.
 *
 * Startup sequence:
 * 1. Create GameModel — generates a random board.
 * 2. Register ConsoleView as an observer (runs on any thread).
 * 3. On the Swing EDT: create SwingView, register it, attach MouseController,
 *    show the window, and trigger an initial render via notifyObservers().
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int numColors = 3;
        GameModel model = new GameModel(numColors);

        // ConsoleView is thread-safe (stdout) — register before the EDT starts
        ConsoleView consoleView = new ConsoleView();
        model.addObserver(consoleView);

        // All Swing work must happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SwingView swingView = new SwingView(model);
            model.addObserver(swingView);

            MouseController controller = new MouseController(model, swingView);
            controller.attach();

            swingView.display();
            model.notifyObservers(); // initial render
        });
    }
}
