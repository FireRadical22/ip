package duke;

import java.util.Scanner;

/**
 * {@code UI} class encapsulates UI between user and program
 */
public class UI {

    /**
     * {@code Scanner} object that prompts input from user
     */
    private static Scanner sc = new Scanner(System.in);

    /**
     * Empty constructor method for UI class
     */
    public UI() {
    }

    /**
     * Prints out introductory message when program first starts up
     */
    public void welcome() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);


        String intro = "_____________________________________\n"
                + "Hello! I'm Duke\n"
                + "What can I do for you?\n"
                + "_____________________________________\n";
        System.out.println(intro);
    }

    /**
     * Prints out error message when program fails to start up
     */
    public void showLoadingError() {
        System.out.println("Unable to start up program.");
        System.exit(-1);
    }

    /**
     * Prints out error message stored in {@code DukeException} object
     * @param dukeError error message in DukeException object
     */
    public void displayError(DukeException dukeError){
        System.out.println(dukeError.errorMessage);
    }

    /**
     * Stops prompting user for input and terminates the program
     */
    public static void end() {
        System.out.println(
                "_____________________________________\n"
                        + "Bye. Hope to see you again soon!\n"
                        + "_____________________________________\n"
        );
        sc.close();
        System.exit(0);
    }

    /**
     * Prompts user to give input on command line
     * @return input from user
     */
    public String getInput() {
        return sc.nextLine();
    }
}
