package io.keyword.easyevents.util;

import io.keyword.easyevents.EventConstructorInvalidInputException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

public class EasyEventsHelper {

    private EasyEventsHelper(){}
    /**
     * convert user input string to Instant
     *
     * @param timeStamp input String "hh:mm:ss"
     * @return Instant of current local date + input time
     */
    public static LocalTime localTimeFromString(String timeStamp) {
        try {
            if(Objects.isNull(timeStamp)){
                throw new IllegalArgumentException("Null input timestamp: " + timeStamp);
            }
            String[] timeParts = timeStamp.split(":");
            switch(timeParts.length){
                case 3:
                    return LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
                case 2:
                    return LocalTime.of(0, Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
                case 1:
                    return LocalTime.of(0, 0, Integer.parseInt(timeParts[0]));
                default:
                    throw new PatternSyntaxException("Invalid format timestamp: " + timeStamp, "HH:MM:SS", -1);
            }
        } catch (PatternSyntaxException e){
            throw new PatternSyntaxException("Invalid format timestamp:" + timeStamp, "HH:MM:SS", -1);
        } catch (DateTimeException de){
            throw new DateTimeException("Hour, minute or second out of range");
        } catch (NumberFormatException ne){
            throw new NumberFormatException("Cannot parse empty String to Integer");
        }
    }

    /**
     * convert LocalTime to formatted string "hh:mm:ss"
     * e.g. 1:1:1 to "01:01:01" or 07:00 to "07:00:00"
     * @param time
     * @return
     */
    public static String localtimeToFormattedString(LocalTime time){
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
