package io.keyword.easyevents;

import java.time.LocalTime;

/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class Sessions {

    // FIELDS
    private LocalTime initialTime;
    private EventLog eventslog = EventLog.getInstance();
    private String sessionName;
    private static final Sessions instance = new Sessions();


    // CONSTRUCTORS


    private Sessions() {
    }

    // BUSINESS METHODS
    public static Sessions getInstance(){
        return instance;
    }
    public void startSession(){
        initialTime = LocalTime.now();
        Sessions session = getInstance();
        session.startSession();

    }
    public void createEventLog(){

    }



    // ACCESSOR METHODS


    // HELPER METHODS


    // OVERRIDES


}