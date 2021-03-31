package io.keyword.easyevents;

import i.keyword.easyevents.util.EasyEventsHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class SessionWriterTest {
    private SessionWriter writer = SessionWriter.getWriter();
    private EventLog log = EventLog.getInstance();


    @Before
    public void setUp() {
    }

    @Test
    public void random() throws IOException {
        loadEvents();
        // only txt file now
        SessionWriter.writeFile("name", SessionWriter.FileType.TXT, log.getAllEvents());
        //SessionWriter.writeFile("name", SessionWriter.FileType.CSV, log.getAllEvents());

    }

    @Test
    public void writeFile_generateDifferentFile_whenFileExist() {
        loadEvents();
        //SessionWriter.writeFile("name", EasyEventsHelper.FileType.TEXT, log.getAllEvents());
    }

    private void loadEvents(){
        log.addEventNoOffset(LocalTime.parse("11:00:00"),"e4");
        log.addEventNoOffset(LocalTime.parse("10:00:00"), "e3");
        log.addEventNoOffset(LocalTime.parse("01:00:00"),"e1");
        log.addEventNoOffset(LocalTime.parse("15:00:00"), "e6");
        log.addEventNoOffset(LocalTime.parse("13:00:00"),"e5");
        log.addEventNoOffset(LocalTime.parse("09:00:00"), "e2");
    }

}