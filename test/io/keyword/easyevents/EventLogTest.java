package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;


import static org.junit.Assert.*;

public class EventLogTest {
    private EventLog log;

    @Before
    public void setUp() {
        log = EventLog.getInstance();
    }

    @Test
    public void addEvent_theEventsSortedAscendingOrder(){
        loadEvents();
        log.dumpEvents();
        int index = 1;
        for(Event e : log.getAllEvents()){
            assertEquals(e.getDescription(), "e"+(index++));
        }
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void addEvent_invalidTimestampFormat_throwsException(){
        // correct input time format "01:01:01"
        log.addEvent("1:1:1","description");
    }

    @Test(expected = EventConstructorInvalidInputException.class)
    public void addEvent_invalidDescription_throwsException(){
        // correct input time format "01:01:01"
        log.addEvent("01:01:01",null);
    }

    @Test
    public void start_initialEventIsAdded(){
        log.start("11:11:11");
        assertTrue(log.getInitialTime().toString().equals("11:11:11"));
        assertTrue(log.getFirstEvent().getDescription().equals("Initial Event - Session starts;"));
        assertTrue(log.getAllEvents().size() == 1);
    }

    @Test
    public void end_lastEventIsAdded(){
        log.end("17:00:00");
        assertTrue(log.getLastEvent().getFormattedEventTimeStamp().toString().equals("17:00:00"));
        assertTrue(log.getLastEvent().getDescription().equals("Last Event - Session ends;"));
    }

    @Test
    public void search_returnDesiredList(){
        loadEvents();
        List<Event> list = log.searchEvent("e");
        assertTrue(list.size() == 6);

        list = log.searchEvent("3");
        assertTrue(list.size() == 1);

        list = log.searchEvent("not");
        assertTrue(list.size() == 0);

        list = log.searchEvent(null);
        assertTrue(list.size() == 0);
    }

    @Test
    public void search_returnDesiredEvent(){
        loadEvents();
        Event event = log.searchEvent(1);
        assertTrue(event.getDescription().equals("e1"));
        event = log.searchEvent(6);
        assertTrue(event.getDescription().equals("e6"));

        event = log.searchEvent(-1); // no -1 id exist
        assertTrue(Objects.isNull(event));
    }

    @Test
    public void delete_returnDesiredSize(){
        loadEvents();
        //log.dumpEvents();
        log.deleteEvent(1);
        assertTrue(log.getAllEvents().size() == 5);
        Event event = log.searchEvent(1);
        assertFalse(Objects.isNull(event)); // assigned event id again, so id 1 still there
        List<Event> list = log.searchEvent("e1"); // e1 is gone
        assertTrue(list.size() == 0);
        //log.dumpEvents();
    }

    private void loadEvents(){
        log.addEvent("11:00:00","e4");
        log.addEvent("10:00:00", "e3");
        log.addEvent("01:00:00","e1");
        log.addEvent("15:00:00", "e6");
        log.addEvent("13:00:00","e5");
        log.addEvent("09:00:00", "e2");
    }
}