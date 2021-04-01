package io.keyword.easyevents;


import io.keyword.easyevents.util.EasyEventsHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class EventLogTest {
    private EventLog log;

    @Before
    public void setUp() {
        log = EventLog.getInstance();
    }

    @After
    public void clean() {
        log.clearEvents();
    }

    @Test
    public void addEvent_theEventsSortedAscendingOrder() {
        loadEvents();
        //log.dumpEvents();
        int index = 1;
        for (Event e : log.getAllEvents()) {
            assertEquals(e.getDescription(), "e" + (index++));
        }
    }

    @Test(expected = DateTimeParseException.class)
    public void addEvent_invalidTimestampFormat_throwsException() {
        // correct input time format "01:01:01"
        log.addEventNoOffset(LocalTime.parse("1:1:1"), "description");
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void addEvent_invalidDescription_throwsException() {
        // correct input time format "01:01:01"
        log.addEventNoOffset(LocalTime.parse("01:01:01"), null);
    }

    @Test
    public void start_initialEventIsAddedManually() {
        log.start("11:11:11");
        assertEquals("11:11:11", log.getInitialTime().toString());
        assertEquals("Initial Event - Session starts;", log.getFirstEvent().getDescription());
        assertEquals(1, log.getAllEvents().size());
    }

    @Test
    public void end_lastEventIsAdded() {
        loadEvents();
        log.end("17:00:00");
        assertEquals("17:00:00", log.getLastEvent().getFormattedEventTimeStamp());
        assertEquals("Last Event - Session ends;", log.getLastEvent().getDescription());
    }

    @Test
    public void search_returnDesiredList() {
        loadEvents();
        List<Event> list = log.searchEvent("e");
        assertEquals(6, list.size());

        list = log.searchEvent("3");
        assertEquals(1, list.size());

        list = log.searchEvent("not");
        assertEquals(0, list.size());

        list = log.searchEvent(null);
        assertEquals(0, list.size());
    }

    @Test
    public void search_returnDesiredEvent() {
        loadEvents();
        Event event = log.searchEvent(1);
        assertEquals("e1", event.getDescription());
        event = log.searchEvent(6);
        assertEquals("e6", event.getDescription());

        event = log.searchEvent(-1); // no -1 id exist
        assertTrue(Objects.isNull(event));
    }

    @Test
    public void delete_returnDesiredSize_deletedIdStillThereButDeletedEventGone() {
        loadEvents();
        log.deleteEvent(2);
        assertEquals(5, log.getAllEvents().size());
        Event event = log.searchEvent(1);
        assertFalse(Objects.isNull(event)); // assigned event id again, so id 1 still there
        List<Event> list = log.searchEvent("e2"); // e2 is gone
        assertEquals(0, list.size());
    }

    @Test
    public void offsetEventWithStart_givenInitialTime_calculateTimeElapsed() {
        log.start("08:08:08");
        loadEvents_testInsertOffset();
        // e0 is inserted with current local time
        // so e0's time elapsed = LocalTime.now() - "08:08:08"
        Event[] events = new Event[1]; // store e0
        log.searchEvent("e0").toArray(events);
        // calculate time elapsed
        Duration duration = Duration.between(LocalTime.parse("08:08:08"), LocalTime.now());
        String timeElapsed = String.format("%d:%d:%d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        assertEquals(EasyEventsHelper.localTimeFromString(timeElapsed), events[0].getEventTimeStamp());
        //log.dumpEvents();
    }

    private void loadEvents_testInsertOffset() {
        log.addEventNoOffset(LocalTime.parse("11:00:00"), "e4");
        log.addEventNoOffset(LocalTime.parse("10:00:00"), "e3");
        log.addEventNoOffset(LocalTime.parse("01:00:00"), "e1");
        log.addEventNoOffset(LocalTime.parse("15:00:00"), "e6");
        log.addEventNoOffset(LocalTime.parse("13:00:00"), "e5");
        log.addEventNoOffset(LocalTime.parse("09:00:00"), "e2");
        log.addEventOffset(LocalTime.now(), "e0");
    }

    private void loadEvents() {
        log.addEventNoOffset(LocalTime.parse("11:00:00"), "e4");
        log.addEventNoOffset(LocalTime.parse("10:00:00"), "e3");
        log.addEventNoOffset(LocalTime.parse("01:00:00"), "e1");
        log.addEventNoOffset(LocalTime.parse("15:00:00"), "e6");
        log.addEventNoOffset(LocalTime.parse("13:00:00"), "e5");
        log.addEventNoOffset(LocalTime.parse("09:00:00"), "e2");
    }
}