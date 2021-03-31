package io.keyword.easyevents.util;

import com.apps.util.Prompter;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Singleton Static Utility class that provides the IO functionality of EasyEvents
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/29/2021
 */
public class EasyEventsIO {

    // FIELDS
    private static Prompter prompter;

    // CONSTRUCTORS

    private EasyEventsIO() {
        // All static utility class. Prevent instantiation.
        prompter = new Prompter(new Scanner(System.in));
    }

    // BUSINESS METHODS

    /**
     * Uses a <code>Prompter</code> to display desired message
     *
     * @param msg The string to display to the client
     */
    public static void info(String msg) {
        prompter.info(msg);
    }

    /**
     * Uses a <code>Prompter</code> to wait for user input.
     *
     * @return Returns the user's input String
     */
    public static String prompt(String msg) {
        return prompter.prompt(msg);
    }

    /**
     * Uses a <code>Prompter</code> to wait for user input. User input must match provided 'pattern', or else
     * input is rejected and the user is presented a designated retry message and re-prompted.
     *
     * @param msg      The prompt message to display to the user
     * @param pattern  The regex pattern to check user input against
     * @param retryMsg The retry message presented to user when input does not match 'pattern'
     * @return Returns the user's input String which matches the provided 'pattern'
     */
    public static String prompt(String msg, String pattern, String retryMsg) {
        return prompter.prompt(msg, pattern, retryMsg);
    }

    /**
     * Convenience method which waits for the initial EasyEvents 'start' command.
     *
     * @return Returns the String containing the 'start' command and any flags used
     */
    public static String promptStart() {
        String result = "";
        String nameCommandRegex = "-n ((\\\"[\\w\\- ]+\\\")|[\\w\\-]+)";
        String timeCommandRegex = "-t ([01][\\d]|2[0-3]):[0-5][\\d]:[0-5][\\d]";

        while (!result.startsWith("start")) {
            result = prompt(
                    "Use 'start' command to begin event logging, or type 'help' for usage\n",
                    "^start *$|" +
                            "^start +" + nameCommandRegex + " *$|" +
                            "^start +" + timeCommandRegex + " *$|" +
                            "^start +" + nameCommandRegex + " +" + timeCommandRegex + " *$|" +
                            "^start +" + timeCommandRegex + " +" + nameCommandRegex + " *$|" +
                            "help *.*",
                    "Please type 'start' or 'help' with or without their respective OPTIONAL tags. See 'help' for details\n");
            if (result.startsWith("help")) {
                displayUsage(result);
            }
        }

        return result;
    }

