package textInterface;

import battleship.BoardView;
import battleship.Ship;

import java.util.List;

/**
 * The View interface represents a
 */
public interface View {

    /**
     * Begins receiving input from the user.
     *
     * @throws IllegalStateException if no InputHandler has been provided to
     * this view
     */
    void begin();

    /**
     * Stops receiving input from the user.
     */
    void exit();

    /**
     * Sets the input handler that will be used by this view in the future for
     * handling user input.
     *
     * @param inputHandler the {@link InputHandler} for this view
     */
    void setInputHandler(InputHandler inputHandler);

    /**
     * Displays the start menu to the user.
     */
    void startMenu();

    void drawBoard(BoardView board);

    void drawBoard(BoardView board, List<Ship> ships);

    /**
     * Displays a prompt to the user to indicate they should set something up
     * @param name String name of thing to prompt user to set up
     */
    void setupPrompt(String name);

    /**
     * Displays a prompt to the user about which position to attack on the board.
     */
    void attackPrompt();

    /**
     * Displays a prompt to the user about which position to place a ship on the board.
     */
    void placeShipPrompt();

    /**
     * Displays a prompt to the user about which orientation to use for a ship being placed.
     */
    void shipOrientationPrompt();

    /**
     * Displays a prompt to the user about what length ship to place?
     */
    void shipLengthPrompt();

    /**
     * Displays a prompt to the user about placing a ship of the given length
     */
    void placeShipOfLength(int length);

    /**
     * Displays an error to the user about an unknown command.
     */
    void showErrorUnknownInput();

    /**
     * Displays an error to the user about an unknown board position.
     */
    void showErrorInvalidPosition();
}
