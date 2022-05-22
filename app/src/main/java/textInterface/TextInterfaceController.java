package textInterface;

import battleship.BoardView;
import battleship.Point;

import battleship.Game;
import battleship.TwoPlayerGame;

import java.util.List;
import java.util.Locale;

import battleship.GameSettings;
import utils.Triple;

public class TextInterfaceController implements InputHandler {

    // This class does not represent an ADT

    /**
     * Possible orientations of Ships
     */
    private static final char[] ORIENTATIONS = new char[]{'u', 'd', 'l', 'r'};

    /**
     * The data-carrier and processor for the application.
     */
    private TwoPlayerGame game;

    /**
     * The user-facing view and input receiver for this application.
     */
    private final TextInterfaceView view;

    /**
     * Temporary data storing the pivot point of the ship to be placed
     */
    private Point p;

    private static final String[] CONTROLLER_PHASES = {"settings", "game", "play again"};
    private int controllerPhase;

    private GameSettings gameSettings;


    /**
     * Creates a new TextInterfaceController with the provided model and view
     * classes to manage.
     *
     * @param game A model to use for computation and data.
     * @param view A view to use to display data to the user.
     */
    public TextInterfaceController(TwoPlayerGame game, TextInterfaceView view) {
        this.game = game;
        this.view = view;
        this.gameSettings = new GameSettings();
        this.controllerPhase = 0;
    }

    public TextInterfaceController(TextInterfaceView view) {
        this(null, view);
    }


    /**
     * Begins the application by displaying some basic information
     * and prompting the user for the starting command.
     * <p>
     * On normal operation, this method does not return until the application
     * is being shut down. Calling this method transfers control of the current
     * thread to the provided {@link TextInterfaceView}.
     */
    @Override
    public void start() {
        view.welcome();
        settingsPrompt();
        view.begin();
    }

    /**
     * Responds to the text of the command depending on the current state of the program
     *
     * @param input The command text that was input.
     */
    @Override
    public void handleInput(String input) {
        if (input.isEmpty()) {
            System.out.println(input);
            return;
        }
        switch (this.controllerPhase) {
            case 0:
                doSettingsPhase(input);
                break;
            case 1:
                switch (game.getPhase()) {
                    // phases - setup
                    case "setup":
                        doShipSetup(input);
                        break;

                    // phases - attack
                    case "playing":
                        doInputAttack(input);
                        break;

                    case "end":
                        // umm? ideally this won't actually be reached since we should set controller phase to 2
                        // after the game ends
                        break;

                    default:
                        break;
                }
                break;
            case 2:
                parsePlayAgain(input);
                break;
            default:
                break;
        }

    }

    // the initial settings prompt after opening the game
    private void settingsPrompt() {
        doSettingsPhase("");
    }

    private void doSettingsPhase(String input) {
        // settings phase consists of going through all the settings and setting them
        Triple<String, GameSettings.OptionType, List<String>> curOption = gameSettings.getAvailableChoices();
        if (!input.equals("")) {
            switch (curOption.getSecond()) {
                case CHOICES:
                    try {
                        int val = Integer.parseInt(input);
                        if (val < 1 || val > curOption.getThird().size()){
                            // handles input in range of indices
                            view.showErrorInvalidInput();
                        } else {
                            gameSettings.setCurChoice(curOption.getThird().get(val - 1));
                        }
                    } catch (NumberFormatException notInteger){
                        // handles non-integer input
                        view.showErrorInvalidInput();
                    }
                    break;
                case RANGE:
                    try {
                        int val = Integer.parseInt(input);
                        if (val < Integer.parseInt(curOption.getThird().get(0)) || val > Integer.parseInt(curOption.getThird().get(1))) {
                            // handles input in range of values
                            view.showErrorInvalidInput();
                        } else {
                            gameSettings.setCurChoice(Integer.toString(Integer.parseInt(input))); // i think this will handle whitespace and some noninteger chars?
                        }
                    } catch (NumberFormatException notInteger){
                        // handles non-integer input
                        view.showErrorInvalidInput();
                    }
                    break;
                case TEXTENTRY:
                    gameSettings.setCurChoice(input);
                    // TODO - make Game class actually set up the names when playernames received through here
                    break;
                default:
                    // getting here indicates a programmer mistake
                    throw new RuntimeException("Encountered bad option type");
            }
        }
        if (gameSettings.isComplete()) {
            controllerPhase = 1; // end the game setup phase
            // initialize up new game
            if (game == null || game.getPhase().equals("end")) {
                game = new TwoPlayerGame(gameSettings);
            }
            // manually start the game w/ corresponding prompts
            // this sequence of calls was referenced from commit @9f4faf3
            // seems to be how the game was directly just started before
            view.placeShipPrompt();
            shipPointPrompt();
        } else {
            curOption = gameSettings.getAvailableChoices();
            settingsPrompt(curOption.getFirst(), curOption.getSecond(), curOption.getThird());
        }
    }

    /**
     * Settings prompt based on what the next setting to enter is
     * @param name the name of the setting
     * @param type the type of the setting - see GameSettings.OptionType
     * @param choices list of string choices for the setting
     */
    private void settingsPrompt(String name, GameSettings.OptionType type, List<String> choices) {
        // TODO: Game Settings calls to View
        view.setupPrompt(name);
        switch (type) {
            case CHOICES:
                view.showOptionsEnumerated(choices);
                break;
            case RANGE:
                view.showOptionRange(Integer.parseInt(choices.get(0)), Integer.parseInt(choices.get(1)));
                break;
            case TEXTENTRY:
                view.showOptionFreeform();
                break;
            default:
                // getting here indicates a programmer mistake
                throw new RuntimeException("Encountered bad option type");
        }
    }

