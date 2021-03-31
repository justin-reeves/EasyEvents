package io.keyword.easyevents;

import i.keyword.easyevents.util.EasyEventsHelper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
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
    private static final EventLog eventLog = new EventLog();
    private NavigableSet<Event> syncTreeSet = Collections.synchronizedNavigableSet(events); // for client - server version app

    private LocalTime initialTime = LocalTime.now(); // current local time

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
        this.addEvent("00:00:00", "Initial Event - Session starts;");
    }

    public void start(String initialTime) {
        this.start(EasyEventsHelper.localTimeFromString(initialTime));
    }

    /**
     * inserting a last event in events collection
     * @param endTime
     */
    public void end(LocalTime endTime) {
        this.timeFromDuration(this.getDuration(endTime, initialTime));
        this.addEvent(endTime, "Last Event - Session ends;");
    }

    public void end(String endTime) {
        this.end(EasyEventsHelper.localTimeFromString(endTime));
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
     * add an event into collection without event id
     *
     * @param timeStamp   user input in format hh:mm:ss
     * @param description user input description
     */
    public void addEvent(String timeStamp, String description) {
        this.syncTreeSet.add(this.createEvent(timeStamp, description));
    }

    public void addEvent(LocalTime timeStamp, String description) {
        Event event = this.createEvent(this.timeFromDuration(this.getDuration(initialTime, timeStamp)),description);
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
        if(Objects.isNull(keyword)){
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
    public LocalTime getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(LocalTime time){
        this.initialTime = time;
    }
    // HELPER METHODS

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

    private Duration getDuration(Instant timeStamp1, Instant timeStamp2) {
        return Duration.between(timeStamp1, timeStamp2);
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
}