    /**
     * Displays, to the console, an Easy Events introduction statement
     * consisting of helpful commands and example usage.
     */
    public static void displayIntro() {
        System.out.println(
                "Welcome to Easy Events!\n" +
                        "-------------------------------------------------------------------------------------------------------\n" +
                        "Type ‘help [command]’ at any time for usage statements.\n" +
                        "\nType ‘start –n [Log name] -t [time: hh:mm:ss]’ to begin event logging.\n" +
                        "\nDuring logging:\n" +
                        "\tCreate an event description by typing:\n" +
                        "\t\t\t(1) ‘event [time: hh:mm:ss]’\n" +
                        "\t\t\t(2) (description)\n" +
                        "\n\tType ‘end’ to finish logging session and write log data to file.\n" +
                        "\nEx:\n" +
                        "\tstart (OR) start -n “My New Log” (OR) start -t 7:40 (OR) start -n DifferentTimeLog -t 7:30:00\n" +
                        "\t...\n" +
                        "\tevent\n" +
                        "\tHello Easy Events!\n" +
                        "\tevent -t 7:45\n" +
                        "\tYour second event!\n" +
                        "\t...\n" +
                        "\tend\n" +
                        "\t...\n" +
                        "-------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays, to the console, the general information for the newly started session.
     */
    public static void displaySession(String sessionName, String startTime) {
        info(String.format(
                "Logging for session '%s' started at %s\n",
                sessionName,
                startTime));
    }

    /**
     * Displays, to the console, a specific command definition or a
     * detailed list of special input terms with their definitions.
     */
    public static void displayUsage(String help) {
        String[] helpOption = help.split(" ");
        String helpWith = helpOption.length > 1 ? helpOption[1].toLowerCase() : "all";

        switch (helpWith) {
            case "start":
                displayStartUsage();
                break;
            case "event":
                displayEventUsage();
                break;
            case "end":
                displayEndUsage();
                break;
            case "help":
                displayHelpUsage();
                break;
            default:
                displayAllUsage();

        }
    }

    /**
     * Helper method to assist in quick usage printing of all commands.
     * Equivalent to invoking displayUsage("help").
     */
    public static void displayUsage() {
        displayUsage("help");
    }

    /**
     * Displays, to the console, the closing messages of Easy Events,
     * including the session stop time, number of events logged and
     * name of the file containing the logged output data.
     *
     * @param endTime         The time that the session was ended.
     * @param numEventsLogged The number of events logged during the session, including the start & end events
     * @param fileName        The name of the file containing logged output data
     *                        including extension, ex. "outputFile.txt".
     */
    public static void displayEnd(String endTime, int numEventsLogged, String fileName) {
        info(String.format(
                "Session event logging ended at %s.\n" +
                        "Logged %d events to file '%s'\n\n" +
                        "Thank you for using Easy Events! Goodbye.",
                endTime, numEventsLogged, fileName));

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ignored) {
        }
    }

    // ACCESSOR METHODS

    /**
     * Allows for setting this IO handler's input stream.
     *
     * @param scanner The Scanner input stream to change to.
     */
    public static void setInputStream(Scanner scanner) {
        EasyEventsIO.prompter = new Prompter(scanner);
    }

    // HELPER METHODS
    private static void displayStartUsage() {
        System.out.println("start -n [-n session name] -t [time: hh:mm:ss]");
        System.out.println(
                "\t{DESCRIPTION}:\n" +
                        "\t\tStarts a logging session which takes in user event data and outputs consolidated events with\n" +
                        "\t\ttheir respective time offsets (from start of session) into an output file which can be used\n" +
                        "\t\tfor reviewing video recordings more quickly.");
        System.out.println(
                "\n\t{OPTIONAL FLAGS}:\n" +
                        "\t-n:\tA [session name] is required when using this flag.\n" +
                        "\t\tDesignates file name of session output file.\n" +
                        "\t\tFile name can only contain alphanumeric characters, underscore's '_',\n" +
                        "\t\t   or spaces ' ' (if name is wrapped with double quotes, ex: \"name with spaces\")\n" +
                        "\t\tIf this flag is not used, output file name will be given a default name using the current date,\n" +
                        "\t\te.g. EasyEvent_03292021.txt\n" +
                        "\n\t-t:\tA [time: hh:mm:ss] is required when using this flag.\n" +
                        "\t\tSets the initial session time to the provided [time: hh:mm:ss]. This is used to calculate\n" +
                        "\t\tevent time offsets.\n" +
                        "\t\tThe provided time must be in 'hh:mm:ss' format and should represent the actual time of\n" +
                        "\t\tthe event.\n" +
                        "\t\tIf this flag is not used, initial session time is set to current time as of command entry.\n");
    }

    private static void displayEventUsage() {
        System.out.println("event -t [time: hh:mm:ss]");
        System.out.println(
                "\t{DESCRIPTION}:\n" +
                        "\t\tLogs an event for the session currently running");
        System.out.println(
                "\t{OPTIONAL FLAGS}:\n" +
                        "\t-t:\tA [time: hh:mm:ss] is required when using this flag.\n" +
                        "\t\tUses the provided value to calculate an event time offset relative to session start time.\n" +
                        "\t\tThe provided time must be in 'hh:mm:ss' format and should represent the actual time of\n" +
                        "\t\tthe event.\n" +
                        "\t\tIf this flag is not used, event time offset is calculated using current time as of command entry.\n");
    }

    private static void displayEndUsage() {
        System.out.println("end");
        System.out.println(
                "\t{DESCRIPTION}:\n" +
                        "\t\tEnds the current recording session and triggers event data to be written to an output file.\n" +
                        "\t\tOnce file writing is complete, program terminates\n");
    }

    private static void displayHelpUsage() {
        System.out.println("help [command]");
        System.out.println(
                "\t{DESCRIPTION}:\n" +
                        "\t\tDisplays special command usage definitions for use while the EasyEvents program is running.");
        System.out.println(
                "\t{OPTIONAL FLAGS}:\n" +
                        "\t[command]:\tOptional argument that designates a specific command to display usage information for.\n" +
                        "\t\tIf [command] is not provided or the provided [command] does not exist,\n" +
                        "\t\t'help' displays usage information for all EasyEvents commands.\n");
    }

    private static void displayAllUsage() {
        displayStartUsage();
        displayEventUsage();
        displayEndUsage();
        displayHelpUsage();
    }

    // OVERRIDES

}