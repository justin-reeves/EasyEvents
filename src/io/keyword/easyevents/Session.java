package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;
import io.keyword.easyevents.util.EasyEventsIO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
public enum Session {

    // FIELDS
    INSTANCE;
    private static EventLog eventLog;
    private String sessionName;


    // CONSTRUCTORS


    // BUSINESS METHODS
    public void execute() {
        EasyEventsIO.displaySession(
                getSessionName(),
                EasyEventsHelper.localtimeToFormattedString(eventLog.getInitialTime()));
        logEvents();
        endSession();
    }


    // ACCESSOR METHODS
    public static Session getInstance() throws EventConstructorInvalidInputException {
        eventLog = EventLog.getInstance();
        eventLog.start(LocalTime.now());
        INSTANCE.setSessionName("EasyEvents_" + LocalDate.now().toString());
        return INSTANCE;
    }

    public static Session getInstance(LocalTime time) throws EventConstructorInvalidInputException {
        Session s = getInstance();
        eventLog.start(time);
        return s;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalTime getInitialTime() {
        return eventLog.getInitialTime();
    }


    // HELPER METHODS
    private static void logEvents() {
        EasyEventsIO.info("You can now continuously enter events by using the 'event' command or\n" +
                "typing 'end' to end your logging session. Type 'help' at anytime for usage information\n\n");
        String timeCommandRegex = "-t +([01][\\d]|2[0-3]):[0-5][\\d]:[0-5][\\d]";

        // while user has not confirmed to 'end' the session it will keep looping until user confirms the 'end'
        while (true) {
            String input = EasyEventsIO.prompt(
                    "Enter command: ",
                    "^end$|" +
                            "^event$|" +
                            "^event +" + timeCommandRegex + " *$|" +
                            "help *.*",
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

    private static void createEvent(String input, String description) throws EventConstructorInvalidInputException {
        LocalTime time = LocalTime.now();

        if (input.contains("-t")) {
            time = EasyEventsHelper.localTimeFromString(input.split(" ")[2]);
        }

        eventLog.addEventOffset(time, description);
    }

    private static boolean confirmEnd() {
        return EasyEventsIO.prompt("Are you ready to end session (y/n)? ",
                "[yYnN]",
                "Your answer must be 'y', 'Y', 'n' or 'N'").matches("[yY]");
    }

    private void endSession() {
        LocalTime endTime = LocalTime.now();
        eventLog.end(endTime);

        String filename = writeToFile();
        int numEvents = eventLog.getAllEvents().size();

        EasyEventsIO.displayEnd(
                EasyEventsHelper.localtimeToFormattedString(endTime),
                numEvents,
                filename
        );
    }

    private String writeToFile() {
        String filename = "";
        try {
            filename = SessionWriter.writeFile(
                    getSessionName(),
                    SessionWriter.FileType.TXT,
                    eventLog.getAllEvents()).toString();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        return filename;
    }


    // OVERRIDES


}