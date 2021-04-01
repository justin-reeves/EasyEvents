package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventTest {
    private final String TIMESTAMP_HHMMSS = "13:10:10";
    private final String INVALID_TIMESTAMP_HH = "13";
    private Event event;


    @Before
    public void setUp() {
    }

    @Test
    public void createNewEvent_defaultEvent_emptyStringAndIdZero() {
        event = new Event();
        assertTrue(event.getDescription().equals("") && event.getId() == 0);
    }

    @Test
    public void createNewEvent_onlyInputTimestamp() throws EventConstructorInvalidInputException {
        event = new Event(TIMESTAMP_HHMMSS);
        assertTrue(event.getEventTimeStamp().toString().equals(TIMESTAMP_HHMMSS));
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void createNewEvent_invalidInputTimestamp_throwException() throws EventConstructorInvalidInputException {
        event = new Event(INVALID_TIMESTAMP_HH);
        assertTrue(event.getEventTimeStamp().toString().equals(INVALID_TIMESTAMP_HH));
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void createNewEvent_nullInputTimestamp_throwException() throws EventConstructorInvalidInputException {
        event = new Event(null);
        assertTrue(event.getEventTimeStamp().toString().isEmpty());
    }

    @Test
    public void setId_idBiggerAndEqualToZero() {
        event = new Event();
        event.setId(0);
        assertTrue(event.getId() == 0);
        event.setId(Integer.MAX_VALUE);
        assertTrue(event.getId() == Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setId_idBiggerAndEqualToZero_ifNotThrowException() {
        event = new Event();
        event.setId(-1);
        assertTrue(event.getId() == -1);
    }

    @Test
    public void equals_passWhenTwoEventsHaveSameTimestampAndDescription() {
        event = new Event();
        Event other = new Event();
        assertTrue(event.equals(other));

        event = new Event("01:01:01", "event");
        other = new Event("01:01:01", "event");
        assertTrue(event.equals(other));
    }

    @Test
    public void equals_passFalseWhenTwoEventsNotEqual() {
        event = new Event();
        Event other = new Event();
        event.setEventTimeStamp("01:01:01");
        other.setEventTimeStamp("01:01:00");
        assertFalse(event.equals(other)); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertFalse(event.equals(other)); // description different
    }

    @Test
    public void hashCode_passWhenTwoEventsHaveSameTimestampAndDescription() {
        event = new Event();
        Event other = new Event();
        assertTrue(event.hashCode() == other.hashCode());

        event = new Event("01:01:01", "event");
        other = new Event("01:01:01", "event");
        assertTrue(event.hashCode() == other.hashCode());
    }

    @Test
    public void hashCode_passFalseWhenTwoEventsNotEqual() {
        event = new Event();
        Event other = new Event();
        event.setEventTimeStamp("01:01:01");
        other.setEventTimeStamp("01:01:00");
        assertFalse(event.hashCode() == other.hashCode()); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertFalse(event.hashCode() == other.hashCode()); // description different
    }

    @Test
    public void compareTo_compareTimestampThenDescription() {
        event = new Event();
        Event other = new Event();
        assertTrue(event.compareTo(other) == 0); // timestamp different

        event.setEventTimeStamp("01:01:01");
        other.setEventTimeStamp("01:01:00");
        assertTrue(event.compareTo(other) > 0); // timestamp different

        event.setEventTimeStamp("01:01:00");
        other.setEventTimeStamp("01:02:00");
        assertTrue(event.compareTo(other) < 0); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertTrue(event.compareTo(other) < 0); // timestamp different

        event = new Event("01:01:01", "d");
        other = new Event("01:01:01", "c");
        assertTrue(event.compareTo(other) > 0); // timestamp different
    }
}