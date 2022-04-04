package textInterface;

import battleship.Point;

import battleship.Game;

public class TextInterfaceController implements InputHandler{

    // This class does not represent an ADT

    /**
     * Possible orientations of Ships
     */
    private static final char[] ORIENTATIONS = new char[]{'u', 'd', 'l', 'r'};

    /**
     * The data-carrier and processor for the application.
     */
    private Game game;

    /**
     * The user-facing view and input receiver for this application.
     */
    private TextInterfaceView view;

    /**
     * Temporary data storing the pivot point of the ship to be placed
     */
    private Point p;


    /**
     * Creates a new TextInterfaceController with the provided model and view
     * classes to manage.
     *
     * @param game A model to use for computation and data.
     * @param view  A view to use to display data to the user.
     */
    public TextInterfaceController(Game game, TextInterfaceView view){
        this.game = game;
        this.view = view;
    }


    /**
     * Begins the application by displaying some basic information
     * and prompting the user for the starting command.
     * <p>
     * On normal operation, this method does not return until the application
     * is being shut down. Calling this method transfers control of the current
     * thread to the provided {@link TextInterfaceView}.
     */
    public void start() {
        // TODO: decide what is returned by game.getShipsToBePlaced(int pid)
        view.placeShipPrompt(game.getShipsToBePlaced(game.getCurrentPlayer()).get(0));
        view.begin();
    }

    /**
     * Responds to the text of the command depending on the current state of the program
     *
     * @param input The command text that was input.
     */
    @Override
    public void handleInput(String input) {
        if(input.isEmpty()) {
            System.out.println(input);
            return;
        }
        switch(game.getPhase()){
            // phases - setup
            case "setup":
                doShipSetup(input);
                break;

            // phases - attack
            case "attack":
                doInputAttack(input);
                break;

            default:
                break;
        }
    }

    /**
     * Responds to user's input to set up the locations of their ship
     */
    private void doShipSetup(String input){
        // check validity
        if (p == null && (input.length() < 2 || checkInvalidPoint(input))) {
            view.showErrorUnknownInput();
            view.placeShipPrompt(game.getShipsToBePlaced(game.getCurrentPlayer()).get(0));
            return;
        } else if (p != null && checkInvalidOrientation(input)){
            view.showErrorUnknownInput();
            view.shipOrientationPrompt();
            return;
        }

        // deal with input
        if (p == null){
            p = new Point(input.charAt(0) - 'A', input.charAt(1) - '0');
            view.shipOrientationPrompt();
        } else {
            char orientation = input.toLowerCase().charAt(0);
            boolean valid = game.addShip(p, calculateSecond(orientation,game.getShipsToBePlaced(game.getCurrentPlayer()).get(0));

            // Note that p is not saved because if the Point is on a ship, both point and orientation need to be rechosen,
            // not just the orientation
            p = null;
            if (!valid) {
                view.showErrorInvalidPosition();
            }

            // check phase
            if (game.getPhase().equals("setup")){
                view.placeShipPrompt(game.getShipsToBePlaced(game.getCurrentPlayer()).get(0));
            } else {
                view.attackPrompt(game.getCurrentPlayer());
            }
        }
    }

    /**
     * Responds to user's input to attack coordinates
     */
    private void doInputAttack(String input){
        if (input.length() < 2 || checkInvalidPoint(input)){
            view.showErrorUnknownInput();
            view.attackPrompt(game.getCurrentPlayer());
        }
        Point temp = new Point(input.toUpperCase().charAt(0), input.charAt(1));

        // TODO: discuss how to get attacked player (only for 2p) - possibly overload method
        game.attack(temp, );

        // read current phase
        if (game.getPhase().equals("attack")){
            view.attackPrompt(game.getCurrentPlayer());
        } else {
            view.showWinner(game.getCurrentPlayer());
            view.exit();
        }
    }

    /**
     * private method to check if input point is valid
     */
    private boolean checkInvalidPoint(String input){
        char letter = input.toUpperCase().charAt(0);
        char number = input.charAt(1);
        return letter < 'A' || letter > 'A' + game.size() || number < '0' || number > '0' + game.size();
    }

    /**
     * private method to check if input orientation is valid
     */
    private boolean checkInvalidOrientation(String input) {
        char check = input.toLowerCase().charAt(0);
        for (char c: ORIENTATIONS){
            if (c == check){
                return false;
            }
        }
        return true;
    }

    /**
     * private method to calculate second point
     */
    private Point calculateSecond(char orientation, int length){
        int x = p.getX();
        int y = p.getY();
        if (orientation == ORIENTATIONS[0]) {
            y -= length;
        } else if (orientation == ORIENTATIONS[1]){
            y += length;
        } else if (orientation == ORIENTATIONS[2]){
            x -= length;
        } else if (orientation == ORIENTATIONS[3]) {
            x += length;
        }
        return new Point(x, y);
    }
}
