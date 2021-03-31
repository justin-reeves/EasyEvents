package io.keyword.easyevents.util;

import io.keyword.easyevents.EventConstructorInvalidInputException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EasyEventsHelper {


    /**
     * convert user input string to Instant
     *
     * @param timeStamp input String "hh:mm:ss"
     * @return Instant of current local date + input time
     */
    public static LocalTime localTimeFromString(String timeStamp) {
        try {
            return LocalTime.parse(timeStamp);
        } catch (DateTimeParseException de){
            throw new EventConstructorInvalidInputException("Invalid input timestamp:" + timeStamp, de);
        }
    }

    /**
     * convert string to an Instant object
     * @param timeStamp
     * @return
     */
    public static Instant instantFromString(String timeStamp) {
        return LocalTime.parse(timeStamp, DateTimeFormatter.ISO_TIME).atDate(LocalDate.now()).toInstant(ZoneOffset.UTC);
    }

    /**
     * convert LocalTime to formatted string "hh:mm:ss"
     * e.g. 1:1:1 to "01:01:01" or 07:00 to "07:00:00"
     * @param time
     * @return
     */
    public static String localtimeToFormattedString(LocalTime time){
        return time.format(DateTimeFormatter.ISO_TIME);
    }

}
