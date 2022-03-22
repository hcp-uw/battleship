package textInterface;

public class TextInterfaceController implements InputHandler{

    // This class does not represent an ADT

    /**
     * The data-carrier and processor for the application.
     */
    private Game game;

    /**
     * The user-facing view and input receiver for this application.
     */
    private TextInterfaceView view;

    /**
     *  The state/phase of the game
     */
    // TODO: this could be stored in the Game
    private String state;

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
        this.state = "start";
    }


    /**
     * Begins the application by displaying some basic information
     * and prompting the user for the starting command.
     * <p>
     * On normal operation, this method does not return until the application
     * is being shut down. Calling this method transfers control of the current
     * thread to the provided {@link TextInterfaceView}.
     */
    public void startMenu() {
        view.startMenu(); // for now, asks what mode the player would like (2p or CPU)
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
        switch(state){
            // phases - menu
            case "start":
                doInputStart(input);
                break;

            // phases - setup
            case "setup":
                doInputSetup(input);
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
     * Responds to user's choice of the mode in the start menu
     */
    private void doInputStart(String input){
        if (!input.equals("1") && !input.equals("2")){
            view.showErrorUnknownCommand();
            view.startMenu();
        }
        if (input.equals("1")){
            // TODO: need to know exact parameters/implementation in Game
            game.setup(0, 2);
        } else {
            game.setup(1, 1);
        }
    }

    /**
     * Responds to user's input to set up the locations of their ship
     */
    private void doInputSetup(String input){
        String[] parsed = input.split(" ");
        if (parsed.length < 2 || checkInvalidPoint(parsed[0]) || checkInvalidPoint(parsed[1])){
            view.showErrorUnknownCommand();
            view.setupPrompt();
        }
        // TODO: update state of game and learn that setup phase has passed when 5 ships have been placed
        // TODO: restrict valid inputs by their ship length
        game.addShip(parsed[0].charAt(0) - 'A', parsed[0].charAt(1) - '0', parsed[1].charAt(0) - 'A', parsed[1].charAt(1) - '0')
    }

    /**
     * Responds to user's input to attack coordinates
     */
    private void doInputAttack(String input){
        if (input.length() < 2 || checkInvalidPoint(input)){
            view.showErrorUnknownCommand();
            view.attackPrompt();
        }
        // TODO: how to call attack given CPU or local mode, and how to indicate which player is being attacked
        game.attack(input.charAt(0) - 'A', input.charAt(1) - '0'); // converted to point in the Game class
    }

    /**
     * private method to check if input point is valid
     */
    private boolean checkInvalidPoint(String input){
        return input.charAt(0) < 'A' || input.charAt(0) > 'A' + game.size() ||
                input.charAt(1) < '0' || input.charAt(1) > '0' + game.size();
    }
}