    /**
     * Responds to user's input to set up the locations of their ship
     */
    private void doShipSetup(String input) {
        // check validity
        if (p == null && (input.length() < 2 || checkInvalidPoint(input))) {
            view.showErrorUnknownInput();
            view.placeShipOfLength(getShipLength());
            return;
        } else if (p != null && checkInvalidOrientation(input)) {
            view.showErrorUnknownInput();
            view.shipOrientationPrompt();
            return;
        }

        // deal with input
        if (p == null) {
            p = new Point(input.toUpperCase().charAt(0) - 'A', input.charAt(1) - '0');
            game.processTurn(p);
            view.shipOrientationPrompt();
        } else {
            char orientation = input.toLowerCase().charAt(0);
            // calculate second point, pass in to processTurn
            boolean valid = game.processTurn(calculateSecond(orientation, getShipLength()));

            // Note that p is not saved because if the Point is on a ship, both point and orientation need to be rechosen,
            // not just the orientation
            p = null;
            if (!valid) {
                view.showErrorInvalidPosition();
            }

            // check phase
            if (game.getPhase().equals("setup")) {
                shipPointPrompt();
            } else {
                view.drawBoard(game.getPlayerView(game.getCurrentPlayer()).get(0), game.getCurrentPlayerShipPoints());
                attackPrompt();
            }
        }
    }

    /**
     * Responds to user's input to attack coordinates
     */
    private void doInputAttack(String input) {
        if (input.length() < 2 || checkInvalidPoint(input)) {
            view.showErrorUnknownInput();
            attackPrompt();
            return;
        }

        // attack using the input point, and if invalid, display error
        if (!game.processTurn(new Point(input.toUpperCase().charAt(0) - 'A', input.charAt(1) - '0'))) {
            view.showErrorInvalidPosition();
        }

        // read current phase
        if (game.getPhase().equals("playing")) {
            attackPrompt();
        } else {
            view.showWinner(game.getCurrentPlayerName());
            this.controllerPhase = 2;
            view.playAgainPrompt();
            // and set the phases as well so we actually parse the play again response
            this.controllerPhase = 2;
        }
    }

    /**
     * Parses play again
     */
    private void parsePlayAgain(String input) {
        if (input.toLowerCase().charAt(0) == 'y') {
//            this.controllerPhase = 0;
//            this.gameSettings = new GameSettings(); // reset the game settings
//            settingsPrompt();
            // or alternatively just reset the game directly and start it again
            // if the user wants to reset the settings they can just quit the program and restart it
            // TODO: decide what we want to do here

            this.controllerPhase = 1;
            this.game = new TwoPlayerGame(this.gameSettings);
            view.placeShipPrompt();
            shipPointPrompt();
        } else {
            view.exit();
        }
    }

    /**
     * draws board and prompts for first point of ship
     */
    private void shipPointPrompt() {
        view.playerPrompt(game.getCurrentPlayerName());
        view.drawBoard(game.getPlayerView(game.getCurrentPlayer()).get(0), game.getCurrentPlayerShipPoints());
        view.placeShipOfLength(getShipLength());
    }

    /**
     * draws player boards for player attack phase and prompts for attack point
     */
    private void attackPrompt() {
        view.playerPrompt(game.getCurrentPlayerName());
        List<BoardView> boards = game.getPlayerView(game.getCurrentPlayer());
        view.drawBoard(boards.get(0), game.getCurrentPlayerShipPoints());
        for (int i = 1; i < boards.size(); i++) {
            view.drawBoard(boards.get(i));
        }
        view.attackPrompt();
    }

    /**
     * returns length of ship to be set up
     */
    private int getShipLength() {
        int[] temp = game.getShipsToBePlaced(game.getCurrentPlayer());
        int index = 0;
        while (index < temp.length && temp[index] == 0) {
            index++;
        }
        return index;
    }


    /**
     * private method to check if input point is valid
     */
    private boolean checkInvalidPoint(String input) {
        char letter = input.toUpperCase().charAt(0);
        char number = input.charAt(1);
        return letter < 'A' || letter > 'A' + game.size() - 1 || number < '0' || number > '0' + game.size() - 1;
    }

    /**
     * private method to check if input orientation is valid
     */
    private boolean checkInvalidOrientation(String input) {
        char check = input.toLowerCase().charAt(0);
        for (char c : ORIENTATIONS) {
            if (c == check) {
                return false;
            }
        }
        return true;
    }

    /**
     * private method to calculate second point
     */
    private Point calculateSecond(char orientation, int length) {
        length = length - 1;
        int x = p.getX();
        int y = p.getY();
        if (orientation == ORIENTATIONS[0]) { // u
            y -= length;
        } else if (orientation == ORIENTATIONS[1]) { // d
            y += length;
        } else if (orientation == ORIENTATIONS[2]) { // l
            x -= length;
        } else if (orientation == ORIENTATIONS[3]) { // r
            x += length;
        }
        return new Point(x, y);
    }
}
