package io.keyword.easyevents.util;

import org.junit.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/*
 * Credit to: https://stackoverflow.com/a/1119559
 * For information on redirecting System.out for testing
 */
public class EasyEventsIOTest {
    private static File outputContent;
    private static final PrintStream originalOut = System.out;

    @BeforeClass
    public static void beforeClass() {
        outputContent = new File("resources/test/sysOutActual.txt");

        try {
            outputContent.createNewFile();
            System.setOut(new PrintStream(new FileOutputStream(outputContent)));
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    @AfterClass
    public static void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    // Doing it this way to fix formatting and also verify by sight that it looks good.
    public void NOT_TEST_displayIntro() {
        EasyEventsIO.displayIntro();
    }

    @Test
    public void getUserInput() {

    }

    @Test
    public void displayEnd_ShouldDisplayArgsInCorrectOrder() {
        EasyEventsIO.displayEnd("15:30:00", 15, "DISPLAYTEST.txt");
        List<String> lines = readTestingFile();

        assertEquals(lines.get(0), "Session event logging ended at 15:30:00.");
        assertEquals(lines.get(1), "Logged 15 events to file 'DISPLAYTEST.txt'");
    }

    @Test
    public void noArgDisplayUsage_ShouldPrintAllCommandsWithDefinitions() {
        EasyEventsIO.displayUsage();
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("start -n [-n session name] -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("event -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("end"));
        assertTrue(lines.contains("help [command]"));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintAllCommandsWithDefinitions_WhenStringDoesNotContainASpecificCommand() {
        EasyEventsIO.displayUsage("help");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("start -n [-n session name] -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("event -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("end"));
        assertTrue(lines.contains("help [command]"));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintAllCommandsWithDefinitions_WhenStringContainsANonExistentCommand() {
        EasyEventsIO.displayUsage("help NOT_A_COMMAND");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("start -n [-n session name] -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("event -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("end"));
        assertTrue(lines.contains("help [command]"));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintStartCommandWithDefinition_WhenStringContainsStartCommand() {
        EasyEventsIO.displayUsage("help start");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("start -n [-n session name] -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("\t-n:\tA [session name] is required when using this flag."));
        assertTrue(lines.contains("\t-t:\tA [time: hh:mm:ss] is required when using this flag."));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintEventCommandWithDefinition_WhenStringContainsEventCommand() {
        EasyEventsIO.displayUsage("help event");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("event -t [time: hh:mm:ss]"));
        assertTrue(lines.contains("\t-t:\tA [time: hh:mm:ss] is required when using this flag."));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintEndCommandWithDefinition_WhenStringContainsEndCommand() {
        EasyEventsIO.displayUsage("help end");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("end"));
    }

    @Test
    public void oneArgDisplayUsage_ShouldPrintHelpCommandWithDefinition_WhenStringContainsHelpCommand() {
        EasyEventsIO.displayUsage("help help");
        List<String> lines = readTestingFile();

        assertNotNull(lines);
        assertTrue(lines.contains("help [command]"));
        assertTrue(lines.contains("\t[command]:\tOptional argument that designates a specific command to display " +
                "usage information for."));
    }

    private List<String> readTestingFile() {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(outputContent.toPath(), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        return lines;
    }
}