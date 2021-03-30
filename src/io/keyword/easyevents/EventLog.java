package io.keyword.easyevents;

import java.time.Duration;
import java.time.Instant;
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
    private Collection<Event> events = new TreeSet<>(); // store all events
    private static final EventLog eventLog = new EventLog();
    Collection<Event> syncTreeSet = Collections.synchronizedCollection(events);

    // CONSTRUCTORS - singleton
    private EventLog(){}

    // BUSINESS METHODS
    public static EventLog getInstance() {
        return eventLog;
    }

    /**
     * add an event into collection without event id
     * @param timeStamp user input in format hh:mm:ss
     * @param description user input description
     */
    public void addEvent(String timeStamp, String description) {
        Event event = this.createEvent(timeStamp, description);
        this.syncTreeSet.add(event);
    }

    /**
     * display events in ascending order
     */
    public void dumpEvents(){
        this.getAllEvents().stream().forEach(System.out::println);
    }

    /**
     * collect a list of events those contain the input keyword in event's description
     *
     * @param keyword can be any String
     * @return list of
     */
    public List<Event> searchEvent(String keyword) {
        return this.getAllEvents().stream().filter(e -> e.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Event searchEvent(int id){
        return this.getAllEvents().stream().filter(e -> Objects.equals(e.getId(), id)).findFirst().orElse(null);
    }

    public Collection<Event> getAllEvents() {
        this.assignEventId();
        return this.events;
    }

    //TODO: make a delete event method
    public void deleteEvent() {
    }

    // HELPER METHODS
    public Duration getDuration(Event event1, Event event2){
        return this.getDuration(event1.getEventTimeStamp(), event2.getEventTimeStamp());
    }

    private Duration getDuration(Instant timeStamp1, Instant timeStamp2) {
        return Duration.between(timeStamp1, timeStamp2);
    }

    private Event createEvent(String timeStamp, String description) {
        return new Event(timeStamp, description);
    }

    /**
     * assign event id by ascending order
     */
    private void assignEventId(){
        Iterator<Event> iterator = this.syncTreeSet.iterator();
        for(int i = 1; i <= this.syncTreeSet.size(); i++) {
            Event e = iterator.next();
            e.setId(i);
        }
    }
}