package duke.task;

import java.time.LocalDateTime;

/**
 * {@code Deadline} class encapsulates a deadline Task.
 */
public class Deadline extends Task{
    /**
     * {@code LocalDateTime} object that stores date and time
     * of deadline
     */
    protected LocalDateTime deadline;

    /**
     * Constructor method for {@code Deadline} class
     * @param description description of Deadline Task
     * @param deadline String representation of date and time of deadline to be reached
     */
    public Deadline(String description, String deadline){
        super(description);
        this.deadline = parseDateTime(deadline);
    }

    /**
     * Provides string representation of {@code Deadline} object
     * @return string representation of Deadline
     */
    @Override
    public String toString(){
        return "D" + " | " + super.toString() + " (by: " + this.deadline.toString() + ")";
    }

    /**
     * converts String to {@code LocalDateTime} object that contains day and time
     * of deadline
     * @param deadline String representation of day and time of deadline
     * @return new {@code LocalDateTime} object containing day and time
     */
    private LocalDateTime parseDateTime(String deadline){
        String[] dateAndTime = deadline.split(" ");
        String[] dayMonthYear = dateAndTime[0].split("/");
        int hour = Integer.valueOf(dateAndTime[1])/100;
        int minute = Integer.valueOf(dateAndTime[1])%100;
        int[] ddMMYY = new int[3];
        for (int i = 0; i < dayMonthYear.length;i++){
            ddMMYY[i] = Integer.valueOf(dayMonthYear[i]);
        }

        return LocalDateTime.of(ddMMYY[2], ddMMYY[1], ddMMYY[0], hour, minute);
    }
}
