package io.keyword.easyevents;

import i.keyword.easyevents.util.EasyEventsHelper;
import i.keyword.easyevents.util.EasyEventsIO;

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
    private EventLog eventslog;
    private String sessionName;
    private static final Session instance = new Session();


    // CONSTRUCTORS

    private Session() {
    }

    // BUSINESS METHODS
    public static Session getInstance() {
        return instance;
    }

    public void startSession() {
        eventslog = EventLog.getInstance();
        eventslog.setInitialTime(LocalTime.now());
        this.setSessionName("EasyEvents_" + LocalDate.now().toString());
        start();
    }

    public void startSession(String time ) {
        this.startSession();
        eventslog.setInitialTime( EasyEventsHelper.localTimeFromString(time)); // Convert String to localTime
        start();
    }

    public void startSessionOnlyName(String sessionName) {
        this.startSession();
        this.setSessionName(sessionName);
    }

    public void startSession(String time, String sessionName ) {
        this.startSession();
        eventslog.setInitialTime( EasyEventsHelper.localTimeFromString(time)); // Convert String to localTime
        this.setSessionName(sessionName);
    }

    public void createEvent(String sessionName){

    }


    public void endSession(){

    }

    public void createFile(){

    }


    // ACCESSOR METHODS

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }


    // HELPER METHODS
    private void start(){
        EasyEventsIO.println("Session event logging has started at " + eventslog.getInitialTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    // OVERRIDES


}