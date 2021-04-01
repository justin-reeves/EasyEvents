package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * singleton class - one session has only one instance of EventLog
 * this class serves as a collector of events
 * it provide add, search, delete, output events and supported services
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class EventLog {

    //region FIELDS
    private SortedSet<Event> events = new TreeSet<>(); // store all events; make it navigable
    private SortedSet<Event> syncTreeSet = Collections.synchronizedSortedSet(events); // for distributed client - server version app
    private static final EventLog eventLog = new EventLog();
    private LocalTime initialTime = LocalTime.now(); // current local time
    private long offset = 0; // store offset time and print in output file;
    //endregion

    //region CONSTRUCTORS - singleton
    private EventLog() {
    }
    //endregion

    //region BUSINESS METHODS
    public static EventLog getInstance() {
        return eventLog;
    }

    /**
     * start this EventLog
     * by creating an initiate event and recording the initial time
     *
     * @param initialTime time when the eventLog is created
     */
    public void start(LocalTime initialTime) {
        if (initialTime.isAfter(LocalTime.now())) {
            throw new EventConstructorInvalidInputException(String.format("Session initial time %s is after the current local time %s. Please try again!"
                    , initialTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    , LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        }
        this.setInitialTime(initialTime);
        this.addEventNoOffset(LocalTime.of(0, 0, 0), "Initial Event - Session starts;");
    }

    public void start(String initialTime) {
        this.start(EasyEventsHelper.localTimeFromString(initialTime));
    }

    /**
     * inserting a last event in events collection
     * cannot insert an event in the future
     *
     * @param endTime the end event's time stamp e.g. LocalTime.now() or "11:11:11"
     */
    public void end(LocalTime endTime) {
        this.addEventOffset(endTime, "Last Event - Session ends;");
    }

    public void end(String endTime) {
        this.addEventNoOffset(EasyEventsHelper.localTimeFromString(endTime), "Last Event - Session ends;");
    }

    /**
     * this allows the user to input specific time elapsed from initial time
     *
     * @param timeStamp   time elapsed from initial time
     * @param description event's description
     */
    public void addEventNoOffset(LocalTime timeStamp, String description) {
        //TODO: for testing purpose comment out this part
        /*LocalTime currentTime = this.getInitialTime().plus(timeStamp.toSecondOfDay(), ChronoUnit.SECONDS);
        if (currentTime.isAfter(LocalTime.now())) {
            throw new EventConstructorInvalidInputException(String.format("Event timestamp %s is  after the current local time %s. Please try again!", timeStamp, this.initialTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        }*/
        Event event = this.createEvent(timeStamp, description);
        this.syncTreeSet.add(event);
    }

    /**
     * use this method to allow EventLog to calculate duration from initialTime to a given LocalTime
     *
     * @param timeStamp   user provided LocalTime
     * @param description event description
     */
    public void addEventOffset(LocalTime timeStamp, String description) {
        if (timeStamp.isBefore(this.initialTime)) {
            throw new EventConstructorInvalidInputException(String.format("Event timestamp %s is before the initial timestamp %s. Please try again!", timeStamp, this.initialTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        } else if (timeStamp.isAfter(LocalTime.now())) {
            throw new EventConstructorInvalidInputException(String.format("Event timestamp %s is after the current local time %s. Please try again!", timeStamp, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        }
        Event event = this.createEvent(this.timeFromDuration(this.getDuration(initialTime, timeStamp)), description);
        this.syncTreeSet.add(event);
    }

    /**
     * collect a list of events those contain the input keyword in each event's description
     *
     * @param keyword can be any String
     * @return list of events or empty list if cannot match keyword
     */
    public List<Event> searchEvent(String keyword) {
        List<Event> list;
        if (Objects.isNull(keyword)) {
            list = new ArrayList<>();
        } else {
            list = this.getAllEvents().stream().filter(e -> e.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return list;
    }

    /**
     * search event in collections by id
     *
     * @param id event's sequence id
     * @return single event object or null if no existence
     */
    public Event searchEvent(int id) {
        return this.getAllEvents().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    /**
     * display events in ascending order
     */
    public void dumpEvents() {
        this.getAllEvents().forEach(System.out::println);
    }

    /**
     * remove an event from the event collection if that event is present
     *
     * @param id the order number of a target event in the collection
     */
    public void deleteEvent(int id) {
        this.syncTreeSet.remove(this.searchEvent(id));
    }

    /**
     * allocate a new collection; let JVM takes care the old one
     * for testing, not for public yet
     */
    void clearEvents() {
        this.events = new TreeSet<>();
        syncTreeSet = Collections.synchronizedSortedSet(events);
    }
    //endregion

    //region ASSESSOR METHODS
    public LocalTime getInitialTime() {
        return initialTime;
    }

    /**
     * calculate offset between input initial time and current LocalTime
     * then assign given time to initialTime
     *
     * @param time user input initial time
     */
    private void setInitialTime(LocalTime time) {
        this.setOffset(time);
        this.initialTime = time;
    }

    public Event getFirstEvent() {
        this.assignEventId();
        return this.syncTreeSet.first();
    }

    public Event getLastEvent() {
        this.assignEventId();
        return this.syncTreeSet.last();
    }

    /**
     * mark sorted event's sequence and return it
     *
     * @return sorted events with timestamps
     */
    public Collection<Event> getAllEvents() {
        this.assignEventId(); // assign event id on sorted events
        return this.syncTreeSet;
    }
    //endregion

    //region HELPER METHODS

    /**
     * calculate duration between two events
     *
     * @param event1 first event
     * @param event2 second event
     * @return a Duration object - time-based amount of time
     */
    private Duration getDuration(LocalTime event1, LocalTime event2) {
        return Duration.between(event1, event2);
    }

    private String timeFromDuration(Duration duration) {
        return String.format("%s:%s:%s", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    /**
     * create an new event then return this event instance
     *
     * @param timeStamp   current local time; time elapsed will be calculated
     * @param description event description
     * @return new Event instance
     */
    private Event createEvent(String timeStamp, String description) {
        return new Event(timeStamp, description);
    }

    private Event createEvent(LocalTime timeStamp, String description) {
        return new Event(timeStamp, description);
    }

    /**
     * assign event id by ascending order
     */
    private void assignEventId() {
        Iterator<Event> iterator = this.syncTreeSet.iterator();
        for (int i = 1; i <= this.syncTreeSet.size(); i++) {
            Event e = iterator.next();
            e.setId(i);
        }
    }

    /**
     * calculate offset time if this EventLog initial time is earlier than the LocalTime
     */
    private void setOffset(LocalTime time) {
        this.offset = this.getDuration(time, LocalTime.now()).toSeconds();
        if (Math.abs(offset) <= 1) {
            this.offset = 0; // no offset, if difference is less than and equal to 1
        }
    }
    //endregion
}