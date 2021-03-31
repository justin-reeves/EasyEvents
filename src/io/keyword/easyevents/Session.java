package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;
import io.keyword.easyevents.util.EasyEventsIO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
public class Session {

    // FIELDS
    private static EventLog eventslog;
    private String sessionName;
    private static final Session instance = new Session();


    // CONSTRUCTORS

    private Session() {
    }

    // BUSINESS METHODS


    public static Session getInstance() {
        eventslog = EventLog.getInstance();
        eventslog.setInitialTime(LocalTime.now());
        instance.setSessionName("EasyEvents_" + LocalDate.now().toString());
        return instance;
    }

    public static Session getInstance(LocalTime time) {
        Session s = getInstance();
        eventslog.setInitialTime(time);
        return s;
    }

    public static Session getInstance(String sessionName) {
        Session s = getInstance();
        s.setSessionName(sessionName);
        return s;
    }

    public static Session getInstance(String sessionName, LocalTime time) {
        Session s = getInstance();
        eventslog.setInitialTime(time);
        s.setSessionName(sessionName);
        return s;

    }

    public void start() {
        EasyEventsIO.info("Session event logging has started at " + eventslog.getInitialTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        logEvents();
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

        // while user has not confirmed to 'end' the session it will keep looping until user confirms the 'end'
        while (true) {
            String input = EasyEventsIO.prompt("Enter command: ",
                    "end|event|event -t ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]|help.*",
                    "Invalid command. Did you mean 'end' or 'entry'? Type 'help end' or 'help entry' for usage information.");

            if (input.startsWith("event")) {
                String description = EasyEventsIO.prompt("Description: ");
                createEvent(input, description);
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

    private static void createEvent(String input, String description) {
        LocalTime time = LocalTime.now();

        if (input.contains("-t")) {
            time = EasyEventsHelper.localTimeFromString(input.split(" ")[2]);
        }

        eventslog.addEvent(time, description);
    }


    public void endSession() {

    }

    public void createFile() {

    }


    // OVERRIDES


}