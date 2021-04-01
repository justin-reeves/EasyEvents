package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;

public class SessionWriterTest {
    private EventLog log;

    @Before
    public void setUp() {
        log = EventLog.getInstance();
        log.addEventNoOffset(LocalTime.parse("11:00:00"), "e4");
        log.addEventNoOffset(LocalTime.parse("10:00:00"), "e3");
        log.addEventNoOffset(LocalTime.parse("01:00:00"), "e1");
        log.addEventNoOffset(LocalTime.parse("15:00:00"), "e6");
        log.addEventNoOffset(LocalTime.parse("13:00:00"), "e5");
        log.addEventNoOffset(LocalTime.parse("09:00:00"), "e2");
    }

    @Test
    public void random() throws IOException {
        // only txt file now
        SessionWriter.writeFile("name", SessionWriter.FileType.TXT, log.getAllEvents());
        //SessionWriter.writeFile("name", SessionWriter.FileType.CSV, log.getAllEvents());

    }

    @Test
    public void writeFile_generateDifferentFile_whenFileExist() {
        //SessionWriter.writeFile("name", EasyEventsHelper.FileType.TEXT, log.getAllEvents());
    }

}