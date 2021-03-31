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

    }


    // OVERRIDES


}