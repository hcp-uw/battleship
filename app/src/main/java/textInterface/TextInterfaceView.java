package textInterface;

import battleship.BoardView;
import battleship.Point;

import java.util.Scanner;
import java.util.Set;

/**
 * The TextInterfaceView class represents a view that allows a user to interact
 * with the command line.
 */
public class TextInterfaceView implements View {

    // This class does not represent an ADT

    private InputHandler inputHandler;
    private Scanner input;

    private boolean active;

    public TextInterfaceView() {
        active = true;
        input = new Scanner(System.in);
    }

    @Override
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void begin() {
        if (inputHandler == null) {
            throw new IllegalStateException("No InputHandler has been provided to respond "
                                            + "to user input");
        }
        while (active) {
            inputHandler.handleInput(nextInput());
        }
    }

    private String nextInput() {
        String nextInput = input.nextLine();
        System.out.println();
        return nextInput;
    }

    @Override
    public void exit() {
        active = false;
        input.close();
    }

    @Override
    public void startMenu() {
        System.out.println("Welcome to Battleship");
        System.out.print("Would you like to play against the computer or "
                         + "or against another player? ");
    }

    @Override
    public void drawBoard(BoardView board) {
        int boardSize = 10;
        StringBuilder builder = new StringBuilder();
        builder.append(" ");
        for (char letter = 'A'; letter < 'A' + boardSize; letter++) {
            builder.append(" ");
            builder.append(letter);
        }
        builder.append("\n");
        Set<Point> hits = board.getHits();
        Set<Point> misses = board.getMisses();
        for (int i = 0; i < boardSize; i++) {
            builder.append(i);
            for (int j = 0; j < boardSize; j++) {
                builder.append(" ");
                Point p = new Point(i, j);
                if (hits.contains(p)) {
                    builder.append("X");
                } else if (misses.contains(p)) {
                    builder.append("O");
                } else {
                    builder.append("-");
                }
            }
            builder.append("\n");
        }
        System.out.print(builder);
    }

    @Override
    public void setupPrompt() {
        // TODO
    }

    @Override
    public void attackPrompt() {
        System.out.print("Position to attack: ");
    }

    @Override
    public void placeShipPrompt() {
        System.out.print("Ship placement on board: ");
    }

    @Override
    public void shipOrientationPrompt() {
        System.out.print("Ship orientation: ");
    }

    @Override
    public void shipLengthPrompt() {
        System.out.print("Ship length: ");
    }

    @Override
    public void placeShipOfLength(int length) {
        System.out.print("Where to place ship of length " + length + ": ");
    }

    @Override
    public void showErrorUnknownInput() {
        System.out.println("Unknown option");
    }

    @Override
    public void showErrorInvalidPosition() {
        System.out.println("Invalid board position");
    }
}
