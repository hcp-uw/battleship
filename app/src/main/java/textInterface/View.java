package textInterface;

import battleship.BoardView;

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

    /**
     * Displays a prompt to th
     */
    void setupPrompt();

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
     * Displays an error to the user about an unknown command.
     */
    void showErrorUnknownCommand();

    /**
     * Displays an error to the user about an unknown board position.
     */
    void showErrorInvalidPosition();
}
