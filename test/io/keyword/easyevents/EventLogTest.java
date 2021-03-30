package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class EventLogTest {
    private EventLog log;

    @Before
    public void setUp() {
        log = EventLog.getInstance();
    }

    @Test
    public void addEvent_theEventsSortedAscendingOrder(){
        log.addEvent("11:00:00","e4");
        log.addEvent("10:00:00", "e3");
        log.addEvent("01:00:00","e1");
        log.addEvent("15:00:00", "e6");
        log.addEvent("13:00:00","e5");
        log.addEvent("09:00:00", "e2");
        log.dumpEvents();
        int index = 1;
        for(Event e : log.getAllEvents()){
            assertEquals(e.getDescription(), "e"+(index++));
        }
    }

    @Test(expected = DateTimeParseException.class)
    public void addEvent_invalidTimestampFormat_throwsException(){
        // correct input time format "01:01:01"
        log.addEvent("1:1:1","description");
    }

    @Test(expected = DateTimeParseException.class)
    public void addEvent_invalidDescription_throwsException(){
        // correct input time format "01:01:01"
        log.addEvent("01:01:01",null);
    }


    @Test
    public void createNewEventsOnly_multipleEvents_noChangeEvenIdUntilEventIsAdded(){
//        Event event = new Event();
//        assertTrue(event.getDescription().equals("") && event.getId() == 0);
    }
}