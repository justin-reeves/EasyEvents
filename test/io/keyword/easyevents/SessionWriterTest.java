package io.keyword.easyevents;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class SessionWriterTest {
    private final EventLog log = EventLog.getInstance();


    @Before
    public void setUp() {
    }

    @Test
    public void writeFile_emptyFileName_generateDefaultName() throws IOException, InterruptedException {
        loadEvents();
        SessionWriter.writeFile("", SessionWriter.FileType.TXT, log.getAllEvents());
        Path path = Paths.get(System.getProperty("user.dir"), "Session_" + LocalDate.now().toString() + ".txt");

        Thread.sleep(1000); // give writer time to generate file
        assertTrue(Files.exists(path));
    }

    @Test
    public void writeFile_ullFileName_generateDefaultName() throws IOException, InterruptedException {
        loadEvents();
        SessionWriter.writeFile(null, SessionWriter.FileType.TXT, log.getAllEvents());
        Path path = Paths.get(System.getProperty("user.dir"), "Session_" + LocalDate.now().toString() + ".txt");

        Thread.sleep(1000); // give writer time to generate file
        assertTrue(Files.exists(path));
    }

    @Test
    public void writeFile_generateDifferentFile_whenFileExist() throws IOException, InterruptedException {
        loadEvents();
        Path path1 = SessionWriter.writeFile("Session", SessionWriter.FileType.TXT, log.getAllEvents());
        Path path2 = SessionWriter.writeFile("Session", SessionWriter.FileType.TXT, log.getAllEvents());

        Thread.sleep(1000); // give writer time to generate file
        assertNotEquals(path1, path2); // given same file name but generate two different files
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