package io.keyword.easyevents;

import io.keyword.easyevents.util.EasyEventsHelper;

import java.time.LocalTime;
import java.util.Objects;

/**
 * this class serves as a single Event storing a timestamp and description
 * this class is coupled with EventLog
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class Event implements Comparable<Event> {
    //region FIELDS
    private LocalTime timeStamp; // time stamp in LocalTime format hh:mm:ss
    private String description; // note or event description
    private int id; // the sequence of events ordered by time stamp and description; id is assigned before output
    // endregion

    //region CONSTRUCTOR
    Event() {
        this.setEventTimeStamp("00:00:00");
        this.setDescription("");
        this.id = 0; // only default event id = 0;
    }

    Event(String timeStamp) {
        this();
        this.setEventTimeStamp(timeStamp);
    }

    Event(String timeStamp, String description) {
        this(timeStamp);
        this.setDescription(description);
    }

    Event(LocalTime timeStamp, String description) {
        this.setEventTimeStamp(timeStamp);
        this.setDescription(description);
    }
    //endregion

    //region ASSESSOR METHODS
    LocalTime getEventTimeStamp() {
        return timeStamp;
    }

    /**
     * convert LocalTime to "HH:mm:ss" format
     * e.g. from 11:00 to "00:11:00"
     *
     * @return "HH:mm:ss" formatted time
     */
    String getFormattedEventTimeStamp() {
        return EasyEventsHelper.localtimeToFormattedString(this.getEventTimeStamp());
    }

    /**
     * assign a LocalTime object to this Event
     *
     * @param timeStamp LocalTime object cannot be null
     */
    private void setEventTimeStamp(LocalTime timeStamp) {
        if (Objects.isNull(timeStamp)) {
            throw new EventConstructorInvalidInputException("Input timestamp is NULL");
        } else {
            this.timeStamp = timeStamp;
        }
    }

    /**
     * convert input String localTime format to "HH:mm:ss" then assign its value
     *
     * @param timeStamp not null or invalid format
     *                  e.g. "46:23:12" hour limit is 0 - 24
     */
    private void setEventTimeStamp(String timeStamp) {
        if (Objects.isNull(timeStamp)) {
            throw new EventConstructorInvalidInputException("Input timestamp is NULL");
        } else {
            this.timeStamp = EasyEventsHelper.localTimeFromString(timeStamp);
        }
    }

    String getDescription() {
        return this.description;
    }

    /**
     * assign description
     *
     * @param description string and can not be null
     */
    private void setDescription(String description) {
        if (Objects.isNull(description)) {
            throw new EventConstructorInvalidInputException("Input description is NULL");
        } else {
            this.description = description;
        }
    }

    int getId() {
        return id;
    }

    /**
     * valid id range integer from 1 to Integer.MAX; default event id = 0;
     *
     * @param id represent the sequence of events ranked by time stamp
     */
    void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("setId(): invalid input id; id is less than 0.");
        } else {
            this.id = id;
        }
    }
    //endregion

    //region OVERRIDES
    @Override
    public String toString() {
        return String.format("Event ID: %d; Event time stamp: %s; Event description: %s.", this.getId(), this.getFormattedEventTimeStamp(), this.getDescription());
    }

    @Override
    public boolean equals(Object obj) {
        if (!Objects.isNull(obj) && obj instanceof Event) {
            return ((Event) obj).getFormattedEventTimeStamp().equals(this.getFormattedEventTimeStamp())
                    && ((Event) obj).getDescription().equals(this.getDescription());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getFormattedEventTimeStamp() + this.getDescription()).hashCode();
    }

    @Override
    public int compareTo(Event other) {
        int result = 0;
        if (!Objects.isNull(other)) {
            // compare time stamps first then compare description
            result = this.getFormattedEventTimeStamp().compareTo(other.getFormattedEventTimeStamp());
            if (result == 0) {
                // if time stamps are same, then compare description
                result = this.getDescription().compareTo(other.getDescription());
            }
        }
        return result;
    }
    //endregion

}