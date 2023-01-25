import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

public class Duke {
    private static final int cap = 100;
    private static ArrayList<Task> storage = new ArrayList<>(cap);

    public static void main(String[] args) throws Exception {
        Scanner sc;
        String input;
        Path path;

        try{
            String cwd = System.getProperty("user.dir");
            path = Paths.get(cwd, "data","Duke.txt");
            if (!Files.exists(path)){
                Files.createFile(path);
            }
        } catch (Exception e){
            throw new DukeException("Unable to access/create file");
        }
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

        sc = new Scanner(System.in);

        while(true){
            try {
                input = sc.nextLine();
                String lcInput = input.toLowerCase();
                String[] inputs = input.split(" ");
                if (lcInput.contains("bye")) {
                    if (inputs.length != 1) {
                        throw new DukeException("Command does not take in extra arguments!");
                    } else {
                        sc.close();
                        bye();
                    }
                } else if (lcInput.contains("list")) {
                    if (inputs.length != 1) {
                        throw new DukeException("Command does not take in extra arguments!");
                    } else {
                        list(path);
                    }
                } else if (lcInput.contains("deadline")) {
                    if (inputs.length <= 1) {
                        throw new DukeException("What is the deadline task????");
                    } else if (!lcInput.contains("/by")) {
                        throw new DukeException("Put in the deadline of your task Please!");
                    } else {
                        deadline(input, path);
                    }
                } else if (lcInput.contains("todo")) {
                    if (inputs.length <= 1) {
                        throw new DukeException("What is the todo task????");
                    } else {
                        todo(input, path);
                    }
                } else if (lcInput.contains("event")) {
                    if (!input.contains("/from") && !input.contains("/to")){
                        throw new DukeException("Period not specified!");
                    } else if (inputs.length <= 1){
                        throw new DukeException("What is the event task????");
                    }
                    events(input, path);
                } else if (Pattern.compile("\\D+.\\d+").matcher(input).find()){
                    int index = Integer.parseInt(inputs[1]);
                    if (inputs[0].equals("mark")) {
                        mark(inputs, index, path);
                    } else if (inputs[0].equals("unmark")) {
                        unmark(index, path);
                    } else if (inputs[0].equals("delete")) {
                        delete(index - 1, path);
                    } else {
                        throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                    }
                } else {
                    throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException e){
                System.out.println(
                        "_____________________________________\n"
                                + e.errorMessage + "\n"
                                + "_____________________________________\n"
                );
                continue;
            }

        }
    }

    private static void bye(){
        System.out.println(
                "_____________________________________\n"
                        + "Bye. Hope to see you again soon!\n"
                        + "_____________________________________\n"
        );

        System.exit(0);
    }

    private static void list(Path filePath) throws DukeException {
        try {
            List<String> tasklines = Files.lines(filePath)
                    .map(line -> line.trim())
                    .collect(Collectors.toList());

            System.out.println("_____________________________________\n");
            for (String line: tasklines){
                System.out.println(line + "\n");
            }
            System.out.println("_____________________________________\n");
        } catch (IOException e){
            throw new DukeException("Unable to access contents of File!");
        }
    }

    private static void deadline(String rawInput, Path filePath) throws IOException {
        String desc = rawInput.substring(
                rawInput.indexOf("deadline") + "deadline ".length(),
                rawInput.indexOf("/by")
        );

        String by = rawInput.substring(
                rawInput.indexOf("/by") + "/by ".length()
        );

        Task taskAdd = new Deadline(desc, by);
        storage.add(taskAdd);
        System.out.println("_____________________________________\n"
                + "Got it. I've added this task:\n"
                + " " + taskAdd.toString() +"\n"
                + taskCount() + "\n"
                + "_____________________________________\n"
        );

        String indexedTaskToAdd = storage.indexOf(taskAdd) + "|" + taskAdd.toString() + "\n";
        Files.write(filePath, indexedTaskToAdd.getBytes(), StandardOpenOption.APPEND);

    }

    private static void events(String rawInput, Path filePath) throws IOException {
        String desc = rawInput.substring(
                rawInput.indexOf("events") + "events ".length(),
                rawInput.indexOf("/from")
        );

        String from = rawInput.substring(
                rawInput.indexOf("/from") + "/from ".length(),
                rawInput.indexOf("/to")
        );

        String to = rawInput.substring(
                rawInput.indexOf("/to") + "/to ".length()
        );

        Task taskAdd = new Events(desc, from, to);
        storage.add(taskAdd);


        System.out.println("_____________________________________\n"
                + "Got it. I've added this task:\n"
                + " " + taskAdd.toString() +"\n"
                + taskCount() + "\n"
                + "_____________________________________\n"
        );

        String indexedTaskToAdd = storage.indexOf(taskAdd) + "|" + taskAdd.toString() +"\n";
        Files.write(filePath, indexedTaskToAdd.getBytes(), StandardOpenOption.APPEND);
    }

    private static void todo(String rawInput, Path filePath) throws IOException {
        String desc = rawInput.substring(
                rawInput.indexOf("todo") + "todo ".length()
        );

        Task taskAdd = new ToDo(desc);
        storage.add(taskAdd);
        System.out.println("_____________________________________\n"
                + "Got it. I've added this task:\n"
                + " " + taskAdd.toString() +"\n"
                + taskCount() + "\n"
                + "_____________________________________\n"
        );

        String indexedTaskToAdd = storage.indexOf(taskAdd) + "|" + taskAdd.toString() + "\n";
        Files.write(filePath, indexedTaskToAdd.getBytes(), StandardOpenOption.APPEND);
    }

    private static String taskCount(){
        int newCount = storage.size();
        String task = newCount == 1 ? " task" : " tasks";
        return "Now you have " + newCount + task + " in the list.";
    }

    private static void delete(int index, Path filePath) throws DukeException{
        try {
            List<String> newTaskLines = Files.lines(filePath)
                    .map(line -> line.trim())
                    .filter(line -> !line.contains(
                            index + "|" +
                            storage.get(index).toString()))
                    .collect(Collectors.toList());

            for (String l : newTaskLines){
                Files.write(filePath, l.getBytes(), StandardOpenOption.APPEND);
            }
            Task taskRemoved = storage.remove(index);
            System.out.println(
                    "_____________________________________\n"
                            +  "Noted. I've removed this task:\n"
                            +  " " + taskRemoved.toString() + "\n"
                            + taskCount() + "\n"
                            + "_____________________________________\n"
            );
        } catch (IndexOutOfBoundsException err){
            throw new DukeException("Invalid index given!");
        } catch (IOException e){
            throw new DukeException("Unable to access content of file");
        }

    }

    private static void mark(String[] args, int index, Path filePath) throws DukeException{
        if (args[0].equals("mark")) {
            try {
                storage.get(index - 1).markAsDone();
                List<String> updateString = Files.lines(filePath)
                                .map(line -> {
                                    line.trim();
                                    if (line.contains(index + "|")){
                                        return index + "|" + storage.get(index).toString() + "\n";
                                    } else {
                                        return line;
                                    }
                                })
                                        .collect(Collectors.toList());
                for (String s: updateString){
                    Files.write(filePath, s.getBytes(), StandardOpenOption.APPEND);
                }
                System.out.println(
                        "_____________________________________\n"
                                + "Nice! I've marked this task as done\n"
                                + " " + storage.get(index - 1).toString() + "\n"
                                + "_____________________________________\n"
                );
            } catch (IndexOutOfBoundsException err) {
                throw new DukeException("Invalid index given!");
            } catch (IOException e){
                throw new DukeException("Unable to access content of file!");
            }
        }
    }

    private static void unmark(int index, Path filePath) throws DukeException{
        try {
            storage.get(index - 1).unMark();
            List<String> updateString = Files.lines(filePath)
                    .map(line -> {
                        line.trim();
                        if (line.contains(index + "|")){
                            return index + "|" + storage.get(index).toString();
                        } else {
                            return line;
                        }
                    })
                    .collect(Collectors.toList());
            for (String s: updateString){
                Files.write(filePath, s.getBytes(), StandardOpenOption.APPEND);
            }
            System.out.println(
                    "_____________________________________\n"
                            + "Ok, I've marked this task as not done yet\n"
                            + " " + storage.get(index - 1).toString() + "\n"
                            + "_____________________________________\n"
            );
        } catch (IndexOutOfBoundsException err) {
            throw new DukeException("Invalid index given!");
        } catch (IOException e){
            throw new DukeException("Unable to access contents of file");
        }
        //try{
            //System.out.println(logo);
            //"todo \D+"
            //deadline .+/by \d{2}/\d{2}/\d{4}
            //event .+/from \d{2}/\d{2}/\d{4} /to \d{2}/\d{2}/\d{4}
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();
            while (true){
                Pattern todoPattern = Pattern.compile("todo \\D+");
                Pattern deadlinePattern = Pattern.compile("deadline .+/by \\d{2}/\\d{2}/\\d{4}");
                Pattern eventPattern = Pattern.compile("event .+/from \\d{2}/\\d{2}/\\d{4} /to \\d{2}/\\d{2}/\\d{4}");
                if (todoPattern.matcher(command).find()){
                    System.out.println("Todo Task added succesfully!");
                } else if (deadlinePattern.matcher(command).find()){
                    System.out.println("Deadline Task added succcesfully!");
                } else if (eventPattern.matcher(command).find()){
                    System.out.println("Event task added successfully!");
                } else {
                    System.out.println("Incorrect command given!");
                }
            }
        //} catch(Exception e){

        //}
    }


}

