package io.keyword.easyevents;

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
        //eventslog.setInitialTime(LocalTime.now());
        instance.setSessionName("EasyEvents_" + LocalDate.now().toString());
        return instance;
    }

    public static Session getInstance(LocalTime time) {
        Session s = getInstance();
        // eventslog.setInitialTime((time)); // Convert String to localTime
        return s;
    }

    public static Session getInstance(String sessionName) {
        Session s = getInstance();
        s.setSessionName(sessionName);
        return s;
    }

    public static Session getInstance(String sessionName, LocalTime time) {
        Session s = getInstance();
        // eventslog.setInitialTime((time)); // Convert String to localTime
        s.setSessionName(sessionName);
        return s;

    }

    public void start() {
        EasyEventsIO.info("Session event logging has started \n");
        //EasyEventsIO.info("Session event logging has started at " + eventslog.getInitialTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }



    public void createEvent(String sessionName) {

    }


    public void endSession() {

    }

    public void createFile() {

    }


    // ACCESSOR METHODS

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }


    // HELPER METHODS
    private void logEvent(){
        EasyEventsIO.info("You can now continuously enter events by using the 'event' command or\n"+
                "typing 'end' to end your logging session. Type 'help' at anytime for usage information\n");
        // while user has not confirmed to 'end' the session it will keep looping until user confirms the 'end'
        while(true){
            String input = EasyEventsIO.prompt("",
                    "end|event|event -t ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",
                    "Invalid command. Type 'help end' or 'help entry' for usage information.");

            if(input.equals("end") && confirmEnd()){
                break;
            }
            else {
                String[] entryCommand = input.split(" ");

                if (entryCommand.length == 1) {
                    session = Session.getInstance();
                } else if (entryCommand.length == 2) {
                    if (entryCommand[1].startsWith("n")) {
                        session = Session.getInstance(entryCommand[1].substring(2).trim());
                    } else {
                        session = Session.getInstance(LocalTime.parse(entryCommand[1].substring(2).trim()));
                    }
                }
            }
        }


    }

    private boolean confirmEnd() {
        return true;
    }


    // OVERRIDES


}