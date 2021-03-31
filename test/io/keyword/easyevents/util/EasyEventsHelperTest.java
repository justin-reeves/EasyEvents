package io.keyword.easyevents.util;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;

public class EasyEventsHelperTest {

    @Test (expected = DateTimeParseException.class)
    public void localTimeFromString_throwDateTimeParseException_notHhMmSsFormat() {
        System.out.println(LocalTime.parse("0:0:18"));
    }

    @Test
    public void localTimeFromString_convertTimeFormat() {
        LocalTime formattedTime = EasyEventsHelper.localTimeFromString("0:0:18");
        LocalTime time = LocalTime.of(0, 0, 18);
        assertEquals(time, formattedTime);
    }

    @Test(expected = PatternSyntaxException.class)
    public void localTimeFromString_throwPatternSyntaxException_splitMore() {
        EasyEventsHelper.localTimeFromString("0:0:0:0");
    }

    @Test(expected = NumberFormatException.class)
    public void localTimeFromString_throwPatternSyntaxException_empty() {
        EasyEventsHelper.localTimeFromString("");
    }

    @Test(expected = DateTimeException.class)
    public void localTimeFromString_throwPatternSyntaxException_moreThan24Hour() {
        EasyEventsHelper.localTimeFromString("36:0:0");
    }

    @Test
    public void localtimeToFormattedString_convertHmsToHhMmSs() {
        LocalTime time = LocalTime.of(3,2);
        System.out.println(time);
        assertEquals(EasyEventsHelper.localtimeToFormattedString(time), "03:02:00");
    }
}