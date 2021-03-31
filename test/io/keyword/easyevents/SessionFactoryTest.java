package io.keyword.easyevents;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class SessionFactoryTest {

    @Test(expected = EventConstructorInvalidInputException.class)
    public void getSession_shouldThrow_EventConstructorInvalidInputException_whenStartTimeFlagArgumentPastCurrentTime() {
        String startCommandWithTimeInPast = "start -t " + LocalTime.now().plusMinutes(1L);
        SessionFactory.getSession(startCommandWithTimeInPast);
    }

    @Test
    public void getSession_shouldReturnSessionWithDefaultName_whenNoFlagsPassed() {
    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenOnlyUsingNameFlag() {

    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenNameFlagComesFirst() {

    }

    @Test
    public void getSession_shouldReturnSessionWithNameEqualToNameFlagArgument_whenNameFlagComesLast() {

    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenOnlyUsingTimeFlag() {

    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenTimeFlagComesFirst() {

    }

    @Test
    public void getSession_shouldReturnSessionWithInitialTimeEqualToTimeFlagArgument_whenTimeFlagComesLast() {

    }

}