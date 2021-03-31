package io.keyword.easyevents;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SessionFactoryTest {

    @Test(expected = EventConstructorInvalidInputException.class)
    public void getSession_shouldThrow_EventConstructorInvalidInputException_whenStartTimeFlagArgumentPastCurrentTime() {
        String startCommandWithTimeInPast = "start -t " + LocalTime.now().plusMinutes(1L);
        SessionFactory.getSession(startCommandWithTimeInPast);
    }

    @Test
    public void getSession_shouldReturnSessionWithDefaultName_whenNoFlagsPassed() {
        assertTrue(SessionFactory.getSession("start").getSessionName().equals("EasyEvents_" + LocalDate.now().toString()));
    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenOnlyUsingNameFlag() {
        assertTrue(SessionFactory.getSession("start -n myEvent").getSessionName().equals("myEvent"));

    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenNameFlagComesFirst() {
        assertTrue(SessionFactory.getSession("start -n myEvent -t 12:00:00").getSessionName().equals("myEvent"));

    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenNameFlagComesLast() {
        assertTrue(SessionFactory.getSession("start -t 12:00:00 -n myEvent").getSessionName().equals("myEvent"));

    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenOnlyUsingTimeFlag() {
        String timeArg = "12:00:00";
        Session sessionWithOnlyTimeFlag = SessionFactory.getSession("start -t " + timeArg);
        LocalTime actual = sessionWithOnlyTimeFlag.getInitialTime();
        LocalTime expected = LocalTime.parse(timeArg);

        assertEquals(expected, actual);
    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenTimeFlagComesFirst() {
        String timeArg = "12:00:00";
        Session sessionWhenTimeFlagComesFirst = SessionFactory.getSession("start -t " + timeArg + " -n myEvent");
        LocalTime actual = sessionWhenTimeFlagComesFirst.getInitialTime();
        LocalTime expected = LocalTime.parse(timeArg);

        assertEquals(expected, actual);

    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenTimeFlagComesLast() {
        String timeArg = "12:00:00";
        Session sessionWhenTimeFlagComesLast = SessionFactory.getSession("start -n myEvent -t " + timeArg);
        LocalTime actual = sessionWhenTimeFlagComesLast.getInitialTime();
        LocalTime expected = LocalTime.parse(timeArg);

        assertEquals(expected, actual);

    }

}