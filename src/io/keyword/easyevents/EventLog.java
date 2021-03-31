package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * singleton class - only one instance
 * this class serves as a events collector
 * it provide add, search, delete and output events services
 * syn
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class EventLog {

    // FIELDS
    private NavigableSet<Event> events = new TreeSet<>(); // store all events; make it navigable
    private NavigableSet<Event> syncTreeSet = Collections.synchronizedNavigableSet(events); // for client - server version app

    private static EventLog eventLog = new EventLog();

    private LocalTime initialTime = LocalTime.now(); // current local time
    private long offset = 0;

    // CONSTRUCTORS - singleton
    private EventLog() {
    }

    // BUSINESS METHODS
    public static EventLog getInstance() {
        return eventLog;
    }

    /**
     * start this EventLog by creating an initiate event
     *
     * @param initialTime
     */
    public void start(LocalTime initialTime) {
        this.initialTime = initialTime;
        this.addEventNoOffset(LocalTime.of(0, 0, 0), "Initial Event - Session starts;");
    }

    public void start(String initialTime) {
        this.start(EasyEventsHelper.localTimeFromString(initialTime));
    }

    /**
     * inserting a last event in events collection
     *
     * @param endTime
     */
    public void end(LocalTime endTime) {
        this.addEventOffset(LocalTime.now(), "Last Event - Session ends;");
    }

    public void end(String endTime) {
        this.addEventNoOffset(EasyEventsHelper.localTimeFromString(endTime), "Last Event - Session ends;");
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
     * this allows the user to input specific time elapsed from initial time
     * @param timeStamp
     * @param description
     */
    public void addEventNoOffset(LocalTime timeStamp, String description) {
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
        if(timeStamp.isBefore(this.initialTime)){
            throw new EventConstructorInvalidInputException(String.format("Event timestamp %s is before the initial timestamp %s. Please try again!", timeStamp, this.initialTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        } else if(timeStamp.isAfter(LocalTime.now())){
            throw new EventConstructorInvalidInputException(String.format("Event timestamp %s is after the current local time %s. Please try again!", timeStamp, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        }
        Event event = this.createEvent(this.timeFromDuration(this.getDuration(initialTime, timeStamp)), description);
        this.syncTreeSet.add(event);
    }

    /**
     * display events in ascending order
     */
    public void dumpEvents() {
        this.getAllEvents().stream().forEach(System.out::println);
    }

    /**
     * collect a list of events those contain the input keyword in event's description
     *
     * @param keyword can be any String
     * @return list of
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

    public Event searchEvent(int id) {
        return this.getAllEvents().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    public Collection<Event> getAllEvents() {
        this.assignEventId();
        return this.events;
    }

    /**
     * remove an event from the event collection if that event is present
     *
     * @param id the order number of a target event in the collection
     * @return true if the event is present and successfully removed
     */
    public boolean deleteEvent(int id) {
        return this.syncTreeSet.remove(this.searchEvent(id));
    }

    // ASSESSOR METHODS

    /**
     * if offset exist
     *
     * @param time
     */
    public void setInitialTime(LocalTime time) {
        this.setOffset(time);
        this.initialTime = time;
    }

    public LocalTime getInitialTime() {
        return initialTime;
    }

    // HELPER METHODS
    private void setOffset(LocalTime time) {
        this.offset = this.getDuration(time, LocalTime.now()).toSeconds();
        if (Math.abs(offset) <= 1) {
            this.offset = 0;
        }
    }

    private long getOffset() {
        return this.offset;
    }

    /**
     * calculate duration between two events
     *
     * @param event1
     * @param event2
     * @return a Duration object - time-based amount of time
     */
    public Duration getDuration(Event event1, Event event2) {
        return Duration.between(event1.getEventTimeStamp(), event2.getEventTimeStamp());
    }

    public Duration getDuration(LocalTime event1, LocalTime event2) {
        return Duration.between(event1, event2);
    }

    public String timeFromDuration(Duration duration) {
        return String.format("%s:%s:%s", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    private Event createEvent(String timeStamp, String description) {
        return new Event(timeStamp, description);
    }

    private Event createEvent(LocalTime timeStamp, String description) {
        return new Event(timeStamp, description);
    }

    /**
     * allows user creating an event with only time
     * then edit description later
     *
     * @param timeStamp
     * @return
     */
    private Event createEvent(LocalTime timeStamp) {
        return new Event(timeStamp, "default");
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

    void clearEvents() {
        this.events = new TreeSet<>();
        syncTreeSet = Collections.synchronizedNavigableSet(events);
    }
}