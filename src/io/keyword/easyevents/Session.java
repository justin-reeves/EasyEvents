package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;
import io.keyword.easyevents.util.EasyEventsIO;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
public class Session {

    // FIELDS
    private static EventLog eventLog;
    private String sessionName;
    private static final Session instance = new Session();


    // CONSTRUCTORS

    private Session() {
    }

    // BUSINESS METHODS


    public static Session getInstance() throws EventConstructorInvalidInputException{
        eventLog = EventLog.getInstance();
        eventLog.start(LocalTime.now());
        instance.setSessionName("EasyEvents_" + LocalDate.now().toString());
        return instance;
    }

    public static Session getInstance(LocalTime time) throws EventConstructorInvalidInputException{
        Session s = getInstance();
        eventLog.start(time);
        return s;
    }

    public static Session getInstance(String sessionName) {
        Session s = getInstance();
        s.setSessionName(sessionName);
        return s;
    }

    public static Session getInstance(String sessionName, LocalTime time) throws EventConstructorInvalidInputException {
        Session s = getInstance();
        eventLog.start(time);
        s.setSessionName(sessionName);
        return s;

    }

    public void execute() {
        EasyEventsIO.info(
                String.format(
                        "Logging for session '%s' started at %s\n",
                        getSessionName(),
                        EasyEventsHelper.localtimeToFormattedString(eventLog.getInitialTime())));
        logEvents();
        endSession();
    }


    // ACCESSOR METHODS

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }


    // HELPER METHODS
    private static void logEvents() {
        EasyEventsIO.info("You can now continuously enter events by using the 'event' command or\n" +
                "typing 'end' to end your logging session. Type 'help' at anytime for usage information\n");
        String timeCommandRegex = "-t +([01][\\d]|2[0-3]):[0-5][\\d]:[0-5][\\d]";

        // while user has not confirmed to 'end' the session it will keep looping until user confirms the 'end'
        while (true) {
            String input = EasyEventsIO.prompt(
                    "Enter command: ",
                    "^end$|" +
                            "^event$|" +
                            "^event +" + timeCommandRegex + " *$|" +
                            "help .*",
                    "Invalid command. Did you mean 'end' or 'event'? Type 'help end' or 'help event' for usage information.");

            if (input.startsWith("event")) {
                String description = EasyEventsIO.prompt("Description: ");

                try {
                    createEvent(input, description);
                } catch (EventConstructorInvalidInputException e) {
                    System.out.println(e.getMessage());
                }

            } else if (input.startsWith("help")) {
                EasyEventsIO.displayUsage(input);
            } else if (confirmEnd()) {
                break;
            }
        }
    }

    private static boolean confirmEnd() {
        return EasyEventsIO.prompt("Are you ready to end session (y/n)? ",
                "[yYnN]",
                "Your answer must be 'y', 'Y', 'n' or 'N'").matches("[yY]");
    }

    private static void createEvent(String input, String description) throws EventConstructorInvalidInputException {
        LocalTime time = LocalTime.now();

        if (input.contains("-t")) {
            time = EasyEventsHelper.localTimeFromString(input.split(" ")[2]);
        }

        eventLog.addEventOffset(time, description);
    }


    public void endSession() {
        eventLog.end(LocalTime.now());
        EasyEventsIO.info(String.format("\nSession event logging was stopped at %s\n",
                EasyEventsHelper.localtimeToFormattedString(LocalTime.now())));

        writeToFile();

        EasyEventsIO.info("Thank you for using Easy Events! Goodbye.");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ignored) {
        }
    }

    private void writeToFile() {
        try {
            Path p = SessionWriter.writeFile(getSessionName(), SessionWriter.FileType.TXT, eventLog.getAllEvents());
            System.out.println(p);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }


    // OVERRIDES


}