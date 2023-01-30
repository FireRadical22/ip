package duke;

/**
 * {@code Parser} class encapsulates parsing of raw inputs
 * from command line for {@code Command} class to make sense
 */
public class Parser {
    /**
     * Array of arguments
     */
    private String[] args;

    /**
     * Constructor method for Parser class
     * @param argLine raw input from command line
     */
    public Parser(String argLine){
        this.args = argLine.split(" ", 2);
    }

    /**
     * splits raw input array of string to be passed as inputs for {@code Command} class
     * @return new Command object
     */
    public Command parseArgs() {
        if (this.args.length < 2) {
            return new Command(this.args[0], "");
        } else {
            return new Command(this.args[0], this.args[1]);
        }
    }
}
