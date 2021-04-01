package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class EventTest {
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
        String TIMESTAMP_HHMMSS = "13:10:10";
        event = new Event(TIMESTAMP_HHMMSS);
        assertEquals(event.getEventTimeStamp().toString(), TIMESTAMP_HHMMSS);
    }

    @Test(expected = PatternSyntaxException.class)
    public void createNewEvent_invalidInputTimestamp_throwException() throws EventConstructorInvalidInputException {
        String INVALID_TIMESTAMP_HH = "13:1:1:1";
        event = new Event(INVALID_TIMESTAMP_HH);
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void createNewEvent_nullTimestamp_throwException() throws EventConstructorInvalidInputException {
        event = new Event(null);
        assertTrue(event.getEventTimeStamp().toString().isEmpty());
    }

    @Test
    public void setId_idBiggerAndEqualToZero() {
        event = new Event();
        event.setId(0);
        assertEquals(0, event.getId());
        event.setId(Integer.MAX_VALUE);
        assertEquals(event.getId(), Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setId_ifIdNotBiggerAndEqualToZero_throwException() {
        event = new Event();
        event.setId(-1);
        assertEquals(event.getId(), -1);
    }

    @Test
    public void equals_pass_WhenTwoEventsHaveSameTimestampAndDescription() {
        event = new Event();
        Event other = new Event();
        assertEquals(event, other);

        event = new Event("01:01:01", "event");
        other = new Event("01:01:01", "event");
        assertEquals(event, other);
    }

    @Test
    public void equals_passFalseWhenTwoEventsNotEqual() {
        event = new Event("01:01:01");
        Event other = new Event("01:01:00");
        assertNotEquals(event, other); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertNotEquals(event, other); // description different
    }

    @Test
    public void hashCode_passWhenTwoEventsHaveSameTimestampAndDescription() {
        event = new Event();
        Event other = new Event();
        assertEquals(event.hashCode(), other.hashCode());

        event = new Event("01:01:01", "event");
        other = new Event("01:01:01", "event");
        assertEquals(event.hashCode(), other.hashCode());
    }

    @Test
    public void hashCode_passFalseWhenTwoEventsNotEqual() {
        event = new Event("01:01:01");
        Event other = new Event("01:01:00");
        assertNotEquals(event.hashCode(), other.hashCode()); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertNotEquals(event.hashCode(), other.hashCode()); // description different
    }

    @Test
    public void compareTo_compareTimestampThenDescription(){
        event = new Event("01:01:00");
        Event other = new Event("01:01:00");
        assertEquals(0, event.compareTo(other)); // timestamp different

        event = new Event("01:03:00");
        other = new Event("01:02:00");
        assertTrue(event.compareTo(other) > 0); // timestamp different

        event = new Event("01:01:00");
        other = new Event("01:02:00");
        assertTrue(event.compareTo(other) < 0); // timestamp different

        event = new Event("01:01:01", "event1");
        other = new Event("01:01:01", "event2");
        assertTrue(event.compareTo(other) < 0); // timestamp different

        event = new Event("01:01:01", "d");
        other = new Event("01:01:01", "c");
        assertTrue(event.compareTo(other) > 0); // timestamp different
    }
}