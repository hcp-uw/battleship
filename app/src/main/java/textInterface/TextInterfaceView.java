package textInterface;

import battleship.BoardView;
import battleship.Point;
import battleship.Ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The TextInterfaceView class represents a view that allows a user to interact
 * with the command line.
 */
public class TextInterfaceView implements View {

    // This class does not represent an ADT

    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String BLUE = "\u001b[36m";
    private static final String RESET = "\u001b[0m";

    private InputHandler inputHandler;
    private BufferedReader input;

    private boolean active;

    public TextInterfaceView() {
        active = true;
        input = new BufferedReader(new InputStreamReader(System.in));
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
            inputHandler.handleInput(nextInput().trim());
        }
    }

    private String nextInput() {
        String inputValue = null;
        while (inputValue == null) {
            try {
                if (input.ready()) {
                    inputValue = input.readLine();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return inputValue;
    }

    @Override
    public void exit() {
        active = false;
        try {
            input.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void startMenu() {
        System.out.println("Welcome to Battleship");
    }

    @Override
    public void drawBoard(BoardView board) {
        drawBoard(board, (Set<Point>) null);
    }

    @Override
    public void drawBoard(BoardView board, List<Ship> ships) {
        Set<Point> shipPoints = getAllShipPoints(ships);
        drawBoard(board, shipPoints);
    }

    public void drawBoard(BoardView board, Set<Point> shipPoints) {
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
                Point p = new Point(j, i);
                if (hits.contains(p)) {
                    builder.append(RED);
                    builder.append("X");
                    builder.append(RESET);
                } else if (misses.contains(p)) {
                    builder.append("O");
                } else if (shipPoints != null && shipPoints.contains(p)) {
                    builder.append(GREEN);
                    builder.append("W");
                    builder.append(RESET);
                } else {
                    builder.append("-");
                }
            }
            builder.append("\n");
        }
        System.out.print(builder);
    }

    private Set<Point> getAllShipPoints(List<Ship> ships) {
        Set<Point> points = new HashSet<>();
        for (Ship ship : ships) {
            Point start = ship.startPoint();
            Point end = ship.endPoint();
            points.add(start);
            points.add(end);
            if (start.getX() == end.getX()) {
                // Vertical orientation
                int deltaY = end.getY() - start.getY();
                for (int i = 1; i < deltaY; i++) {
                    points.add(new Point(start.getX(), start.getY() + i));
                }
            } else {
                // Horizontal orientation
                int deltaX = end.getX() - start.getX();
                for (int i = 1; i < deltaX; i++) {
                    points.add(new Point(start.getX() + i, start.getY()));
                }
            }
        }
        return points;
    }

    public void welcome(){
        System.out.println("Welcome to Battleship!");
    }

    @Override
    public void setupPrompt() {
        // TODO
    }

    /**
     * Prompts the user for the number of human players to have
     */
    public void numPlayersPrompt(int min, int max) {
        numThingPrompt("players", min, max);
    }

    /**
     * Prompts the user for the number of CPU players to have
     */
    public void numCPUsPrompt(int min, int max) {
        numThingPrompt("CPUs", min, max);
    }

    public void numThingPrompt(String thing, int min, int max) {
        System.out.print("Number of " + thing + " (" + min + "-" + max + "): ");
    }

    public void boardLengthPrompt(int min, int max) {
        System.out.print("Board length (" + min + "-" + max + "): ");
    }

    public void playAgainPrompt() {
        System.out.print("Play again? ");
    }

    /**
     * Lists options for game settings
     *
     * @param options the options
     */
    public void showOptionsEnumerated(List<String> options) {
        System.out.println("Choose your option:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + 1 + " - " + options.get(i));
        }
    }

    public void showOptions(List<String> options) {
        System.out.println("Choose your option:");
        for (String option : options) {
            System.out.println(option);
        }
    }

    public void playerPrompt(String player){
        clearConsole();
        System.out.println(BLUE + player + "'s turn:" + RESET);
    }

    @Override
    public void attackPrompt() {
        System.out.print(BLUE + "Position to attack: " + RESET);
    }

    @Override
    public void placeShipPrompt() {
        System.out.println(BLUE + "Ship placement on board: " + RESET);
    }

    @Override
    public void shipOrientationPrompt() {
        System.out.print(BLUE + "Ship orientation: " + RESET);
    }

    @Override
    public void shipLengthPrompt() {
        System.out.print(BLUE + "Ship length: " + RESET);
    }

    @Override
    public void placeShipOfLength(int length) {
        System.out.print(BLUE + "Where to place ship of length " + length + ": " + RESET);
    }

    public void showWinner(String player){
        System.out.println(BLUE + player + " wins!" + RESET);
    }

    public void playAgainPrompt(){
        System.out.print("Would you like to play again? (y/n):");
    }

    @Override
    public void showErrorUnknownInput() {
        System.out.println(RED + "Unknown option" + RESET);
    }

    @Override
    public void showErrorInvalidPosition() {
        System.out.println(RED + "Invalid board position" + RESET);
    }

    public void showErrorInvalidInput() {
        System.out.println(RED + "Invalid input" + RESET);
    }

    public void clearConsole() {
        System.out.print("\033[H\033[2J");
    }
}
