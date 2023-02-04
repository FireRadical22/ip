package duke;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@code Duke} class that encapsulates Duke program
 */
public class Duke extends Application {
    /**
     * Duke's UI feature
     */
    private UI ui = new UI();

    /**
     * filePath of file to be accessed or edited
     */
    private Path path;

    /**
     * stores a list of tasks to complete
     */
    private TaskList taskList;

    /**
     * stores data of file specified by path
     */
    private Storage storage;

    /**
     * handles gui of Duke Application
     */
    private GUI gui= new GUI();

    /**
     * Default constructor to circumvent NoSuchMethodException problem when running
     * Application.launch
     *
     * Credit to @rmj1405 for providing the tip :)
     */
    public Duke() {}

    /**
     * Constructor method of {@code Duke} class
     * @param filePath path of file to be accessed or edited
     */
    public Duke(String filePath) {
        path = Paths.get(filePath, "data", "Duke.txt");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            storage = new Storage(path);
            taskList = new TaskList(storage.loadLines());
        } catch (InvalidPathException err) {
            ui.showLoadingError();
        } catch (IOException errIO) {
            System.out.println("Unable to create File!");
            System.exit(-1);
        } catch (DukeException dukeErr) {
            ui.displayError(dukeErr);
        }
    }

    /**
     * Starts Duke Application with default stage provided
     * @param stage default stage provided to start the application
     */
    @Override
    public void start(Stage stage) {
        gui.startUpProgram(stage);
        gui.runEvent(taskList, storage);
    }

    /**
     * runs Duke program
     */
    public void run() {
        ui.welcome();
        try {
            Storage store = new Storage(path);
            while (true) {
                try {
                    Parser parser = new Parser(ui.getInput());
                    Command commandHandler = parser.parseArgs();
                    commandHandler.execArgs(taskList);
                    store.editFile(this.taskList.loadTaskList());
                } catch (DukeException err) {
                    ui.displayError(err);
                }
            }
        } catch (DukeException err) {
            ui.displayError(err);
        }
    }

    /**
     * @param args array of command line arguments if any
     */
    public static void main(String[] args) {
        new Duke(System.getProperty("user.dir")).run();
    }
}
