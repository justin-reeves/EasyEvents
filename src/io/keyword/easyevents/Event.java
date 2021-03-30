package io.keyword.easyevents;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * this class serves as a single Event storing a timestamp and description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class Event implements Comparable<Event> {
    // FIELDS
    private Instant timeStamp; // time stamp in second recording the duration from first event
    private String description; // note or event description
    private int id; // store event id; each session's first event id=1

    //region CONSTRUCTOR
    Event() {
        this.setEventTimeStamp(Instant.now());
        this.setDescription("");
        this.id = 0; // only default event id = 0;
    }

    Event(String timeStamp) {
        this();
        try {
            // convert string to Instant time stamp
            this.setEventTimeStamp(timeStamp);
        } catch (NullPointerException e) {
            throw new EventConstructorInvalidInputException("Input timestamp is NULL.", e);
        }
    }

    Event(Instant timeStamp, String description) {
        try {
            this.setEventTimeStamp(timeStamp);
            this.setDescription(description);
        } catch (DateTimeParseException de) {
            throw new EventConstructorInvalidInputException("Invalid input timestamp format hh:mm:ss", de);
        } catch (NullPointerException ne) {
            throw new EventConstructorInvalidInputException("Input description is NULL.", ne);
        }
    }

    Event(String timeStamp, String description) {
        this(timeStamp);
        this.setDescription(description);
    }
    //endregion

    //region ASSESSOR METHODS
    Instant getEventTimeStamp() {
        return timeStamp;
    }

    void setEventTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    void setEventTimeStamp(String timeStamp) {
        this.setEventTimeStamp(this.instantFromString(timeStamp));
    }

    String getDescription() {
        return this.description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    int getId() {
        return id;
    }

    // default event id = 0; valid id range >= 1
    void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("setId(): invalid input id");
        } else {
            this.id = id;
        }
    }
    //endregion


    // HELPER METHODS

    /**
     * convert user input string to Instant
     *
     * @param timeStamp input String "hh:mm:ss"
     * @return Instant of current local date + input time
     */
    Instant instantFromString(String timeStamp) {
        return LocalTime.parse(timeStamp, DateTimeFormatter.ISO_TIME).atDate(LocalDate.now()).toInstant(ZoneOffset.UTC);
    }

    // OVERRIDES
    @Override
    public String toString() {
        return String.format("Event ID: %d; Event time stamp: %s; Event description: %s.", this.getId(), this.getEventTimeStamp(), this.getDescription());
    }

    @Override
    public boolean equals(Object obj) {
        if (!Objects.isNull(obj) && obj instanceof Event) {
            if (((Event) obj).getEventTimeStamp().equals(this.getEventTimeStamp())
                    && ((Event) obj).getDescription().equals(this.getDescription())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getEventTimeStamp().toString() + this.getDescription()).hashCode();
    }

    @Override
    public int compareTo(Event other) {
        int result = 0;
        if (!Objects.isNull(other)) {
            // compare time stamps first
            result = this.getEventTimeStamp().compareTo(other.getEventTimeStamp());
            if (result == 0) {
                // if time stamps are same, then compare description
                result = this.getDescription().compareTo(other.getDescription());
            }
        }
        return result;
    }

    class EventConstructorInvalidInputException extends RuntimeException {

        public EventConstructorInvalidInputException() {
        }

        public EventConstructorInvalidInputException(String message) {
            super(message);
        }

        public EventConstructorInvalidInputException(String message, Throwable cause) {
            super(message, cause);
        }

        public EventConstructorInvalidInputException(Throwable cause) {
            super(cause);
        }

        public EventConstructorInvalidInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}